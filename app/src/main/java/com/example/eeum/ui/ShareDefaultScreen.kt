package com.example.eeum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShareDefaultScreen(
    onBack: () -> Unit,
    onOpenMusicSearch: () -> Unit
) {
    val bg = Color(0xFFF7F6F2)
    var title by remember { mutableStateOf("") }
    val maxLength = 200

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.Home, contentDescription = "back to home")
            }
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "share"
            )
        }

        Spacer(Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFFE6E2DC), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onOpenMusicSearch) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "search music"
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = "사연의 제목을 작성해 주세요",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1D1D1D)
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "OO자에서 OO자 이내로 자유롭게 공유하고싶은 사연을 작성해 주세요",
            fontSize = 12.sp,
            color = Color(0xFF777777)
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            value = title,
            onValueChange = { title = it.take(maxLength) },
            placeholder = { Text("사연의 제목") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "${title.length}/$maxLength",
            fontSize = 12.sp,
            color = Color(0xFF999999)
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1D1D1D),
                contentColor = Color.White
            )
        ) {
            Text("share", fontSize = 14.sp)
        }
        Spacer(Modifier.height(12.dp))
    }
}
