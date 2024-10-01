package com.grg.core.presentation.ui.components.navigationdrawer

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.grg.core.presentation.ui.theme.SpotifyDarkRed


@Composable
fun ProfileNameIcon(
    userInitials: Char,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(SpotifyDarkRed)
            .layoutId("profileIcon")
            .clickable(onClick = {
                Log.d("SomeTag", "Profile icon clicked")
                onClick()
            }),

        contentAlignment = Alignment.Center
    ) {
        Text(
            text = userInitials.toString(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}