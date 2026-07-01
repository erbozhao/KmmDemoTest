package com.example.kmmdemotest.demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.kmmdemotest.kibo.KBCacheImage
import com.example.kmmdemotest.kibo.KBCacheText
import kmmdemotest.composeapp.generated.resources.Res
import kmmdemotest.composeapp.generated.resources.cover_icon1
import kmmdemotest.composeapp.generated.resources.cover_icon2
import kmmdemotest.composeapp.generated.resources.cover_icon3
import kmmdemotest.composeapp.generated.resources.cover_icon4
import kmmdemotest.composeapp.generated.resources.cover_icon5
import kmmdemotest.composeapp.generated.resources.cover_icon6
import kmmdemotest.composeapp.generated.resources.cover_icon7
import kmmdemotest.composeapp.generated.resources.cover_icon8
import kmmdemotest.composeapp.generated.resources.cover_icon9
import kmmdemotest.composeapp.generated.resources.novel_half_star
import org.jetbrains.compose.resources.painterResource

/**
 * @author onuszhao
 * @since 2025/10/30
 * @description
 */

@Immutable // 声明数据类是不可变的，帮助 Compose 编译器优化
data class ListItemData(val type: Int, val id: Int)

private val listData = mutableListOf<ListItemData>().apply {
    repeat(200) {
        // add(Pair(if (it < 40) 1 else 2, it))
        add(ListItemData(1, it))
    }
}

@Composable
fun LagPageView() {
    val listState = rememberLazyListState()
    // println("LagPageView")


    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.1f)),
        state = listState,
        // flingBehavior = rememberSnapFlingBehavior(listState, SnapPosition.Start),
        // flingBehavior = rememberCustomSnapFlingBehavior(lazyListState, SnapPosition.Start),
        // overscrollEffect = NoOpOverscrollEffect(),
    ) {
        itemsIndexed(listData, key = { index, item -> "${item.id}" }, contentType = { index, item -> "${item.type}" }) { cIndex, item ->
            // println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss:SSS")}   App  cIndex: $cIndex")
            when (item.type) {
                1 -> {
                    RankingCardView(cIndex)
                }

                2 -> {
                    CommonListItemView(cIndex)
                }
            }
        }
    }
}

val cardListData = mutableListOf<ListItemData>().apply {
    repeat(16) {
        add(ListItemData(5, it + 1000))
    }
}

@Stable
@Composable
fun RankingCardView(carIndex: Int) {

    // println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss:SSS")}  RankingCardView  index: $index")

    // val cardListState = remember(index) { LazyListState() }

    // val cardModifier = remember { Modifier.padding(10.dp).fillMaxWidth().height(100.dp).background(Color.Black.copy(0.4f)) }

    // Column(modifier = cardModifier) {
    //     Text(
    //         text = index.toString(), color = Color.Black.copy(alpha = 0.8f), // 可替换为具体的颜色资源
    //         fontSize = 12.sp, lineHeight = 13.sp, fontWeight = FontWeight.Medium, maxLines = 2, overflow = TextOverflow.Ellipsis
    //     )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        // state = cardListState,
        // contentPadding = PaddingValues(start = 6.dp, top = 0.dp, bottom = 8.dp),
        // flingBehavior = rememberSnapFlingBehavior(lazyListState, SnapPosition.Start),
        // overscrollEffect = NoOpOverscrollEffect()
    ) {
        itemsIndexed(
            items = cardListData,
            key = { index, item -> "${item.id}" },
            contentType = { index, item -> "${item.type}" }) { index, itemData ->
            // RankingItemView(carIndex, index)
            RankingItemView2(carIndex, index)
        }
    }

    // Row(
    //     modifier = Modifier.padding(start = 6.dp, top = 0.dp, bottom = 8.dp).fillMaxWidth().horizontalScroll(rememberScrollState()),
    // ) {
    //     list.forEachIndexed { index, itemData ->
    //         RankingItemView(index)
    //     }
    // }

    // }
}

// @Immutable
@Stable
@Composable
fun RankingItemView(carIndex: Int, itemIndex: Int) {

    // println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss:SSS")}   RankingItemView  index: $index")
    Row(
        modifier = Modifier.width(300.dp).height(94.dp)
    ) {

        Box(
            modifier = Modifier.padding(start = 6.dp, top = 5.dp).size(60.dp, 84.dp).background(Color.Gray)
        ) {
            val resId = when (carIndex % 9) {
                0 -> Res.drawable.cover_icon1
                1 -> Res.drawable.cover_icon2
                2 -> Res.drawable.cover_icon3
                3 -> Res.drawable.cover_icon4
                4 -> Res.drawable.cover_icon5
                5 -> Res.drawable.cover_icon6
                6 -> Res.drawable.cover_icon7
                7 -> Res.drawable.cover_icon8
                8 -> Res.drawable.cover_icon9
                else -> Res.drawable.cover_icon1
            }
            KBCacheImage(
                resource = resId,
                colorFilter = null,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier.padding(start = 2.dp).width(20.dp).heightIn(19.dp), contentAlignment = Alignment.Center

            ) {
                KBCacheImage(
                    modifier = Modifier.widthIn(16.dp).height(19.dp),
                    resource = Res.drawable.novel_half_star,
                    colorFilter = null,
                    contentDescription = "Image"
                )

                KBCacheText(
                    modifier = Modifier.fillMaxSize(),
                    text = "${carIndex + itemIndex}",
                    color = Color.Black.copy(alpha = 0.8f), // 可替换为具体的颜色资源
                    fontSize = 12.sp,
                    lineHeight = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }

        // Right Container
        Column(
            modifier = Modifier.align(Alignment.CenterVertically).fillMaxSize().padding(start = 12.dp)
        ) {
            // Title
            KBCacheText(
                modifier = Modifier.padding(end = 6.dp),
                text = "Title_${carIndex}_${itemIndex}",
                color = Color.Black.copy(0.3f), // 可替换为具体的颜色资源
                fontSize = 13.sp, lineHeight = 13.sp, fontWeight = FontWeight.Medium, maxLines = 2, overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 4.dp)
            ) {
                KBCacheText(
                    text = "Tag_${carIndex}_${itemIndex}", color = Color.Black.copy(0.3f), // 可替换为具体的颜色资源
                    fontSize = 12.sp, modifier = Modifier.padding(end = 2.dp), maxLines = 1, overflow = TextOverflow.Ellipsis
                )

                KBCacheImage(
                    resource = Res.drawable.novel_half_star,
                    colorFilter = null,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp, 14.dp),
                    contentScale = ContentScale.Crop
                )

                KBCacheText(
                    text = "${100 + carIndex + itemIndex} Views", color = Color.Black.copy(0.5f), // 这里你可以替换为自定义颜色
                    fontSize = 11.sp, modifier = Modifier.padding(start = 6.dp, end = 2.dp), maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }

            KBCacheText(
                text = "Rule_${carIndex}_${itemIndex}", color = Color.Black.copy(0.3f), // 这里你可以替换为自定义颜色
                fontSize = 9.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.clip(RoundedCornerShape(4.dp)) // 圆
                    .background(Color.Black.copy(0.3f)).padding(start = 4.dp, top = 2.dp, end = 4.dp, bottom = 2.dp)
            )
        }
    }
}

@Stable
@Composable
fun RankingItemView2(carIndex: Int, itemIndex: Int) {

    // println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss:SSS")}   RankingItemView  index: $index")
    Row(
        modifier = Modifier.width(300.dp).height(94.dp)
    ) {

        Box(
            modifier = Modifier.padding(start = 6.dp, top = 5.dp).size(60.dp, 84.dp)
        ) {
            val resId = when (carIndex % 9) {
                0 -> Res.drawable.cover_icon1
                1 -> Res.drawable.cover_icon2
                2 -> Res.drawable.cover_icon3
                3 -> Res.drawable.cover_icon4
                4 -> Res.drawable.cover_icon5
                5 -> Res.drawable.cover_icon6
                6 -> Res.drawable.cover_icon7
                7 -> Res.drawable.cover_icon8
                8 -> Res.drawable.cover_icon9
                else -> Res.drawable.cover_icon1
            }
            Image(
                painter = painterResource(resId),
                colorFilter = null,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier.padding(start = 2.dp).width(20.dp).heightIn(19.dp), contentAlignment = Alignment.Center

            ) {
                Image(
                    modifier = Modifier.widthIn(16.dp).height(19.dp),
                    painter = painterResource(Res.drawable.novel_half_star),
                    colorFilter = null,
                    contentDescription = "Image"
                )

                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = "${carIndex + itemIndex}",
                    color = Color.Black.copy(alpha = 0.8f), // 可替换为具体的颜色资源
                    fontSize = 12.sp,
                    lineHeight = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }

        // Right Container
        Column(
            modifier = Modifier.align(Alignment.CenterVertically).fillMaxSize().padding(start = 12.dp)
        ) {
            // Title
            Text(
                modifier = Modifier.padding(end = 6.dp), text = "Title_${carIndex}_${itemIndex}", color = Color.Black.copy(0.3f), // 可替换为具体的颜色资源
                fontSize = 13.sp, lineHeight = 13.sp, fontWeight = FontWeight.Medium, maxLines = 2, overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 4.dp)
            ) {
                Text(
                    text = "Tag_${carIndex}_${itemIndex}", color = Color.Black.copy(0.3f), // 可替换为具体的颜色资源
                    fontSize = 12.sp, modifier = Modifier.padding(end = 2.dp), maxLines = 1, overflow = TextOverflow.Ellipsis
                )

                Image(
                    painter = painterResource(Res.drawable.novel_half_star),
                    colorFilter = null,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp, 14.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = "${100 + carIndex + itemIndex} Views", color = Color.Black.copy(0.5f), // 这里你可以替换为自定义颜色
                    fontSize = 11.sp, modifier = Modifier.padding(start = 6.dp, end = 2.dp), maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = "Rule_${carIndex}_${itemIndex}", color = Color.Black.copy(0.3f), // 这里你可以替换为自定义颜色
                fontSize = 9.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.clip(RoundedCornerShape(4.dp)) // 圆
                    .background(Color.Black.copy(0.3f)).padding(start = 4.dp, top = 2.dp, end = 4.dp, bottom = 2.dp)
            )
        }
    }
}

@Composable
fun CommonListItemView(index: Int) {
    // println("CommonListItemView  index: $index")

    val rowModifier = remember {
        Modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .fillMaxWidth()
            .height(114.dp)
//            .clip(RoundedCornerShape(6.dp))
            .background(Color.Red.copy(alpha = 0.2f))
            // .clickable(
            //     interactionSource = null,
            //     indication = ripple(bounded = true, color = LocalAppTheme.current.commonCustomColors["listRippleColor"] ?: Color.Transparent),
            //     onClick = {
            //         onItemClick(itemData, mutableListOf<NovelBasic>().also {
            //             it.add(itemData)
            //         })
            //     }
            // )
            .padding(start = 12.dp, top = 0.dp, end = 12.dp, bottom = 0.dp)
    }

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // AsyncImage(
        //     model = itemData.stInfo?.stCoverImage?.sImageUrl, // 图片的 URL
        //     contentDescription = "Cover Image",
        //     modifier = Modifier
        //         .size(70.dp, 98.dp)
        //         .clip(RoundedCornerShape(6.dp))
        //         .border(0.5.dp, LocalAppTheme.current.commonCustomColors["common_border_color"] ?: Color.Transparent, RoundedCornerShape(6.dp)),
        //     placeholder = ColorPainter(LocalAppTheme.current.commonColors.commonColorD4),
        //     contentScale = ContentScale.Crop
        // )
        Image(
            // painter = painterResource(Res.drawable.cover_icon0),
            painter = ColorPainter(Color.Red),
            colorFilter = null,
            contentDescription = null,
            modifier = Modifier
                .size(70.dp, 98.dp)
            // .clip(RoundedCornerShape(6.dp))
            // .border(0.5.dp, Color.Black.copy(0.2f), RoundedCornerShape(6.dp))
            ,
            contentScale = ContentScale.Crop
        )

        val columModifier = remember {
            Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(start = 12.dp)
        }

        // Right Container
        Column(
            modifier = columModifier
        ) {
            // Title
            Text(
                text = "Test title $index",
                color = Color.Black, // 可替换为具体的颜色资源
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Description
            Text(
                text = "Test desc long text!!!!!!!!!!!!!!!!!!!!!!!!$index",
                color = Color.Black.copy(alpha = 0.3f), // 可替换为具体的颜色资源
                fontSize = 13.sp,
                maxLines = 2,
                lineHeight = 15.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 4.dp)
            ) {
                Text(
                    text = "4.5",
                    color = Color.Black.copy(alpha = 0.5f),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(end = 2.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Image(
                    // painter = painterResource(Res.drawable.novel_half_star),
                    painter = ColorPainter(Color.Blue),
                    colorFilter = ColorFilter.tint(Color.Yellow),
                    contentDescription = null,
                    modifier = Modifier
                        .size(13.dp, 14.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = "277K Views",
                    color = Color.Gray.copy(alpha = 0.5f), // 这里你可以替换为自定义颜色
                    fontSize = 11.sp,
                    modifier = Modifier
                        .padding(start = 6.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            val tags = listOf<String>("baby", "ruthless")
            if (!tags.isNullOrEmpty()) {
                val textModifier = remember {
                    Modifier
                        .padding(end = 6.dp)
                        .clip(RoundedCornerShape(4.dp)) // 圆
                        .background(Color.Gray.copy(alpha = 0.5f))
                        .padding(start = 4.dp, top = 2.dp, end = 4.dp, bottom = 2.dp)
                }
                Row(modifier = Modifier) {
                    tags.forEachIndexed { index, s ->
                        Text(
                            text = tags.getOrNull(index) ?: "",
                            color = Color.Gray.copy(alpha = 0.5f), // 这里你可以替换为自定义颜色
                            fontSize = 9.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = textModifier
                        )
                    }
                }
            }
        }
    }
}