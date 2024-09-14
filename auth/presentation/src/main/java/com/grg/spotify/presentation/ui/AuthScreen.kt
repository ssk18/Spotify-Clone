import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.grg.core.presentation.ui.theme.MySpotifyTheme

@Composable
fun AuthScreen(modifier: Modifier = Modifier) {
    Scaffold { paddingValues ->
        Text(
            text = "Authenticating",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(paddingValues)

        )
    }

}

@Preview
@Composable
fun AuthScreenPreview() {
    MySpotifyTheme {
        AuthScreen()
    }
}
