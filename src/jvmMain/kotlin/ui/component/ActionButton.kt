package ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingAction(onClicked: () -> Unit) {
    IconButton(onClick = { onClicked() }) {
        Icon(imageVector = Icons.Rounded.Logout, contentDescription = null)
    }
}

@Composable
fun PlayAction(onClicked: () -> Unit) {
    IconButton(onClick = { onClicked() }, modifier = Modifier.size(24.dp)) {
        Icon(imageVector = Icons.Rounded.PlayCircle, contentDescription = null)
    }
}

@Composable
fun DownloadAction(onClicked: () -> Unit) {
    IconButton(onClick = { onClicked() }, modifier = Modifier.size(24.dp)) {
        Icon(imageVector = Icons.Rounded.Link, contentDescription = null)
    }
}

@Composable
fun BookmarkAction(onClicked: () -> Unit) {
    IconButton(onClick = { onClicked() }, modifier = Modifier.size(24.dp)) {
        Icon(imageVector = Icons.Rounded.BookmarkAdd, contentDescription = null)
    }
}

@Composable
@Preview
private fun ActionsPreview(){
    Column {
        SettingAction {}
        PlayAction {}
        DownloadAction {}
        BookmarkAction {}
    }
}