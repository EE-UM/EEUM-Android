package com.example.eeum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eeum.ui.components.BottomNav
import com.example.eeum.ui.components.CurveDecoration

@Composable
fun HomeShakenScreen(onReply: () -> Unit) {
    val bg = Color(0xFFF7F6F2)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            Text(
                text = "eeum",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))

            CurveDecoration(
                showRight = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .offset(x = (-64).dp)
            )

            Spacer(Modifier.height(28.dp))

            Text(
                text = "어머니께",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1D1D1D)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text =
                    "어려서부터 우리 집은 가난했고 늘 더하는 의식 못 변한 적이 없었고...\n\n" +
                        "어려서부터 우리 집은 가난했고 늘 더하는 의식 못 변한 적이 없었고...",
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = Color(0xFF555555)
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onReply,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1D1D1D),
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Reply,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("reply", fontSize = 14.sp)
            }

            Spacer(Modifier.height(12.dp))
            BottomNav(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 64.dp, vertical = 6.dp)
            )
        }
    }
}
