package com.example.fruitties.android.ui.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fruitties.android.ui.AppTheme

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onLogout: () -> Unit,
) {
    var isRankingHidden by remember { mutableStateOf(false) }
    var isRoomHidden by remember { mutableStateOf(false) }
    var isFloatingWindow by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F5FA)),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 56.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "返回",
                        modifier = Modifier.size(24.dp),
                        tint = Color(0xFF1F1F3A),
                    )
                }
                Text(
                    text = "设置",
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = Color(0xFF1F1F3A),
                    modifier = Modifier.padding(start = 12.dp),
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
            ) {
                item {
                    SettingsItem(
                        title = "账号管理",
                        hasArrow = true,
                        onClick = {},
                    )
                }
                item {
                    SettingsItem(
                        title = "消息提醒",
                        hasArrow = true,
                        onClick = {},
                    )
                }
                item {
                    SettingsItem(
                        title = "第三方信息共享清单",
                        hasArrow = true,
                        onClick = {},
                    )
                }
                item {
                    SettingsItem(
                        title = "账号切换",
                        hasArrow = true,
                        onClick = {},
                    )
                }
                item {
                    SettingsSwitchItem(
                        title = "榜单隐身",
                        checked = isRankingHidden,
                        onCheckedChange = { isRankingHidden = it },
                    )
                }
                item {
                    SettingsSwitchItem(
                        title = "进房隐身",
                        checked = isRoomHidden,
                        onCheckedChange = { isRoomHidden = it },
                    )
                }
                item {
                    SettingsItem(
                        title = "清除缓存",
                        rightText = "115.922MB",
                        onClick = {},
                    )
                }
                item {
                    SettingsItem(
                        title = "关于",
                        rightText = "1.24.0.1001 无更新",
                        onClick = {},
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    SettingsSwitchItem(
                        title = "APP信息悬浮窗",
                        checked = isFloatingWindow,
                        onCheckedChange = { isFloatingWindow = it },
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
            ) {
                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White,
                    ),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF3B82F6), Color(0xFF9333EA), Color(0xFFEC4899)),
                                )
                            )
                            .clip(RoundedCornerShape(28.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "退出登录",
                            fontSize = 18.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    rightText: String? = null,
    hasArrow: Boolean = false,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontSize = 15.sp,
            color = Color(0xFF1F1F3A),
        )
        Spacer(modifier = Modifier.weight(1f))
        if (rightText != null) {
            Text(
                text = rightText,
                fontSize = 14.sp,
                color = Color(0xFF8B8BA7),
            )
        }
        if (hasArrow) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color(0xFFC4C4C4),
            )
        }
    }
}

@Composable
private fun SettingsSwitchItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontSize = 15.sp,
            color = Color(0xFF1F1F3A),
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF9333EA),
                checkedTrackColor = Color(0xFFE9D5FF),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE5E7EB),
            ),
        )
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    AppTheme() {
        SettingsScreen(
            onBackClick = {},
            onLogout = {},
        )
    }
}