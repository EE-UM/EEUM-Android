package com.example.eeum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eeum.R

@Composable
fun MyPostsMenuScreen(
    onClose: () -> Unit,
    onOpenPosts: () -> Unit,
    onOpenComments: () -> Unit,
    onOpenLikes: () -> Unit
) {
    val bg = Color(0xFFF7F6F2)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Menu",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1D1D1D)
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = "close")
            }
        }

        Spacer(Modifier.height(16.dp))

        MenuItem(text = "Posts", onClick = onOpenPosts)
        MenuItem(text = "Comments", onClick = onOpenComments)
        MenuItem(text = "Likes", onClick = onOpenLikes)
    }
}

@Composable
fun MyCommentsScreen(
    onHome: () -> Unit,
    onOpenMenu: () -> Unit
) {
    val bg = Color(0xFFF7F6F2)
    val items = List(5) { index ->
        "사연 제목 ${index + 1}"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp)
    ) {
        TopNavigationRow(onHome = onHome, onOpenMenu = onOpenMenu)

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Comments",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1D1D)
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "3개\n참여한 사연과 플레이리스트입니다.",
            fontSize = 12.sp,
            color = Color(0xFF7A7A7A)
        )

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { title ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE6E2DC))
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 4.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1D1D1D)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "사연 제목 사연 제목 사연 제목",
                            fontSize = 10.sp,
                            color = Color(0xFF7A7A7A),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyLikesScreen(
    onHome: () -> Unit,
    onOpenMenu: () -> Unit
) {
    val bg = Color(0xFFF7F6F2)
    val items = List(6) { index ->
        "사연 제목 ${index + 1}"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp)
    ) {
        TopNavigationRow(onHome = onHome, onOpenMenu = onOpenMenu)

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Likes",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1D1D)
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "352개\n좋아요 한 플레이리스트입니다.",
            fontSize = 12.sp,
            color = Color(0xFF7A7A7A)
        )

        Spacer(Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(items) { title ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFE6E2DC))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = Color(0xFFD4574B),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .size(16.dp)
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1D1D1D),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Evergreen Griffin Au/Ra",
                                fontSize = 10.sp,
                                color = Color(0xFF7A7A7A),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(Modifier.width(6.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TopNavigationRow(
    onHome: () -> Unit,
    onOpenMenu: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onHome) {
            Icon(Icons.Filled.Home, contentDescription = "home")
        }
        IconButton(onClick = onOpenMenu) {
            Icon(
                painter = painterResource(id = R.drawable.ic_multi),
                contentDescription = "menu"
            )
        }
    }
}

@Composable
private fun MenuItem(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1D1D1D),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
    )
}
