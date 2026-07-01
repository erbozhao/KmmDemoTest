package com.example.kmmdemotest.ksp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import kmmdemotest.composeapp.generated.resources.Res
import kmmdemotest.composeapp.generated.resources.img_checken
import org.jetbrains.compose.resources.painterResource

class LocalSplash : ISplash {
    override val splashPriority: Int = 0

    override fun canShow(openType: Int): Boolean {
        return true
    }

    @Composable
    override fun ShowSplash(openType: Int) {
        Box(
            modifier = Modifier.wrapContentSize()
        ) {
            Image(
                modifier = Modifier.wrapContentSize(),
                painter = painterResource(Res.drawable.img_checken),
                colorFilter = null,
                contentDescription = "Image"
            )

            Text(
                modifier = Modifier.fillMaxSize(),
                text = "This is a test Text!!",
                color = Color.Black.copy(alpha = 0.8f), // 可替换为具体的颜色资源
                fontSize = 12.sp,
                lineHeight = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}