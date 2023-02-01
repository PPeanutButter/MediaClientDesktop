package ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RatingBar(progress: Int,
              size: Dp = 40.dp,
              minValue: Int = 0,
              maxValue: Int = 100){
    Surface(color = Color.Transparent,
        modifier = Modifier
            .size(size)) {
        Background(1.dp){
            Indicator(paddingSize = 2.dp, progress = progress, minValue = minValue, maxValue=maxValue){ realProgress ->
                Column(verticalArrangement = Arrangement.Center) {
                    Row(Modifier.fillMaxWidth()) {
                        Text(text = realProgress.toString(), color = Color.White, modifier = Modifier.weight(2f), textAlign = TextAlign.Right)
                        Text(text = "%", fontSize = 8.sp, color = Color.White, modifier = Modifier.weight(1f).padding(top = 2.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Background(paddingSize: Dp, content: @Composable () -> Unit){
    Surface(color = Color.Transparent,
        modifier = Modifier
            .padding(paddingSize)
            .fillMaxSize()
            .drawBehind {
                drawCircle(color = Color(java.awt.Color.decode("#081C22").rgb))
            }, content = content)
}

@Composable
fun Indicator(paddingSize: Dp, progress: Int, minValue: Int = 0,
              maxValue: Int = 100, content: @Composable (realProgress: Int) -> Unit){
    val realProgress = ((if (progress < minValue) minValue else if (progress > maxValue) maxValue else progress) - minValue / (maxValue - minValue))
    Surface(color = Color.Transparent,
        modifier = Modifier
            .padding(paddingSize)
            .fillMaxSize()
            .drawBehind {
                drawCircle(color = Color(java.awt.Color.decode("#204529").rgb), style = Stroke(width = 5.dp.value))
                drawArc(
                    color = Color(java.awt.Color.decode("#21D07A").rgb),
                    startAngle = -90f,
                    sweepAngle = realProgress * 3.6f,
                    useCenter = false,
                    style = Stroke(width = 5.dp.value, cap = StrokeCap.Round)
                )
            }, content = {content(realProgress)})
}
