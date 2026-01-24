package com.example.eeum.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eeum.ui.components.*

@Composable
fun HomeShakenScreen(onReply: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        TopSection(showRightCurve = true)

        Spacer(Modifier.height(32.dp))

        Text("어머니께", fontSize = 16.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(12.dp))

        Text(
            text =
                "어려서부터 우리 집은 가난했고 늘 더하는 의식 못 변한 적이 없었고...\n\n" +
                        "어려서부터 우리 집은 가난했고 늘 더하는 의식 못 변한 적이 없었고...",
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onReply,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("reply")
        }

        Spacer(Modifier.height(12.dp))
        BottomNav()
    }
}
