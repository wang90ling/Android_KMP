package com.example.fruitties.android.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fruitties.android.R
import com.example.fruitties.android.ui.AppTheme

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onBackClick: () -> Unit,
) {
    var phoneNumber by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(0) }

    LoginScreen(
        phoneNumber = phoneNumber,
        verificationCode = verificationCode,
        isChecked = isChecked,
        countdown = countdown,
        onPhoneNumberChange = { phoneNumber = it },
        onVerificationCodeChange = { verificationCode = it },
        onCheckedChange = { isChecked = it },
        onGetVerificationCode = {
            if (phoneNumber.length == 11) {
                countdown = 60
            }
        },
        onLoginClick = {
            if (isChecked && phoneNumber.length == 11 && verificationCode.length >= 4) {
                onLoginSuccess()
            }
        },
        onBackClick = onBackClick,
    )
}

@Composable
fun LoginScreen(
    phoneNumber: String,
    verificationCode: String,
    isChecked: Boolean,
    countdown: Int,
    onPhoneNumberChange: (String) -> Unit,
    onVerificationCodeChange: (String) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onGetVerificationCode: () -> Unit,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFBFF)),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
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
                TextButton(onClick = {}) {
                    Text(
                        text = "登录遇到问题？",
                        fontSize = 14.sp,
                        color = Color(0xFF8B8BA7),
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "HI~欢迎登录",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F1F3A),
                modifier = Modifier.padding(start = 32.dp),
            )

            Spacer(modifier = Modifier.height(48.dp))

            Column(
                modifier = Modifier.padding(horizontal = 18.dp),
            ) {
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = onPhoneNumberChange,
                    placeholder = {
                        Text(
                            text = "请输入手机号",
                            fontSize = 14.sp,
                            color = Color(0xFF8B8BA7),
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF9333EA),
                        unfocusedBorderColor = Color(0xFFD1D5DB),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    ),
                    leadingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 10.dp,end = 2.dp),
                        ) {
                            Text(
                                text = "+86",
                                fontSize = 14.sp,
                                color = Color(0xFF1F1F3A),
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFF8B8BA7),
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                    ),
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = verificationCode,
                    onValueChange = onVerificationCodeChange,
                    placeholder = {
                        Text(
                            text = "请输入验证码",
                            fontSize = 14.sp,
                            color = Color(0xFF8B8BA7),
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF9333EA),
                        unfocusedBorderColor = Color(0xFFD1D5DB),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFF8B8BA7),
                        )
                    },
                    trailingIcon = {
                        TextButton(
                            onClick = onGetVerificationCode,
                            enabled = countdown == 0 && phoneNumber.length == 11,
                            modifier = Modifier.padding(end = 8.dp),
                        ) {
                            Text(
                                text = if (countdown > 0) "${countdown}s后获取" else "获取验证码",
                                fontSize = 14.sp,
                                color = Color(0xFF9333EA),
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 4.dp),
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = onCheckedChange,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF9333EA),
                            uncheckedColor = Color(0xFFD1D5DB),
                            checkmarkColor = Color.White,
                        ),
                    )
                    Text(
                        text = "我已阅读并同意",
                        fontSize = 12.sp,
                        color = Color(0xFF8B8BA7),
                    )
                    Text(
                        text = "用户协议",
                        fontSize = 12.sp,
                        color = Color(0xFF9333EA),
                    )
                    Text(
                        text = "和",
                        fontSize = 12.sp,
                        color = Color(0xFF8B8BA7),
                    )
                    Text(
                        text = "隐私政策",
                        fontSize = 12.sp,
                        color = Color(0xFF9333EA),
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White,
                    ),
                    enabled = isChecked && phoneNumber.length == 11 && verificationCode.length >= 4,
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
                            text = "登录",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "账号登录",
                        fontSize = 14.sp,
                        color = Color(0xFF8B8BA7),
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 40.dp),
            ) {
                Text(
                    text = "其他登录方式",
                    fontSize = 12.sp,
                    color = Color(0xFF8B8BA7),
                    modifier = Modifier.padding(bottom = 16.dp),
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(48.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "QQ登录",
                            modifier = Modifier.size(24.dp),
                            tint = Color(0xFF1B9AF7),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Chat,
                            contentDescription = "微信登录",
                            modifier = Modifier.size(24.dp),
                            tint = Color(0xFF07C160),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    AppTheme() {
        LoginScreen(
            phoneNumber = "",
            verificationCode = "",
            isChecked = false,
            countdown = 0,
            onPhoneNumberChange = {},
            onVerificationCodeChange = {},
            onCheckedChange = {},
            onGetVerificationCode = {},
            onLoginClick = {},
            onBackClick = {},
        )
    }
}