package ui.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import data.Configuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import network.ApiService
import javax.imageio.ImageIO

@Composable
fun CoverImage(albumRelativePath: String, modifier: Modifier = Modifier) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    imageBitmap?.let {
        Image(bitmap = imageBitmap!!, contentDescription = null, modifier = modifier, contentScale = ContentScale.Crop)
    }
    LaunchedEffect(true){
        MainScope().launch(Dispatchers.IO){
            val api = ApiService.getInstance(Configuration.Empty)
            val r = api.getCover(albumRelativePath)
            r.byteStream().let{
                imageBitmap = ImageIO.read(it).toComposeImageBitmap()
            }
        }
    }
}