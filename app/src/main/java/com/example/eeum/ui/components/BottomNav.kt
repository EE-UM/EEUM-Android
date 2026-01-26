package com.example.eeum.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eeum.R

@Composable
fun BottomNav(
    modifier: Modifier = Modifier,
    onFeed: (() -> Unit)? = null,
    onShare: (() -> Unit)? = null
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        NavItem(
            icon = { Icon(painterResource(R.drawable.ic_feed), contentDescription = null) },
            label = "feed",
            onClick = onFeed
        )
        NavItem(
            icon = { Icon(painterResource(R.drawable.ic_share), contentDescription = null) },
            label = "share",
            onClick = onShare
        )
    }
}

@Composable
private fun NavItem(
    icon: @Composable () -> Unit,
    label: String,
    onClick: (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.then(
            if (onClick != null) {
                Modifier.clickable { onClick() }
            } else {
                Modifier
            }
        )
    ) {
        Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
            icon()
        }
        Spacer(Modifier.height(6.dp))
        Text(label, fontSize = 10.sp)
    }
}
