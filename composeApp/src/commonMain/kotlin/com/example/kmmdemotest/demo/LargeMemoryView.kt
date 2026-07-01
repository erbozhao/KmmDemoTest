package com.example.kmmdemotest.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.overscroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * @author onuszhao
 * @since 2025/10/30
 * @description
 */

@Composable
fun LargeMemoryView() {
    val result = mutableListOf<ByteArray>()
    repeat(1) {
        val largeByteArray = ByteArray(500 * 1024 * 1024)
        largeByteArray[0] = 1
        result.add(largeByteArray)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color.Black).overscroll(null),
    ) {
        itemsIndexed(result) { index, item ->
            Text(
                modifier = Modifier.fillMaxWidth().height(50.dp).wrapContentSize(align = Alignment.Center),
                text = "$index 数组大小: ${item.size / (1024 * 1024)} MB",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
            )
        }
    }
}