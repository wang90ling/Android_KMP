package com.example.fruitties.android.ui.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.Headphones
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fruitties.android.R
import com.example.fruitties.android.ui.AppTheme

private val HeaderGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFE0CFFC), Color(0xFFFFD1F0)),
)

private val PageBg = Color(0xFFF6F5FA)
private val TextPrimary = Color(0xFF1F1F3A)
private val TextSecondary = Color(0xFF8B8BA7)
private val TextLight = Color(0xFFFFFFFF)

private val OrderIconBg = Brush.verticalGradient(
    colors = listOf(Color(0xFFFF9ECE), Color(0xFFFF6BA6)),
)
private val CouponIconBg = Brush.verticalGradient(
    colors = listOf(Color(0xFF8EB9FF), Color(0xFF5E8CFF)),
)
private val WalletIconBg = Brush.verticalGradient(
    colors = listOf(Color(0xFFD8A8FF), Color(0xFFB26DFF)),
)

private data class ServiceItem(
    val title: String,
    val icon: ImageVector,
    val iconColor: Color,
)

private val moreServiceItems = listOf(
    listOf(
        ServiceItem("装扮中心", Icons.Default.Diamond, Color(0xFF6366F1)),
        ServiceItem("我的房间", Icons.Default.Home, Color(0xFFEC4899)),
        ServiceItem("搭子入驻", Icons.Default.PersonAdd, Color(0xFF6366F1)),
        ServiceItem("贵族特权", Icons.Default.EmojiEvents, Color(0xFFF97316)),
    ),
    listOf(
        ServiceItem("守护团", Icons.Default.Shield, Color(0xFFEC4899)),
        ServiceItem("我看过的", Icons.Default.History, Color(0xFF6366F1)),
    ),
)

private val supportItems = listOf(
    listOf(
        ServiceItem("联系客服", Icons.Outlined.Headphones, Color(0xFF6B7280)),
        ServiceItem("举报记录", Icons.Default.Flag, Color(0xFF6B7280)),
        ServiceItem("帮助中心", Icons.Default.Info, Color(0xFF6B7280)),
        ServiceItem("设置", Icons.Default.Settings, Color(0xFF6B7280)),
    ),
    listOf(
        ServiceItem("专属客服", Icons.Outlined.SupportAgent, Color(0xFF6B7280)),
    ),
)

private data class StatItem(
    val value: String,
    val label: String,
)

private val profileStats = listOf(
    StatItem("2", "关注"),
    StatItem("0", "粉丝"),
    StatItem("1", "最近访问"),
)

@Composable
fun MineScreen(
    onMenuClick: (String) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize().background(PageBg)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                ProfileHeader(onMenuClick = onMenuClick)
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
                QuickActionsRow(onMenuClick = onMenuClick)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                ServiceSection(
                    title = "更多服务",
                    rows = moreServiceItems,
                    onItemClick = onMenuClick,
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                ServiceSection(
                    title = "支持与帮助",
                    rows = supportItems,
                    onItemClick = onMenuClick,
                )
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
                Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }
}

@Composable
private fun ProfileHeader(onMenuClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(HeaderGradient)
            .padding(horizontal = 20.dp)
            .padding(top = 56.dp, bottom = 40.dp),
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0D5FF)),
                    contentAlignment = Alignment.Center,
                ) {
                    AsyncImage(
                        model = "https://api.dicebear.com/7.x/adventurer/svg?seed=ling&backgroundColor=e0d5ff",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "时空探险家WL",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F1F3A),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "ID:1530878",
                            fontSize = 14.sp,
                            color = Color(0xFF6B6B85),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF6B6B85),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF22D3EE), Color(0xFF06B6D4)),
                                    ),
                                )
                                .padding(horizontal = 10.dp, vertical = 3.dp),
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = Color.White,
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Lv.0",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.clickable { onMenuClick("profile") },
                ) {
                    Text(
                        text = "个人主页",
                        fontSize = 14.sp,
                        color = Color(0xFF6B6B85),
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                profileStats.forEach { stat ->
                    ProfileStat(
                        value = stat.value,
                        label = stat.label,
                        onClick = { onMenuClick(stat.label) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileStat(
    value: String,
    label: String,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick),
    ) {
        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F1F3A),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color(0xFF6B6B85),
        )
    }
}

@Composable
private fun QuickActionsRow(onMenuClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            QuickActionItem(
                title = "订单",
                iconBrush = OrderIconBg,
                iconRes = R.drawable.ic_order,
                onClick = { onMenuClick("订单") },
            )
            QuickActionItem(
                title = "优惠券",
                iconBrush = CouponIconBg,
                iconRes = R.drawable.ic_order,
                onClick = { onMenuClick("优惠券") },
            )
            QuickActionItem(
                title = "钱包",
                iconBrush = WalletIconBg,
                iconRes = R.drawable.ic_order,
                onClick = { onMenuClick("钱包") },
            )
        }
    }
}

@Composable
private fun QuickActionItem(
    title: String,
    iconBrush: Brush,
    iconRes: Int,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(iconBrush),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = Color.White,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
        )
    }
}

@Composable
private fun ServiceSection(
    title: String,
    rows: List<List<ServiceItem>>,
    onItemClick: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 20.dp),
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(start = 8.dp, bottom = 16.dp),
            )

            rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    row.forEach { item ->
                        ServiceGridItem(
                            item = item,
                            onClick = { onItemClick(item.title) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                    repeat(4 - row.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun ServiceGridItem(
    item: ServiceItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = item.iconColor,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
        )
    }
}

@Preview
@Composable
private fun MineScreenPreview() {
    AppTheme() {
        MineScreen {}
    }
}
