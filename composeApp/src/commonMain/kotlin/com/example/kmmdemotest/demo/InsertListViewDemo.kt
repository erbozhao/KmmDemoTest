package com.example.kmmdemotest.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kmmdemotest.ext.NoOpOverscrollEffect
import com.example.kmmdemotest.ext.rememberReadViewSnapFlingBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author onuszhao
 * @since 2025/10/30
 * @description
 */

private val preList = mutableListOf<String>().apply {
    repeat(129) {
        add("pre_${it + 1}")
    }
}

private val curList = mutableListOf<String>().apply {
    repeat(150) {
        add("cur_${it + 1}")
    }
}

private val nextList = mutableListOf<String>().apply {
    repeat(150) {
        add("next_${it + 1}")
    }
}

private val list = mutableStateListOf<String>()

private val uiScope = CoroutineScope(Dispatchers.Main)

@Composable
fun InsertListViewDemo() {

    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Text(text = "test", modifier = Modifier.fillMaxWidth().height(100.dp).wrapContentSize(align = Alignment.BottomCenter), color = Color.Black)

        LazyRow(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            // flingBehavior = rememberReadViewSnapFlingBehavior(listState),
            // userScrollEnabled = true,
            // overscrollEffect = NoOpOverscrollEffect()
        ) {
            itemsIndexed(
                list
                , key = { index, item -> item }
            ) { index, item ->

                val pgColor = when (index % 3) {
                    0 -> Color.Red
                    1 -> Color.Green
                    else -> Color.Blue
                }
                Box(
                    modifier = Modifier.fillParentMaxSize().background(pgColor),
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth().height(50.dp).wrapContentSize(align = Alignment.Center),
                        text = item,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }

    val hasInit = remember { mutableStateOf(false) }
    LaunchedEffect(hasInit) {
        if (!hasInit.value) {
            list.addAll(curList)
            uiScope.launch(Dispatchers.Main.immediate) {
                listState.scrollToItem(6)
            }
            hasInit.value = true
        }
    }

    val hasAdd = remember { mutableStateOf(false) }
    LaunchedEffect(hasAdd) {
        if (!hasAdd.value) {
            delay(1000)
            list.addAll(0, preList)  //往前插入超过129复用的元素, 会出现数据跳动问题
            // list.addAll(nextList)
            hasAdd.value = true
        }
    }
}
