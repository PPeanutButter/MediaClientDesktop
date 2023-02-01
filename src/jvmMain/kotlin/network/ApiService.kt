package network

import gson.Album
import data.Configuration
import gson.Episode
import gson.TMDB
import utils.SettingManager
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        private var apiService: ApiService? = null

        fun getInstance(configuration: Configuration): ApiService {
            if (apiService == null){
                synchronized(ApiService::class.java){
                    if (apiService == null){
                        apiService = Retrofit
                            .Builder()
                            .baseUrl(configuration.serverIp)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(
                                OkHttpClient.Builder()
                                    .cookieJar(object : CookieJar {
                                        override fun loadForRequest(url: HttpUrl): List<Cookie> {
                                            println("OkHttpClient => loadForRequest: $url")
                                            return if (SettingManager.token != "") listOf(
                                                Cookie.Builder().name("token")
                                                    .value(SettingManager.token)
                                                    .domain(SettingManager.getValue("token_domain", ""))
                                                    .build()
                                            ) else emptyList()
                                        }

                                        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                                            for (cookie in cookies) {
                                                if (cookie.name == "token") {
                                                    SettingManager.token = cookie.value
                                                    SettingManager["token_domain"] = cookie.domain
                                                }
                                            }
                                        }
                                    })
                                    .build()
                            )
                            .build()
                            .create(ApiService::class.java)
                    }
                    return apiService!!
                }
            }
            return apiService!!
        }
    }

    @GET("/userLogin")
    suspend fun userLogin(@Query("name") name: String, @Query("psw") password: String)

    @GET("/getFileList")
    suspend fun getAlbumList(@Query("path") path: String = "/"): Array<Album>

    @GET("/getFileList")
    suspend fun getEpisodeList(@Query("path") path: String): Array<Episode>

    @GET("/getFile/get_album_info")
    suspend fun getTmdbInfo(@Query("path") path: String): TMDB

    @GET("/getCover")
    suspend fun getCover(@Query("cover") path: String): ResponseBody
}