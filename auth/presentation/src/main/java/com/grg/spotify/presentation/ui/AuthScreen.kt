import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.grg.core.presentation.ui.theme.MySpotifyTheme

@Composable
fun AuthScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Authenticating",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center

    )
}

@Preview
@Composable
fun AuthScreenPreview() {
    MySpotifyTheme {
        AuthScreen()
    }
}
