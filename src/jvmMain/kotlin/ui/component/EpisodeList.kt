package ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gson.Episode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.EpisodeViewModel
import model.SharedViewModel
import utils.SettingManager
import java.util.Base64

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpisodeList(episodes: MutableList<Episode>, sharedViewModel: SharedViewModel, viewModel: EpisodeViewModel){
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.padding(start = 4.dp, end = 4.dp)) {
        items(items = episodes, key = { item: Episode -> item.hashKey }){
            EpisodeCard(episode = it, sharedViewModel, viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeCard(episode: Episode, sharedViewModel: SharedViewModel, viewModel: EpisodeViewModel) {
    OutlinedCard(
        onClick = { }, modifier = Modifier.padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        var ratio by remember { mutableStateOf(1280f/720f) }
        VideoPreviewImage(
            path = episode.absolutePath, modifier = Modifier
                .clip(CardDefaults.outlinedShape)
                .fillMaxWidth()
                .aspectRatio(ratio),
            onSuccess = {
                ratio = it
            }
        )
        EpisodeCardContent(episode = episode, sharedViewModel, viewModel)
    }
}

@Composable
fun EpisodeCardContent(episode: Episode, sharedViewModel: SharedViewModel, viewModel: EpisodeViewModel){
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = episode.episodeDisplayName, fontSize = 14.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            //会先绘制unweighted，再分配剩余空间给weighted组件
            Text(text = episode.bitrateDescription + " " + episode.downloadDate, fontSize = 12.sp, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PlayAction {
                    Windows {
                        scope.launch(Dispatchers.IO){
                            try {
                                val token = SettingManager.token
                                val c = sharedViewModel.configuration.value
                                val url = "${c.serverIp}/getFile2/${episode.episodeDisplayName}?" +
                                        "path=${Base64.getUrlEncoder().encodeToString(episode.absolutePath.encodeToByteArray())}^&" +
                                        "token=$token"
                                Runtime.getRuntime().exec(arrayOf("cmd", "/c", "start", "potplayer://$url"))
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    // download subtitle service
                    scope.launch{
                        viewModel.getPreDownloadSubtitles(episode)?.let {
                            Runtime.getRuntime().exec(arrayOf("cmd", "/c", "start", viewModel.downloadSubtitles(it).absolutePath))
                        }
                    }
                }
                DownloadAction {

                }
                BookmarkAction {

                }
            }
        }
    }
}