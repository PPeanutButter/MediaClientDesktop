package model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import data.Configuration
import data.RequestStore
import gson.Album
import network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedViewModel {
    init {
        println("SharedViewModel init")
    }
    var albumResponse: MutableState<RequestStore<Album>> = mutableStateOf(RequestStore.Empty())

    fun getAlbumList(){
        MainScope().launch(Dispatchers.IO) {
            albumResponse.value = RequestStore.Loading()
            val apiService = ApiService.getInstance(configuration.value)
            try {
                val data: Array<Album> = apiService.getAlbumList()
                albumResponse.value = RequestStore.Success(data.toMutableList())
            }catch (e:Exception){
                e.printStackTrace()
                albumResponse.value = RequestStore.Failure(e.localizedMessage?:"unknown error")
            }
        }
    }

    val loginState: MutableState<RequestStore<Int>> = mutableStateOf(RequestStore.Empty())
    fun userLogin(){
        MainScope().launch(Dispatchers.IO) {
            val configuration = configuration.value
            loginState.value = RequestStore.Loading()
            val apiService = ApiService.getInstance(configuration)
            try {
                apiService.userLogin(name = configuration.userName, password = configuration.userPassword)
                loginState.value = RequestStore.Success(0)
            }catch (e:Exception){
                loginState.value = RequestStore.Failure(e.localizedMessage?:"unknown error")
            }
        }
    }

    private val _configuration = MutableStateFlow(Configuration.Empty)
    val configuration: StateFlow<Configuration> = _configuration

    suspend fun loadConfiguration(dataStore: ConfigurationDataStore){
        dataStore.getConfiguration().let{
            _configuration.value = it
        }
    }

    fun setConfiguration(configuration: Configuration) {
        _configuration.value = configuration
    }

    var episodeState: MutableState<String> = mutableStateOf("")
}