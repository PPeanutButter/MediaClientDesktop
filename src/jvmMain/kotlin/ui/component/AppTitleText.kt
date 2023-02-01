package ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle

@Composable
fun AppTitleText(modifier: Modifier = Modifier, textAlign: TextAlign? = null){
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Peanut")
            }
            append(" ")
            append("NAS")
        },
        maxLines = 1,
        modifier = modifier,
        textAlign = textAlign,
        overflow = TextOverflow.Ellipsis,
    )
}