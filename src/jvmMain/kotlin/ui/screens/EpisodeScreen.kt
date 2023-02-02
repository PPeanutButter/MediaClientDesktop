package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ui.component.EpisodeList
import data.RequestStore
import gson.Episode
import gson.TMDB
import kotlinx.coroutines.launch
import model.EpisodeViewModel
import model.SharedViewModel
import ui.component.EpisodePost


@Composable
fun EpisodeScreen(sharedViewModel: SharedViewModel, viewModel: EpisodeViewModel, albumRelativePath: String){
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit){
        viewModel.getEpisodes("/$albumRelativePath")
        viewModel.getTmdbInfo("/$albumRelativePath/.info")
        scope.launch{
            if (viewModel.episodeResponse.value is RequestStore.Success){
                viewModel.preDownloadSubtitles((viewModel.episodeResponse.value as RequestStore.Success<Episode>)._datas)
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        val tmdb = viewModel.tmdbResponse.value
        EpisodePost(sharedViewModel = sharedViewModel, tmdb = if (tmdb !is RequestStore.Success) TMDB.Empty else tmdb._data!!,
            albumRelativePath = albumRelativePath
        )
        when (val result = viewModel.episodeResponse.value) {
            is RequestStore.Success -> {
                EpisodeList(episodes = result._datas, sharedViewModel, viewModel)
            }

            is RequestStore.Failure -> {
                EpisodeError(msg = result.message)
            }

            else -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Empty Data!")
                }
            }
        }
    }
}

@Composable
fun EpisodeError(msg: String) {
    Text(text = "Error: $msg", color = Color.Red)
}