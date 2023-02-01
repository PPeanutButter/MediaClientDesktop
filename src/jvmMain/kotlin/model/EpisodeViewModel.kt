package model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import data.Configuration
import data.RequestStore
import gson.Episode
import gson.TMDB
import kotlinx.coroutines.Dispatchers
import network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

}