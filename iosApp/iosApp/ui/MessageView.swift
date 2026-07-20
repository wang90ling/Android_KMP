/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import SwiftUI

struct MessageView: View {
    @State private var selectedSegment: Int = 0
    private let segments = ["消息", "通知"]

    var body: some View {
        ZStack(alignment: .top) {
            Color( "0F0F23")
                .ignoresSafeArea()

            VStack(spacing: 0) {
                // Header
                headerView
                    .padding(.top, 8)

                // Segment control
                segmentControl
                    .padding(.horizontal, 12)
                    .padding(.top, 16)

                // Content
                if selectedSegment == 0 {
                    messagesContent
                } else {
                    notificationsContent
                }
            }
        }
    }

    private var headerView: some View {
        HStack {
            Text("消息")
                .font(.system(size: 20, weight: .bold))
                .foregroundColor(.white)

            Spacer()

            Button(action: {}) {
                Image(systemName: "tray")
                    .font(.system(size: 20))
                    .foregroundColor(Color( "8B8BA7"))
            }
        }
        .padding(.horizontal, 16)
    }

    private var segmentControl: some View {
        HStack(spacing: 0) {
            ForEach(0..<segments.count, id: \.self) { index in
                Button(action: {
                    withAnimation(.easeInOut(duration: 0.2)) {
                        selectedSegment = index
                    }
                }) {
                    VStack(spacing: 5) {
                        Text(segments[index])
                            .font(.system(size: 15, weight: selectedSegment == index ? .bold : .regular))
                            .foregroundColor(selectedSegment == index ? Color( "E879F9") : Color( "8B8BA7"))

                        if selectedSegment == index {
                            Rectangle()
                                .fill(Color( "E879F9"))
                                .frame(width: 20, height: 2)
                        } else {
                            Rectangle()
                                .fill(Color.clear)
                                .frame(width: 20, height: 2)
                        }
                    }
                }
                .frame(maxWidth: .infinity)
            }
        }
    }

    private var messagesContent: some View {
        ScrollView(.vertical, showsIndicators: false) {
            LazyVStack(spacing: 0) {
                ForEach(mockMessages) { message in
                    MessageItemRow(message: message)
                    Divider()
                        .background(Color( "374151"))
                        .padding(.leading, 72)
                }
                Spacer().frame(height: 80)
            }
        }
    }

    private var notificationsContent: some View {
        ScrollView(.vertical, showsIndicators: false) {
            LazyVStack(spacing: 12) {
                ForEach(mockNotifications) { notification in
                    NotificationCard(notification: notification)
                }
                Spacer().frame(height: 80)
            }
            .padding(.horizontal, 12)
            .padding(.top, 16)
        }
    }
}

// MARK: - Message Model
private struct Message: Identifiable {
    let id: Int
    let avatar: String
    let username: String
    let lastMessage: String
    let time: String
    let unreadCount: Int
    let isOnline: Bool
}

private let mockMessages: [Message] = [
    Message(id: 1, avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cute%20anime%20girl%20avatar%20red%20hair&image_size=square", username: "小红", lastMessage: "好的，那我们明天见！", time: "刚刚", unreadCount: 2, isOnline: true),
    Message(id: 2, avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cute%20anime%20boy%20avatar%20blue%20hair&image_size=square", username: "小明", lastMessage: "哈哈，太好笑了", time: "10分钟前", unreadCount: 0, isOnline: true),
    Message(id: 3, avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cute%20anime%20girl%20avatar%20pink%20hair&image_size=square", username: "小美", lastMessage: "这个视频太棒了", time: "1小时前", unreadCount: 5, isOnline: false),
    Message(id: 4, avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cool%20anime%20boy%20avatar%20black%20hair&image_size=square", username: "阿杰", lastMessage: "有空一起打游戏吗", time: "2小时前", unreadCount: 1, isOnline: true),
    Message(id: 5, avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=elegant%20anime%20girl%20avatar%20white%20hair&image_size=square", username: "小雪", lastMessage: "晚安~", time: "昨天", unreadCount: 0, isOnline: false)
]

private struct MessageItemRow: View {
    let message: Message

    var body: some View {
        HStack(spacing: 12) {
            // Avatar with online indicator
            ZStack(alignment: .bottomTrailing) {
                AsyncImage(url: URL(string: message.avatar)) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                } placeholder: {
                    Circle()
                        .fill(Color( "374151"))
                }
                .frame(width: 52, height: 52)
                .clipShape(Circle())

                if message.isOnline {
                    Circle()
                        .fill(Color( "10B981"))
                        .frame(width: 12, height: 12)
                        .overlay(
                            Circle()
                                .stroke(Color( "1F1F3A"), lineWidth: 2)
                        )
                }
            }

            // Content
            VStack(alignment: .leading, spacing: 6) {
                HStack {
                    Text(message.username)
                        .font(.system(size: 15, weight: .semibold))
                        .foregroundColor(.white)

                    Spacer()

                    Text(message.time)
                        .font(.system(size: 12))
                        .foregroundColor(Color( "8B8BA7"))
                }

                HStack {
                    Text(message.lastMessage)
                        .font(.system(size: 13))
                        .foregroundColor(Color( "8B8BA7"))
                        .lineLimit(1)

                    Spacer()

                    if message.unreadCount > 0 {
                        Text("\(message.unreadCount)")
                            .font(.system(size: 11, weight: .bold))
                            .foregroundColor(.white)
                            .frame(width: 20, height: 20)
                            .background(Color( "EF4444"))
                            .clipShape(Circle())
                    }
                }
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        .background(Color( "0F0F23"))
    }
}

// MARK: - Notification Model
private struct Notification: Identifiable {
    let id: Int
    let type: String
    let icon: String
    let title: String
    let content: String
    let time: String
}

private let mockNotifications: [Notification] = [
    Notification(id: 1, type: "like", icon: "heart.fill", title: "有人赞了你", content: "小红、小明等15人赞了你的动态", time: "刚刚"),
    Notification(id: 2, type: "comment", icon: "bubble.right.fill", title: "新评论", content: "小美评论了你的动态: \"好棒啊！\"", time: "10分钟前"),
    Notification(id: 3, type: "follow", icon: "person.badge.plus", title: "新粉丝", content: "阿杰关注了你", time: "1小时前"),
    Notification(id: 4, type: "system", icon: "bell.fill", title: "系统通知", content: "恭喜你获得\"活跃达人\"称号", time: "昨天"),
    Notification(id: 5, type: "gift", icon: "gift.fill", title: "收到礼物", content: "小雪送了你一朵玫瑰花", time: "昨天")
]

private struct NotificationCard: View {
    let notification: Notification

    private var iconColor: Color {
        switch notification.type {
        case "like": return Color( "EF4444")
        case "comment": return Color( "3B82F6")
        case "follow": return Color( "10B981")
        case "gift": return Color( "EC4899")
        default: return Color( "8B8BA7")
        }
    }

    var body: some View {
        HStack(spacing: 12) {
            // Icon
            ZStack {
                Circle()
                    .fill(iconColor.opacity(0.2))
                    .frame(width: 44, height: 44)

                Image(systemName: notification.icon)
                    .font(.system(size: 18))
                    .foregroundColor(iconColor)
            }

            // Content
            VStack(alignment: .leading, spacing: 4) {
                HStack {
                    Text(notification.title)
                        .font(.system(size: 14, weight: .semibold))
                        .foregroundColor(.white)

                    Spacer()

                    Text(notification.time)
                        .font(.system(size: 11))
                        .foregroundColor(Color( "8B8BA7"))
                }

                Text(notification.content)
                    .font(.system(size: 12))
                    .foregroundColor(Color( "8B8BA7"))
                    .lineLimit(2)
            }
        }
        .padding(12)
        .background(Color( "1F1F3A"))
        .cornerRadius(12)
    }
}

