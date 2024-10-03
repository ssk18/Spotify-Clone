package com.grg.core.presentation.ui.components.topappbar

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableList
import androidx.compose.ui.unit.dp
import com.grg.core.presentation.ui.components.navigationdrawer.ProfileNameIcon
import com.grg.core.presentation.ui.theme.SpotifyGreen

@Composable
fun CustomTopAppBar(
    userInitials: Char,
    selectedText: String,
    onTextSelected: (String) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = 40.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileNameIcon(
            userInitials = userInitials,
            onClick = onProfileClick
        )
        Spacer(modifier = Modifier.width(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(TopAppBarScreens.values()) { screen ->
                TopAppBarItem(
                    title = screen.text,
                    isSelected = screen.text == selectedText,
                    onClick = { onTextSelected(screen.text) }
                )
            }
        }
    }
}

@Composable
fun TopAppBarItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) SpotifyGreen else Color.Gray
    val textColor = if (isSelected) Color.Black else Color.White

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
