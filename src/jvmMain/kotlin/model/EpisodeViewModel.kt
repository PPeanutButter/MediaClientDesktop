package model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import data.Configuration
import data.RequestStore
import gson.Episode
import gson.TMDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import net.harawata.appdirs.AppDirsFactory
import network.ApiService
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.Executors


class EpisodeViewModel {
    init {
        println("EpisodeViewModel init")
    }
    var episodeResponse: MutableState<RequestStore<Episode>> = mutableStateOf(RequestStore.Empty())
    var tmdbResponse: MutableState<RequestStore<TMDB>> = mutableStateOf(RequestStore.Empty())

    private val _configuration = MutableStateFlow(Configuration.Empty)
    val configuration: StateFlow<Configuration> = _configuration


    suspend fun getEpisodes(path: String){
        println("EpisodeViewModel => getEpisodes for ($path)")
        withContext(Dispatchers.IO) {
            episodeResponse.value = RequestStore.Loading()
            val apiService = ApiService.getInstance(configuration.value)
            try {
                val data: Array<Episode> = apiService.getEpisodeList(path = path)
                episodeResponse.value = RequestStore.Success(data.toMutableList())
            }catch (e:Exception){
                e.printStackTrace()
                episodeResponse.value = RequestStore.Failure(e.localizedMessage?:"unknown error")
            }
        }
    }

    suspend fun getTmdbInfo(path: String){
        println("EpisodeViewModel => getTmdbInfo for ($path)")
        withContext(Dispatchers.IO) {
            tmdbResponse.value = RequestStore.Loading()
            val apiService = ApiService.getInstance(configuration.value)
            try {
                val data: TMDB = apiService.getTmdbInfo(path = path)
                tmdbResponse.value = RequestStore.Success(data)
            }catch (e:Exception){
                e.printStackTrace()
                tmdbResponse.value = RequestStore.Failure(e.localizedMessage?:"unknown error")
            }
        }
    }

    companion object{
        fun calculateColorLightValue(argb: Int):Double{
            return try {
                val r = argb shr 16 and 0xff
                val g = argb shr 8 and 0xff
                val b = argb and 0xff
                (0.299 * r + 0.587 * g + 0.114 * b)/255.0
            }catch (e:Exception){
                e.printStackTrace()
                0.0
            }
        }
    }

    suspend fun preDownloadSubtitles(episodes: MutableList<Episode>) {
        withContext(Dispatchers.IO){
            val appDirs = AppDirsFactory.getInstance()
            val root = appDirs.getUserCacheDir("NAS", "subtitleList", "PeanutButter")
            File(root).mkdirs()
            for (episode in episodes){
                val cacheFile = File(root, episode.absolutePath.replace("/", "_")+".json")
                if (!cacheFile.exists()) {
                    ("https://api-shoulei-ssl.xunlei.com/oracle/subtitle?" +
                            "name=${episode.episodeDisplayName}&" +
                            "duration=${episode.seconds}"
                            ).http()?.let {
                            cacheFile.writeText(it)
                        }
                }
            }
        }
    }

    suspend fun getPreDownloadSubtitles(episode: Episode):JSONObject?{
        return withContext(Dispatchers.IO) {
            val appDirs = AppDirsFactory.getInstance()
            val root = appDirs.getUserCacheDir("NAS", "subtitleList", "PeanutButter")
            val cacheFile = File(root, episode.absolutePath.replace("/", "_")+".json")
            if (cacheFile.exists()){
                return@withContext JSON.parseObject(cacheFile.readText())
            } else {
                return@withContext null
            }
        }
    }

    suspend fun downloadSubtitles(json: JSONObject):File {
        return withContext(Dispatchers.IO){
            val appDirs = AppDirsFactory.getInstance()
            val root = appDirs.getUserCacheDir("NAS", "subtitleAvailable", "PeanutButter")
            //用于存放当前播放剧集的字幕，每次会清空
            val ava = File(root).apply {
                if (this.exists()){
                    Files.walkFileTree(this.toPath(), object : SimpleFileVisitor<Path>() {
                        override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                            file?.let { Files.delete(it) }
                            return FileVisitResult.CONTINUE
                        }
                    })
                }
                this.mkdirs()
            }
            //存放所有字幕文件的下载缓存
            val cacheRoot = appDirs.getUserCacheDir("NAS", "subtitleCache", "PeanutButter")
            val cache = File(cacheRoot).apply {
                this.mkdirs()
            }
            if (json.getString("result") == "ok") {
                val datas = json.getJSONArray("data")
                val downloaderService = Executors.newFixedThreadPool(4)
                for (i in 0 until datas.size) {
                    val data = datas.getJSONObject(i)
                    val url = data.getString("url")
                    val name = data.getString("name")
                    val cacheFile = File(cache, name)
                    if (!cacheFile.exists()){
                        downloaderService.execute {
                            val client = getHttpClient()
                            val request: Request = Request.Builder()
                                .url(url)
                                .build()
                            val r = client.newCall(request).execute()
                            r.body?.bytes()?.let {
                                cacheFile.writeBytes(it)
                            }
                            copyFileUsingFileChannels(cacheFile, File(ava, name))
                        }
                    }else{
                        copyFileUsingFileChannels(cacheFile, File(ava, name))
                    }
                }
            }
            return@withContext ava
        }
    }

    suspend fun String.http(): String? {
        val body = withContext(Dispatchers.IO){
            val client = getHttpClient()
            val request: Request = Request.Builder()
                .url(this@http)
                .build()
            client.newCall(request).execute()
        }
        return body.body?.string()
    }

    private var okHttpClient: OkHttpClient? = null

    fun getHttpClient(): OkHttpClient {
        if (okHttpClient == null) {
            synchronized("okHttpClient") {
                if (okHttpClient == null) {
                    okHttpClient = OkHttpClient.Builder()
                        .build()
                }
            }
        }
        return okHttpClient!!
    }

    @Throws(IOException::class)
    private fun copyFileUsingFileChannels(source: File, dest: File) {
        var inputChannel: FileChannel? = null
        var outputChannel: FileChannel? = null
        try {
            inputChannel = FileInputStream(source).channel
            outputChannel = FileOutputStream(dest).channel
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size())
        } finally {
            inputChannel!!.close()
            outputChannel!!.close()
        }
    }

}