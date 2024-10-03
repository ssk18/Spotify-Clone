package com.grg.spotify.ui.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.grg.core.domain.DataState
import com.grg.core.presentation.ui.components.ErrorWithTryAgain
import com.grg.core.presentation.ui.components.LoadingDataIndicator
import com.grg.spotify.R
import com.grg.spotify.domain.model.Item

@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeViewModel
) {
    val dataState by viewModel.followedArtistsInfoFlow.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
    ) {
        when (val stateValue = dataState) {
            is DataState.Success -> {
                Spacer(Modifier.height(30.dp))
                FollowedArtist(
                    artists = stateValue.result.artist.artists.filterIsInstance<Item.Artist>(),
                )
            }

            is DataState.Failure -> {
                ErrorWithTryAgain(errorMessage = stateValue.throwable.message.orEmpty()) {
                    viewModel.followedArtistsInfoFlow::restart
                }
            }

            DataState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingDataIndicator("Loading Spotify data...")
                }

            }
            DataState.Uninitialized -> CircularProgressIndicator()
        }
    }
}

@Composable
private fun FollowedArtistItem(
    artistImageUrl: String,
    artistName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .height(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = artistImageUrl,
            contentDescription = stringResource(R.string.artist_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = artistName,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FollowedArtist(
    artists: List<Item.Artist>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.followed_artists),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(10.dp))
        LazyRow {
            items(artists) {
                FollowedArtistItem(
                    artistName = it.name,
                    artistImageUrl = it.image.first().url
                )
                Spacer(Modifier.width(10.dp))
            }
        }
    }
}
