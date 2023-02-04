package ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import data.Configuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.harawata.appdirs.AppDirsFactory
import network.ApiService
import org.jetbrains.skiko.toBitmap
import palette.ImageResizer
import utils.MD5Utils
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

@Composable
fun CoverImage(albumRelativePath: String, modifier: Modifier = Modifier) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    imageBitmap?.let {
        Image(bitmap = it, contentDescription = null, modifier = modifier.aspectRatio(2f/3f), contentScale = ContentScale.Crop)
    }
    LaunchedEffect(Unit) {
        MainScope().launch(Dispatchers.IO) {
            val c = readCache("getCover$albumRelativePath")
            if (c == null){
                val api = ApiService.getInstance(Configuration.Empty)
                val r = api.getCover(albumRelativePath)
                r.byteStream().let {
                    var a = ImageIO.read(it)
                    a = ImageResizer.resize(a, 300, 450)
                    imageBitmap = a.toBitmap().asComposeImageBitmap()
                    writeCache("getCover$albumRelativePath", a)
                }
            }else{
                imageBitmap = c.toBitmap().asComposeImageBitmap()
            }
        }
    }
}

@Composable
fun PostImage(path: String, modifier: Modifier = Modifier, alpha: Float = 1f, onSuccess:(BufferedImage)->Unit = {}) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    imageBitmap?.let {
        Image(
            bitmap = imageBitmap!!,
            contentDescription = null,
            modifier = modifier.blur(
                radiusX = 5.dp,
                radiusY = 5.dp,
                edgeTreatment = BlurredEdgeTreatment.Rectangle
            ),
            contentScale = ContentScale.Crop,
            alpha = alpha
        )
    }
    LaunchedEffect(true) {
        MainScope().launch(Dispatchers.IO) {
            val c = readCache("getPostImage$path")
            if (c == null){
                val api = ApiService.getInstance(Configuration.Empty)
                val r = api.getPostImage(path)
                r.byteStream().let {
                    var a = ImageIO.read(it)
                    a = ImageResizer.resize(a, 426, 240)
                    imageBitmap = a.toBitmap().asComposeImageBitmap()
                    onSuccess(ImageResizer.resize(a, 106, 60))
                    writeCache("getPostImage$path", a)
                }
            }else{
                imageBitmap = c.toBitmap().asComposeImageBitmap()
                onSuccess(ImageResizer.resize(c, 106, 60))
            }
        }
    }
}

@Composable
fun VideoPreviewImage(path: String, modifier: Modifier = Modifier, onSuccess:(ratio: Float)->Unit) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    imageBitmap?.let {
        Image(
            bitmap = imageBitmap!!,
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
    LaunchedEffect(true) {
        MainScope().launch(Dispatchers.IO) {
            val c = readCache("getVideoPreview$path")
            if (c == null){
                val api = ApiService.getInstance(Configuration.Empty)
                val r = api.getVideoPreview(path)
                r.byteStream().let {
                    var a = ImageIO.read(it)
                    val ratio = a.width.toFloat()/a.height.toFloat()
                    onSuccess(ratio)
                    a = ImageResizer.resize(a, (240 * ratio).toInt(), 240)
                    imageBitmap = a.toBitmap().asComposeImageBitmap()
                    writeCache("getVideoPreview$path", a)
                }
            }else{
                imageBitmap = c.toBitmap().asComposeImageBitmap()
                val ratio = c.width.toFloat()/c.height.toFloat()
                onSuccess(ratio)
            }
        }
    }
}

suspend fun writeCache(key: String, im: BufferedImage){
    withContext(Dispatchers.IO){
        val k = MD5Utils.string2MD5(key)
        val appDirs = AppDirsFactory.getInstance()
        val root = appDirs.getUserCacheDir("NAS", "ImageCache", "PeanutButter")
        val p = File(root).apply {
            this.mkdirs()
        }
        ImageIO.write(im, "png", File(p, "$k.png"))
    }
}

suspend fun readCache(key: String): BufferedImage?{
    return withContext(Dispatchers.IO) {
        val k = MD5Utils.string2MD5(key)
        val appDirs = AppDirsFactory.getInstance()
        val root = appDirs.getUserCacheDir("NAS", "ImageCache", "PeanutButter")
        val p = File(root).apply {
            this.mkdirs()
        }
        val f = File(p, "$k.png")
        return@withContext if (f.exists()) {
            ImageIO.read(f)
        }else null
    }
}