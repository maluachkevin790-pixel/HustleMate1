package com.example.hustlemate.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.hustlemate.ui.theme.SkyBlueDark

@Composable
fun WishlistHeartButton(
    isSaved: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (isSaved)
                Icons.Default.Favorite
            else
                Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = if (isSaved)
                MaterialTheme.colorScheme.error
            else
                SkyBlueDark
        )
    }
}
