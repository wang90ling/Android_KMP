package com.example.fruitties.android.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.fruitties.android.R
import com.example.fruitties.android.ui.FruittiesTheme
import com.example.fruitties.android.ui.circle.CircleScreen
import com.example.fruitties.android.ui.im.MessageScreen
import com.example.fruitties.android.ui.mine.MineScreen
import kotlinx.coroutines.launch

private val topTabTitles = listOf("推荐", "派单厅", "热门", "交友", "点唱")
private val bottomTabTitles = listOf("推荐主播", "心动房间", "休闲天地")

private data class FeatureCard(
    val title: String,
    val subtitle: String,
    val gradientColors: List<Color>,
)

private val featureCards = listOf(
    FeatureCard("去交友", "遇见美好的TA", listOf(Color(0xFFD946EF), Color(0xFFDB2777))),
    FeatureCard("NBTI测试", "挖掘潜在人格", listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))),
    FeatureCard("去开黑", "默契队友在这里", listOf(Color(0xFF7C3AED), Color(0xFF4C1D95))),
    FeatureCard("去扩列", "遇见有趣的人", listOf(Color(0xFFD97706), Color(0xFFEA580C))),
)

private data class Anchor(
    val name: String,
    val avatar: String,
    val isLive: Boolean,
    val viewerCount: String,
)

private val anchors = listOf(
    Anchor("小红", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cute%20anime%20boy%20avatar%20red%20hair%203d%20style&image_size=square", true, "9"),
    Anchor("额威威", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cute%20anime%20boy%20avatar%20blonde%20hair%203d%20style&image_size=square", true, "6"),
    Anchor("测试测试测试测试测...", "", false, ""),
    Anchor("乐游集结号割发...", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cute%20anime%20boy%20avatar%20blonde%20hair%20cool%20style&image_size=square", true, "6"),
)

private data class Room(
    val name: String,
    val cover: String,
    val hostName: String,
    val memberCount: Int,
)

private val rooms = listOf(
    Room("甜蜜小屋", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cozy%20romantic%20room%20pink%20lighting%20anime%20style&image_size=square", "小美", 12),
    Room("游戏开黑房", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=gaming%20room%20neon%20lights%20dark%20blue&image_size=square", "大神", 8),
    Room("音乐派对", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=music%20party%20room%20colorful%20lights&image_size=square", "DJ小王", 25),
    Room("深夜聊天室", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=late%20night%20chat%20room%20warm%20lighting&image_size=square", "夜猫子", 18),
)

private data class Activity(
    val name: String,
    val icon: String,
    val participants: Int,
)

private val activities = listOf(
    Activity("真心话大冒险", "", 520),
    Activity("谁是卧底", "", 380),
    Activity("你画我猜", "", 290),
    Activity("狼人杀", "", 450),
    Activity("K歌之王", "", 320),
    Activity("剧本杀", "", 180),
)

// 派单厅任务数据
private data class DispatchTask(
    val title: String,
    val type: String,
    val price: String,
    val status: String,
    val avatar: String,
    val publisher: String,
)

private val dispatchTasks = listOf(
    DispatchTask("陪玩游戏·王者荣耀", "游戏陪玩", "¥30/局", "待接单", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=gamer%20avatar%20cool%20headset&image_size=square", "小甜甜"),
    DispatchTask("语音聊天·情感倾诉", "语音聊天", "¥50/小时", "待接单", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=sweet%20girl%20avatar%20headphones&image_size=square", "暖心小姐姐"),
    DispatchTask("唱歌点播·流行金曲", "才艺点唱", "¥20/首", "待接单", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=singer%20avatar%20microphone%20stage&image_size=square", "音乐小王子"),
    DispatchTask("开黑上分·和平精英", "游戏陪玩", "¥25/局", "进行中", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=pro%20gamer%20avatar%20military%20helmet&image_size=square", "吃鸡大神"),
    DispatchTask("深夜电台·故事分享", "语音聊天", "¥40/小时", "待接单", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=radio%20host%20avatar%20night%20warm&image_size=square", "夜语者"),
    DispatchTask("剧本杀·多人组局", "游戏陪玩", "¥35/场", "待接单", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=mystery%20detective%20avatar%20magnifier&image_size=square", "推理大师"),
)

// 热门排行榜数据
private data class HotItem(
    val rank: Int,
    val name: String,
    val avatar: String,
    val hotValue: String,
    val tag: String,
)

private val hotItems = listOf(
    HotItem(1, "倾城小仙女", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=beautiful%20fairy%20girl%20avatar%20sparkle&image_size=square", "128.5w", "才艺主播"),
    HotItem(2, "深夜歌手", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=male%20singer%20avatar%20microphone%20cool&image_size=square", "96.3w", "音乐才子"),
    HotItem(3, "游戏女王", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=female%20gamer%20avatar%20neon%20glasses&image_size=square", "85.7w", "游戏陪玩"),
    HotItem(4, "暖心大叔", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=warm%20uncle%20avatar%20smile%20gentle&image_size=square", "72.1w", "情感电台"),
    HotItem(5, "甜美小可爱", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cute%20sweet%20girl%20avatar%20pink&image_size=square", "68.9w", "舞蹈主播"),
    HotItem(6, "摇滚少年", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=rock%20boy%20avatar%20guitar%20cool&image_size=square", "55.4w", "音乐才子"),
)

// 交友用户数据
private data class DatingUser(
    val name: String,
    val avatar: String,
    val age: Int,
    val gender: String,
    val city: String,
    val signature: String,
    val isOnline: Boolean,
)

private val datingUsers = listOf(
    DatingUser("柠檬不萌", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cute%20girl%20avatar%20yellow%20lemon%20theme&image_size=square", 22, "女", "北京", "想遇见有趣的你", true),
    DatingUser("星辰大海", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=handsome%20boy%20avatar%20star%20galaxy%20theme&image_size=square", 25, "男", "上海", "寻找那个对的人", true),
    DatingUser("温柔岁月", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=elegant%20woman%20avatar%20soft%20warm%20light&image_size=square", 24, "女", "广州", "愿得一人心", false),
    DatingUser("清风明月", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=gentle%20man%20avatar%20moon%20breeze%20theme&image_size=square", 27, "男", "深圳", "等风也等你", true),
    DatingUser("糖果女孩", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=sweet%20candy%20girl%20avatar%20colorful&image_size=square", 21, "女", "杭州", "甜甜的恋爱轮到我了吗", true),
    DatingUser("孤独患者", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=lonely%20boy%20avatar%20dark%20moody%20style&image_size=square", 26, "男", "成都", "想找个人说说话", false),
)

// 点唱歌曲数据
private data class Song(
    val name: String,
    val singer: String,
    val cover: String,
    val orderCount: Int,
    val duration: String,
)

private val songs = listOf(
    Song("晴天", "周杰伦", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=music%20album%20cover%20sunny%20day%20guitar&image_size=square", 1280, "4:29"),
    Song("起风了", "买辣椒也用券", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=music%20album%20cover%20wind%20breeze%20sky&image_size=square", 980, "5:25"),
    Song("孤勇者", "陈奕迅", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=music%20album%20cover%20lonely%20hero%20dark&image_size=square", 1560, "4:13"),
    Song("稻香", "周杰伦", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=music%20album%20cover%20rice%20field%20countryside&image_size=square", 890, "3:43"),
    Song("告白气球", "周杰伦", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=music%20album%20cover%20balloon%20romantic%20pink&image_size=square", 1120, "3:35"),
    Song("年少有为", "李荣浩", "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=music%20album%20cover%20young%20ambition%20city&image_size=square", 750, "4:51"),
)

enum class BottomNavItem(val index: Int) {
    HOME(0),
    CIRCLE(1),
    MESSAGE(2),
    MINE(3),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onCartClick: () -> Unit,
    onProductClick: (String) -> Unit,
    onTabClick: (BottomNavItem) -> Unit = {},
) {
    var selectedBottomNavIndex by remember { mutableIntStateOf(0) }
    var unreadMessageCount by remember { mutableIntStateOf(99) }

    val onTabSelected: (Int) -> Unit = { index ->
        selectedBottomNavIndex = index
        onTabClick(BottomNavItem.entries[index])
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1F1F3A),
                tonalElevation = 0.dp,
            ) {
                NavigationBarItem(
                    selected = selectedBottomNavIndex == 0,
                    onClick = { onTabSelected(0) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = stringResource(R.string.home),
                            tint = if (selectedBottomNavIndex == 0) Color(0xFFE879F9) else Color(0xFF8B8BA7),
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.home),
                            fontSize = 12.sp,
                            color = if (selectedBottomNavIndex == 0) Color(0xFFE879F9) else Color(0xFF8B8BA7),
                        )
                    },
                )
                NavigationBarItem(
                    selected = selectedBottomNavIndex == 1,
                    onClick = { onTabSelected(1) },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Group,
                            contentDescription = stringResource(R.string.categories),
                            tint = if (selectedBottomNavIndex == 1) Color(0xFFE879F9) else Color(0xFF8B8BA7),
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.categories),
                            fontSize = 12.sp,
                            color = if (selectedBottomNavIndex == 1) Color(0xFFE879F9) else Color(0xFF8B8BA7),
                        )
                    },
                )
                NavigationBarItem(
                    selected = selectedBottomNavIndex == 2,
                    onClick = { onTabSelected(2) },
                    icon = {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Color(0xFFFF6B6B),
                                    contentColor = Color.White,
                                ) {
                                    Text(
                                        text = if (unreadMessageCount > 99) "99+" else unreadMessageCount.toString(),
                                        fontSize = 10.sp,
                                    )
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Chat,
                                contentDescription = stringResource(R.string.chat),
                                tint = if (selectedBottomNavIndex == 2) Color(0xFFE879F9) else Color(0xFF8B8BA7),
                            )
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.chat),
                            fontSize = 12.sp,
                            color = if (selectedBottomNavIndex == 2) Color(0xFFE879F9) else Color(0xFF8B8BA7),
                        )
                    },
                )
                NavigationBarItem(
                    selected = selectedBottomNavIndex == 3,
                    onClick = { onTabSelected(3) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(R.string.mine),
                            tint = if (selectedBottomNavIndex == 3) Color(0xFFE879F9) else Color(0xFF8B8BA7),
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.mine),
                            fontSize = 12.sp,
                            color = if (selectedBottomNavIndex == 3) Color(0xFFE879F9) else Color(0xFF8B8BA7),
                        )
                    },
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
        ),
        containerColor = Color(0xFF0F0F23),
    ) { paddingValues ->
        when (selectedBottomNavIndex) {
            0 -> HomeContent(paddingValues = paddingValues)
            1 -> CircleScreen { }
            2 -> MessageScreen() { }
            3 -> MineScreen() { }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(paddingValues: PaddingValues) {
    val pagerState = rememberPagerState(initialPage = 0) { topTabTitles.size }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(top = 20.dp)) {
        TopTabRow(
            selectedTabIndex = pagerState.currentPage,
            onTabClick = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { pageIndex ->
            when (pageIndex) {
                0 -> RecommendTabContent()
                1 -> DispatchTabContent()
                2 -> HotTabContent()
                3 -> DatingTabContent()
                4 -> SingTabContent()
            }
        }

        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
    }
}

@Composable
private fun TopTabRow(
    selectedTabIndex: Int,
    onTabClick: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        topTabTitles.forEachIndexed { index, title ->
            Column(
                modifier = Modifier
                    .clickable { onTabClick(index) }
                    .padding(vertical = 10.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                    color = if (selectedTabIndex == index) Color(0xFFE879F9) else Color(0xFF8B8BA7),
                )
                if (selectedTabIndex == index) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(2.dp)
                            .background(Color(0xFFE879F9), RoundedCornerShape(2.dp)),
                    )
                }else{
                    Spacer(modifier = Modifier.height(5.dp))
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(2.dp)
                            .background(Color.Transparent)
                    )
                }
            }
        }
    }
}

@Composable
private fun RecommendTabContent() {
    var selectedBottomTabIndex by remember { mutableIntStateOf(0) }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            FeatureCardsSection()
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SecondaryTabRow(
                    selectedTabIndex = selectedBottomTabIndex,
                    containerColor = Color.Transparent,
                    divider = {},
                    modifier = Modifier.weight(1f),
                ) {
                    bottomTabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedBottomTabIndex == index,
                            onClick = { selectedBottomTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 15.sp,
                                    fontWeight = if (selectedBottomTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedBottomTabIndex == index) Color.White else Color(0xFF8B8BA7),
                                )
                            },
                        )
                    }
                }
                Text(
                    text = "筛选",
                    fontSize = 13.sp,
                    color = Color(0xFF8B8BA7),
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clickable { },
                )
            }
        }
        when (selectedBottomTabIndex) {
            0 -> items(anchors.chunked(2)) { pair ->
                AnchorGridRow(items = pair)
            }
            1 -> items(rooms.chunked(2)) { pair ->
                RoomGridRow(items = pair)
            }
            2 -> items(activities.chunked(2)) { pair ->
                ActivityGridRow(items = pair)
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DispatchTabContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(dispatchTasks) { task ->
            DispatchTaskCard(task = task)
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DispatchTaskCard(task: DispatchTask) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F1F3A),
        ),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF374151)),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = task.avatar,
                    contentDescription = task.publisher,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE879F9).copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                    ) {
                        Text(
                            text = task.type,
                            fontSize = 11.sp,
                            color = Color(0xFFE879F9),
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "发布者: ${task.publisher}",
                        fontSize = 12.sp,
                        color = Color(0xFF8B8BA7),
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = task.price,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE879F9),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .background(
                            if (task.status == "待接单") Color(0xFFD97706) else Color(0xFF10B981),
                            RoundedCornerShape(10.dp),
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                ) {
                    Text(
                        text = task.status,
                        fontSize = 11.sp,
                        color = Color.White,
                    )
                }
            }
        }
    }
}

@Composable
private fun HotTabContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(hotItems) { item ->
            HotRankCard(item = item)
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HotRankCard(item: HotItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F1F3A),
        ),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        when (item.rank) {
                            1 -> Color(0xFFEF4444)
                            2 -> Color(0xFFF59E0B)
                            3 -> Color(0xFFEAB308)
                            else -> Color(0xFF374151)
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = item.rank.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (item.rank <= 3) Color.White else Color(0xFF8B8BA7),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF374151)),
            ) {
                AsyncImage(
                    model = item.avatar,
                    contentDescription = item.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .background(Color(0xFFE879F9).copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                ) {
                    Text(
                        text = item.tag,
                        fontSize = 11.sp,
                        color = Color(0xFFE879F9),
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = item.hotValue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF4444),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "热度",
                    fontSize = 11.sp,
                    color = Color(0xFF8B8BA7),
                )
            }
        }
    }
}

@Composable
private fun DatingTabContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(datingUsers) { user ->
            DatingUserCard(user = user)
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DatingUserCard(user: DatingUser) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F1F3A),
        ),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF374151)),
            ) {
                AsyncImage(
                    model = user.avatar,
                    contentDescription = user.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
                if (user.isOnline) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981))
                            .padding(2.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(if (user.gender == "女") Color(0xFFEC4899) else Color(0xFF3B82F6)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = user.gender,
                            fontSize = 11.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${user.age}岁",
                        fontSize = 12.sp,
                        color = Color(0xFF8B8BA7),
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user.city,
                        fontSize = 12.sp,
                        color = Color(0xFF8B8BA7),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (user.isOnline) "在线" else "离线",
                        fontSize = 12.sp,
                        color = if (user.isOnline) Color(0xFF10B981) else Color(0xFF8B8BA7),
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = user.signature,
                    fontSize = 12.sp,
                    color = Color(0xFF8B8BA7),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Box(
                modifier = Modifier
                    .background(Color(0xFFE879F9), RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { },
            ) {
                Text(
                    text = "关注",
                    fontSize = 13.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
private fun SingTabContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(songs) { song ->
            SongCard(song = song)
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SongCard(song: Song) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F1F3A),
        ),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF374151)),
            ) {
                AsyncImage(
                    model = song.cover,
                    contentDescription = song.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = song.singer,
                    fontSize = 13.sp,
                    color = Color(0xFF8B8BA7),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "已点唱 ${song.orderCount}次",
                        fontSize = 11.sp,
                        color = Color(0xFF8B8BA7),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = song.duration,
                        fontSize = 11.sp,
                        color = Color(0xFF8B8BA7),
                    )
                }
            }
            Box(
                modifier = Modifier
                    .background(Color(0xFFE879F9), RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { },
            ) {
                Text(
                    text = "点唱",
                    fontSize = 13.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
private fun FeatureCardsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = {
            featureCards.take(2).forEach { card ->
                FeatureCardItem(
                    card = card,
                    modifier = Modifier.weight(1f),
                )
            }
        },
    )
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = {
            featureCards.drop(2).forEach { card ->
                FeatureCardItem(
                    card = card,
                    modifier = Modifier.weight(1f),
                )
            }
        },
    )
}

@Composable
private fun FeatureCardItem(
    card: FeatureCard,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.verticalGradient(card.gradientColors)),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = card.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = card.subtitle,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f),
                )
            }
        }
    }
}



@Composable
private fun AnchorCard(
    anchor: Anchor,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F1F3A),
        ),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (anchor.avatar.isNotEmpty()) {
                AsyncImage(
                    model = anchor.avatar,
                    contentDescription = anchor.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF374151)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = anchor.name.take(1),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }

            if (anchor.isLive) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp)
                        .background(Color(0xFFEF4444), RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                ) {
                    Text(
                        text = "推荐主播",
                        fontSize = 10.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                        ),
                    )
                    .padding(12.dp),
            ) {
                Column {
                    Text(
                        text = anchor.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (anchor.isLive) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Live",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(12.dp),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "小帅 ${anchor.viewerCount}",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RoomCard(
    room: Room,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F1F3A),
        ),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (room.cover.isNotEmpty()) {
                AsyncImage(
                    model = room.cover,
                    contentDescription = room.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF374151)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = room.name.take(1),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                        ),
                    )
                    .padding(12.dp),
            ) {
                Column {
                    Text(
                        text = room.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${room.hostName} · ${room.memberCount}人",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f),
                    )
                }
            }
        }
    }
}

@Composable
private fun AnchorGridRow(items: List<Anchor>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items.forEach { anchor ->
            AnchorCard(anchor = anchor, modifier = Modifier.weight(1f))
        }
        if (items.size == 1) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun RoomGridRow(items: List<Room>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items.forEach { room ->
            RoomCard(room = room, modifier = Modifier.weight(1f))
        }
        if (items.size == 1) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun ActivityGridRow(items: List<Activity>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items.forEach { activity ->
            ActivityCard(activity = activity, modifier = Modifier.weight(1f))
        }
        if (items.size == 1) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun ActivityCard(
    activity: Activity,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F1F3A),
        ),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF374151)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = activity.name.take(1),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE879F9),
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = activity.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${activity.participants}人在玩",
                    fontSize = 12.sp,
                    color = Color(0xFF8B8BA7),
                )
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    FruittiesTheme(darkTheme = true) {
        MainScreen(
            onCartClick = {},
            onProductClick = {},
        )
    }
}