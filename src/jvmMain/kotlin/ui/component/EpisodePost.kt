package ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gson.TMDB
import kotlinx.coroutines.Dispatchers
import model.EpisodeViewModel.Companion.calculateColorLightValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.SharedViewModel

@Composable
fun EpisodePost(sharedViewModel: SharedViewModel, tmdb: TMDB, albumRelativePath: String) {
    Surface(
        Modifier
            .fillMaxWidth()
            .height(230.dp)
    ) {
        val viewScope = rememberCoroutineScope()
        var vibrantBody by remember { mutableStateOf(java.awt.Color.decode("#7367EF").rgb) }
        var textColor by remember { mutableStateOf(Color.Black) }

        PostImage(
            path = "$albumRelativePath/.post", modifier = Modifier
                .fillMaxSize()
                .background(Color(vibrantBody)), alpha = 0.15f
        )
        Column(modifier = Modifier.fillMaxSize()) {
            BackAction(modifier = Modifier.padding(start = 16.dp, top = 12.dp), tint = textColor) {
                sharedViewModel.episodeState.value = ""
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
            ) {
                CoverImage(albumRelativePath = albumRelativePath, modifier = Modifier
                    .heightIn(150.dp)
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { /* todo download attachments */ },
                    onSuccess = {
                        viewScope.launch {
                            withContext(Dispatchers.Default) {
                                var main: java.awt.Color = java.awt.Color.decode("#7367EF")
                                try {
                                    main = palette.Palette.generate(it).toJavaColor()
                                }catch (_:Exception){
                                    println("Palette Error")
                                }
                                vibrantBody = main.rgb
                                val light = calculateColorLightValue(vibrantBody)
                                textColor = if (light < 0.4) Color.White else Color.Black
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.padding(end = 16.dp))
                Column {
                    Text(
                        text = tmdb.displayTitle,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        color = textColor
                    )
                    Text(text = tmdb.infoDescription, fontSize = 16.sp, maxLines = 2, color = textColor)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RatingBar(progress = tmdb.userScoreRating)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "用户评价",
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Italic,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = textColor
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = tmdb.displayTagDescription,
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = textColor
                    )
                }
            }
        }
    }
}