package com.example.fruitties.android.ui.circle

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.fruitties.android.R
import com.example.fruitties.android.ui.AppTheme

private val HeaderGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFE0CFFC), Color(0xFFFFD1F0)),
)

private val TabTextColor = Color(0xFF8B8BA7)
private val TabSelectedColor = Color(0xFF1F1F3A)

private val postImageColors = listOf(
    Color(0xFFFF9ECE),
    Color(0xFF8EB9FF),
    Color(0xFFD8A8FF),
    Color(0xFF81C784),
    Color(0xFFFFB74D),
    Color(0xFF4DD0E1),
    Color(0xFFBA68C8),
    Color(0xFF64B5F6),
    Color(0xFFAED581),
)

private val topicColors = listOf(
    Color(0xFFFF9ECE),
    Color(0xFF8EB9FF),
    Color(0xFFD8A8FF),
    Color(0xFF81C784),
)

private data class MediaItem(
    val url: String,
    val type: MediaType,
)

enum class MediaType {
    IMAGE, VIDEO
}

private data class CirclePost(
    val id: String,
    val username: String,
    val avatar: String,
    val createTime: Long,
    val content: String,
    val medias: List<MediaItem>,
    val comments: Int,
    val isLiked: Boolean,
    val likeCount: Int,
)

private data class TopicItem(
    val title: String,
    val tag: String = "",
    val hasImage: Boolean = false,
)

private val sampleTopics = listOf(
    TopicItem("游戏小菜鸟", "考官"),
    TopicItem("组队开黑", "管理"),
    TopicItem("游戏大神", hasImage = true),
    TopicItem("好好生活", hasImage = true),
)

private fun getTimeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    val minutes = diff / (1000 * 60)
    if (minutes < 60) {
        return "${minutes}分钟前"
    }

    val hours = minutes / 60
    if (hours < 24) {
        return "${hours}小时前"
    }

    val days = hours / 24
    if (days < 30) {
        return "${days}天前"
    }

    val months = days / 30
    if (months < 12) {
        return "${months}月前"
    }

    return "${months / 12}年前"
}

private val samplePosts = listOf(
    CirclePost(
        id = "1",
        username = "神秘大佬六 哈哈",
        avatar = "https://api.dicebear.com/7.x/adventurer/svg?seed=boss&backgroundColor=ffd5dc",
        createTime = System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000,
        content = "",
        medias = listOf(
            MediaItem("https://images.unsplash.com/photo-1551009175-8a69b48d8203?w=800", MediaType.IMAGE),
        ),
        comments = 0,
        isLiked = false,
        likeCount = 0,
    ),
    CirclePost(
        id = "2",
        username = "秘境开拓者A",
        avatar = "https://api.dicebear.com/7.x/adventurer/svg?seed=explorer&backgroundColor=ffe0b2",
        createTime = System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000,
        content = "123",
        medias = listOf(
            MediaItem("https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=800", MediaType.IMAGE),
        ),
        comments = 0,
        isLiked = false,
        likeCount = 0,
    ),
    CirclePost(
        id = "3",
        username = "zhaojian1",
        avatar = "https://api.dicebear.com/7.x/adventurer/svg?seed=zhao&backgroundColor=c8e6c9",
        createTime = System.currentTimeMillis() - 90L * 24 * 60 * 60 * 1000,
        content = "Hhhhhhhlllll",
        medias = listOf(
            MediaItem("https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=800", MediaType.IMAGE),
        ),
        comments = 2,
        isLiked = false,
        likeCount = 0,
    ),
    CirclePost(
        id = "4",
        username = "幻界星语者9PavR",
        avatar = "https://api.dicebear.com/7.x/adventurer/svg?seed=star&backgroundColor=bbdefb",
        createTime = System.currentTimeMillis() - 90L * 24 * 60 * 60 * 1000,
        content = "哈哈哈哈`(*∩_∩*)′",
        medias = listOf(
            MediaItem("https://images.unsplash.com/photo-1485846234645-a62644f84728?w=800", MediaType.VIDEO),
        ),
        comments = 0,
        isLiked = false,
        likeCount = 0,
    ),
    CirclePost(
        id = "5",
        username = "游戏领航官khWfY",
        avatar = "https://api.dicebear.com/7.x/adventurer/svg?seed=game&backgroundColor=e1bee7",
        createTime = System.currentTimeMillis() - 90L * 24 * 60 * 60 * 1000,
        content = "测试三张照片",
        medias = listOf(
            MediaItem("https://images.unsplash.com/photo-1490750967868-88aa4486c946?w=400", MediaType.IMAGE),
            MediaItem("https://images.unsplash.com/photo-1518770660439-4636190af475?w=400", MediaType.IMAGE),
            MediaItem("https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=400", MediaType.IMAGE),
        ),
        comments = 0,
        isLiked = false,
        likeCount = 0,
    ),
    CirclePost(
        id = "6",
        username = "游戏领航官khWfY",
        avatar = "https://api.dicebear.com/7.x/adventurer/svg?seed=game2&backgroundColor=e1bee7",
        createTime = System.currentTimeMillis() - 90L * 24 * 60 * 60 * 1000,
        content = "Hvv",
        medias = listOf(
            MediaItem("https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=400", MediaType.IMAGE),
            MediaItem("https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=400", MediaType.IMAGE),
        ),
        comments = 0,
        isLiked = false,
        likeCount = 0,
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircleScreen(
    onPostClick: (String) -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(1) }
    var showImagePreview by remember { mutableStateOf(false) }
    var previewImageUrl by remember { mutableStateOf("") }
    var showPostDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        listOf("关注", "最新", "最热").forEachIndexed { index, title ->
                            Text(
                                text = title,
                                fontSize = 16.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) TabSelectedColor else TabTextColor,
                                modifier = Modifier.clickable { selectedTab = index },
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "通知",
                        modifier = Modifier.size(24.dp),
                        tint = Color(0xFF1F1F3A),
                    )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFFD946EF), Color(0xFFEC4899)),
                        )
                    )
                    .clickable { showPostDialog = true }
                    .padding(16.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 4.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "发动态",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White,
                    )
                    Text(
                        text = "发动态",
                        fontSize = 10.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        },
        containerColor = Color(0xFFF6F5FA),
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp),
        ) {
            item {
                HotTopicsSection()
            }

            items(samplePosts) { post ->
                CirclePostItem(
                    post = post,
                    onClick = { onPostClick(post.id) },
                    onImageClick = { url ->
                        previewImageUrl = url
                        showImagePreview = true
                    },
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        if (showImagePreview) {
            ImagePreviewDialog(
                imageUrl = previewImageUrl,
                onClose = { showImagePreview = false },
            )
        }

        if (showPostDialog) {
            PostDialog(
                onClose = { showPostDialog = false },
                onPost = { showPostDialog = false },
            )
        }
    }
}

@Composable
private fun HotTopicsSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFFD946EF),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "热门话题",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F1F3A),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                sampleTopics.take(2).forEach { topic ->
                    TopicChip(topic = topic)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                sampleTopics.drop(2).forEach { topic ->
                    TopicChip(topic = topic)
                }
            }
        }
    }
}

@Composable
private fun TopicChip(topic: TopicItem) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF6F5FA))
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { },
    ) {
        if (topic.tag.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        if (topic.tag == "考官") Color(0xFFFF9ECE)
                        else Color(0xFFFFB74D),
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
                Text(
                    text = topic.tag,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
        }
        if (topic.hasImage) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = Color(0xFF8B8BA7),
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = "#${topic.title}",
            fontSize = 13.sp,
            color = Color(0xFF6366F1),
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun CirclePostItem(
    post: CirclePost,
    onClick: () -> Unit,
    onImageClick: (String) -> Unit,
) {
    var isLiked by remember(post) { mutableStateOf(post.isLiked) }
    var likeCount by remember(post) { mutableIntStateOf(post.likeCount) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = post.avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = post.username,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F1F3A),
                )
                Text(
                    text = "发布于${getTimeAgo(post.createTime)}",
                    fontSize = 13.sp,
                    color = Color(0xFF8B8BA7),
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF6366F1), Color(0xFFD946EF)),
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable { },
            ) {
                Text(
                    text = "Hi",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }

            IconButton(
                onClick = { },
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color(0xFF8B8BA7),
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                if (post.content.isNotEmpty()) {
                    Text(
                        text = post.content,
                        fontSize = 15.sp,
                        color = Color(0xFF1F1F3A),
                        lineHeight = 22.sp,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (post.medias.isNotEmpty()) {
                    MediaGrid(
                        medias = post.medias,
                        onImageClick = onImageClick,
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    PostActionItem(
                        icon = Icons.Default.Share,
                        label = "分享",
                        onClick = { },
                    )
                    PostActionItem(
                        icon = Icons.AutoMirrored.Outlined.Comment,
                        label = if (post.comments > 0) "评论 ${post.comments}" else "评论",
                        onClick = { },
                    )
                    PostActionItem(
                        icon = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        label = "点赞",
                        tint = if (isLiked) Color(0xFFFF6B6B) else Color(0xFF8B8BA7),
                        onClick = {
                            isLiked = !isLiked
                            likeCount += if (isLiked) 1 else -1
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun MediaGrid(
    medias: List<MediaItem>,
    onImageClick: (String) -> Unit,
) {
    val count = medias.size
    when {
        count == 1 -> {
            SingleMediaItem(
                media = medias[0],
                onClick = onImageClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        count == 2 -> {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                medias.forEach { media ->
                    SingleMediaItem(
                        media = media,
                        onClick = onImageClick,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
        count == 4 -> {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(2) { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        repeat(2) { col ->
                            val index = row * 2 + col
                            SingleMediaItem(
                                media = medias[index],
                                onClick = onImageClick,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
        }
        else -> {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val rows = (count + 2) / 3
                for (row in 0 until rows) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (col in 0 until 3) {
                            val index = row * 3 + col
                            if (index < count) {
                                SingleMediaItem(
                                    media = medias[index],
                                    onClick = onImageClick,
                                    modifier = Modifier.weight(1f),
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SingleMediaItem(
    media: MediaItem,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                if (media.type == MediaType.IMAGE) {
                    onClick(media.url)
                } else {
                }
            },
    ) {
        AsyncImage(
            model = media.url,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        if (media.type == MediaType.VIDEO) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.PlayCircle,
                    contentDescription = "播放视频",
                    modifier = Modifier.size(60.dp),
                    tint = Color.White.copy(alpha = 0.9f),
                )
            }
        }
    }
}

@Composable
private fun PostActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    tint: Color = Color(0xFF8B8BA7),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = onClick),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = tint,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            color = tint,
        )
    }
}

@Composable
private fun ImagePreviewDialog(
    imageUrl: String,
    onClose: () -> Unit,
) {
    Dialog(onDismissRequest = onClose) {
        var scale by remember { mutableFloatStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 5f)
                        offset += pan
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.8f)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y,
                    ),
                contentScale = ContentScale.Fit,
            )

            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "关闭",
                    modifier = Modifier.size(28.dp),
                    tint = Color.White,
                )
            }
        }
    }
}

@Composable
private fun PostDialog(
    onClose: () -> Unit,
    onPost: () -> Unit,
) {
    Dialog(onDismissRequest = onClose) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "发动态",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F1F3A),
                    )
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(100.dp)
                        .background(Color(0xFFF6F5FA), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                ) {
                    Text(
                        text = "分享你的新鲜事...",
                        fontSize = 15.sp,
                        color = Color(0xFF8B8BA7),
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        PostOption(
                            icon = Icons.Default.Image,
                            label = "图片",
                            onClick = { },
                        )
                        PostOption(
                            icon = Icons.Default.VideoLibrary,
                            label = "视频",
                            onClick = { },
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF6366F1), Color(0xFFD946EF)),
                                )
                            )
                            .padding(horizontal = 24.dp, vertical = 10.dp)
                            .clickable(onClick = onPost),
                    ) {
                        Text(
                            text = "发布",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PostOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = Color(0xFF6366F1),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF8B8BA7),
        )
    }
}

@Preview
@Composable
private fun CircleScreenPreview() {
    AppTheme() {
        CircleScreen(onPostClick = {})
    }
}
