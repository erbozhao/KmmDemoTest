package com.example.kmmdemotest.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun ListViewDemo() {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {

        Text(text = "test", modifier = Modifier.fillMaxWidth().height(100.dp).wrapContentSize(align = Alignment.BottomCenter), color = Color.Black)

        val tabData = listOf(0, 1)
        var selectedTabIndex by remember { mutableStateOf(0) }
        val pagerState = rememberPagerState(pageCount = { tabData.size })
        LaunchedEffect(selectedTabIndex) {
            pagerState.animateScrollToPage(selectedTabIndex)
        }
        LaunchedEffect(pagerState.currentPage) {
            val curPageIndex = pagerState.currentPage
            val targetPageIndex = pagerState.targetPage
            if (curPageIndex == targetPageIndex) {
                selectedTabIndex = curPageIndex
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Black)
                .weight(1f),
        ) { pageIndex ->
            when (pageIndex) {
                1 -> PageView(listOf(31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50))
                else -> PageView(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20))
            }
        }
    }
}

@Composable
private fun PageView(list: List<Int>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color.Black),
    ) {
        items(list) { item ->
            Text(
                modifier = Modifier.fillMaxWidth().height(50.dp).wrapContentSize(align = Alignment.Center),
                text = item.toString(),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
            )
        }
    }
}
