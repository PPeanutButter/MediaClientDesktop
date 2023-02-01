package ui.component

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import gson.Album
import model.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumCard(album: Album, viewModel: SharedViewModel) {
    OutlinedCard(
        onClick = {
            viewModel.episodeState.value = album.relativePath
        }, modifier = Modifier.padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        CoverImage(album.relativePath, modifier = Modifier
            .clip(CardDefaults.outlinedShape)
            .fillMaxWidth()
            .aspectRatio(2f / 3f))
        Text(
            text = album.displayTitle, modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(), maxLines = 1, style = MaterialTheme.typography.titleSmall
        )
    }
}