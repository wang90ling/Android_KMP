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
import shared

private let topTabTitles = ["推荐", "派单厅", "热门", "交友", "点唱"]
private let bottomTabTitles = ["推荐主播", "心动房间", "休闲天地"]

// MARK: - Feature Card Model
private struct FeatureCard: Identifiable {
    let id = UUID()
    let title: String
    let subtitle: String
    let gradientColors: [Color]
}

private let featureCards = [
    FeatureCard(title: "去交友", subtitle: "遇见美好的TA", gradientColors: [Color( "D946EF"), Color( "DB2777")]),
    FeatureCard(title: "NBTI测试", subtitle: "挖掘潜在人格", gradientColors: [Color( "6366F1"), Color( "8B5CF6")]),
    FeatureCard(title: "去开黑", subtitle: "默契队友在这里", gradientColors: [Color( "7C3AED"), Color( "4C1D95")]),
    FeatureCard(title: "去扩列", subtitle: "遇见有趣的人", gradientColors: [Color( "D97706"), Color( "EA580C")])
]

// MARK: - Anchor Model
private struct Anchor: Identifiable {
    let id = UUID()
    let name: String
    let avatar: String
    let isLive: Bool
    let viewerCount: String
}

private let anchors = [
    Anchor(name: "小红", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cute%20anime%20boy%20avatar%20red%20hair%203d%20style&image_size=square", isLive: true, viewerCount: "9"),
    Anchor(name: "额威威", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cute%20anime%20boy%20avatar%20blonde%20hair%203d%20style&image_size=square", isLive: true, viewerCount: "6"),
    Anchor(name: "测试测试测试测试测...", avatar: "", isLive: false, viewerCount: ""),
    Anchor(name: "乐游集结号割发...", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cute%20anime%20boy%20avatar%20blonde%20hair%20cool%20style&image_size=square", isLive: true, viewerCount: "6")
]

// MARK: - Room Model
private struct Room: Identifiable {
    let id = UUID()
    let name: String
    let cover: String
    let hostName: String
    let memberCount: Int
}

private let rooms = [
    Room(name: "甜蜜小屋", cover: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cozy%20romantic%20room%20pink%20lighting%20anime%20style&image_size=square", hostName: "小美", memberCount: 12),
    Room(name: "游戏开黑房", cover: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=gaming%20room%20neon%20lights%20dark%20blue&image_size=square", hostName: "大神", memberCount: 8),
    Room(name: "音乐派对", cover: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=music%20party%20room%20colorful%20lights&image_size=square", hostName: "DJ小王", memberCount: 25),
    Room(name: "深夜聊天室", cover: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=late%20night%20chat%20room%20warm%20lighting&image_size=square", hostName: "夜猫子", memberCount: 18)
]

// MARK: - Activity Model
private struct Activity: Identifiable {
    let id = UUID()
    let name: String
    let participants: Int
}

private let activities = [
    Activity(name: "真心话大冒险", participants: 520),
    Activity(name: "谁是卧底", participants: 380),
    Activity(name: "你画我猜", participants: 290),
    Activity(name: "狼人杀", participants: 450),
    Activity(name: "K歌之王", participants: 320),
    Activity(name: "剧本杀", participants: 180)
]

// MARK: - Dispatch Task Model
private struct DispatchTask: Identifiable {
    let id = UUID()
    let title: String
    let type: String
    let price: String
    let status: String
    let avatar: String
    let publisher: String
}

private let dispatchTasks = [
    DispatchTask(title: "陪玩游戏·王者荣耀", type: "游戏陪玩", price: "¥30/局", status: "待接单", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=gamer%20avatar%20cool%20headset&image_size=square", publisher: "小甜甜"),
    DispatchTask(title: "语音聊天·情感倾诉", type: "语音聊天", price: "¥50/小时", status: "待接单", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=sweet%20girl%20avatar%20headphones&image_size=square", publisher: "暖心小姐姐"),
    DispatchTask(title: "唱歌点播·流行金曲", type: "才艺点唱", price: "¥20/首", status: "待接单", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=singer%20avatar%20microphone%20stage&image_size=square", publisher: "音乐小王子"),
    DispatchTask(title: "开黑上分·和平精英", type: "游戏陪玩", price: "¥25/局", status: "进行中", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=pro%20gamer%20avatar%20military%20helmet&image_size=square", publisher: "吃鸡大神"),
    DispatchTask(title: "深夜电台·故事分享", type: "语音聊天", price: "¥40/小时", status: "待接单", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=radio%20host%20avatar%20night%20warm&image_size=square", publisher: "夜语者"),
    DispatchTask(title: "剧本杀·多人组局", type: "游戏陪玩", price: "¥35/场", status: "待接单", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=mystery%20detective%20avatar%20magnifier&image_size=square", publisher: "推理大师")
]

// MARK: - Hot Item Model
private struct HotItem: Identifiable {
    let id = UUID()
    let rank: Int
    let name: String
    let avatar: String
    let hotValue: String
    let tag: String
}

private let hotItems = [
    HotItem(rank: 1, name: "倾城小仙女", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=beautiful%20fairy%20girl%20avatar%20sparkle&image_size=square", hotValue: "128.5w", tag: "才艺主播"),
    HotItem(rank: 2, name: "深夜歌手", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=male%20singer%20avatar%20microphone%20cool&image_size=square", hotValue: "96.3w", tag: "音乐才子"),
    HotItem(rank: 3, name: "游戏女王", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=female%20gamer%20avatar%20neon%20glasses&image_size=square", hotValue: "85.7w", tag: "游戏陪玩"),
    HotItem(rank: 4, name: "暖心大叔", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=warm%20uncle%20avatar%20smile%20gentle&image_size=square", hotValue: "72.1w", tag: "情感电台"),
    HotItem(rank: 5, name: "甜美小可爱", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cute%20sweet%20girl%20avatar%20pink&image_size=square", hotValue: "68.9w", tag: "舞蹈主播"),
    HotItem(rank: 6, name: "摇滚少年", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=rock%20boy%20avatar%20guitar%20cool&image_size=square", hotValue: "55.4w", tag: "音乐才子")
]

// MARK: - Dating User Model
private struct DatingUser: Identifiable {
    let id = UUID()
    let name: String
    let avatar: String
    let age: Int
    let gender: String
    let city: String
    let signature: String
    let isOnline: Bool
}

private let datingUsers = [
    DatingUser(name: "柠檬不萌", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cute%20girl%20avatar%20yellow%20lemon%20theme&image_size=square", age: 22, gender: "女", city: "北京", signature: "想遇见有趣的你", isOnline: true),
    DatingUser(name: "星辰大海", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=handsome%20boy%20avatar%20star%20galaxy%20theme&image_size=square", age: 25, gender: "男", city: "上海", signature: "寻找那个对的人", isOnline: true),
    DatingUser(name: "温柔岁月", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=elegant%20woman%20avatar%20soft%20warm%20light&image_size=square", age: 24, gender: "女", city: "广州", signature: "愿得一人心", isOnline: false),
    DatingUser(name: "清风明月", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=gentle%20man%20avatar%20moon%20breeze%20theme&image_size=square", age: 27, gender: "男", city: "深圳", signature: "等风也等你", isOnline: true),
    DatingUser(name: "糖果女孩", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=sweet%20candy%20girl%20avatar%20colorful&image_size=square", age: 21, gender: "女", city: "杭州", signature: "甜甜的恋爱轮到我了吗", isOnline: true),
    DatingUser(name: "孤独患者", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=lonely%20boy%20avatar%20dark%20moody%20style&image_size=square", age: 26, gender: "男", city: "成都", signature: "想找个人说说话", isOnline: false)
]

// MARK: - Song Model
private struct Song: Identifiable {
    let id = UUID()
    let name: String
    let singer: String
    let cover: String
    let orderCount: Int
    let duration: String
}

private let songs = [
    Song(name: "晴天", singer: "周杰伦", cover: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=music%20album%20cover%20sunny%20day%20guitar&image_size=square", orderCount: 1280, duration: "4:29"),
    Song(name: "起风了", singer: "买辣椒也用券", cover: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=music%20album%20cover%20wind%20breeze%20sky&image_size=square", orderCount: 980, duration: "5:25"),
    Song(name: "孤勇者", singer: "陈奕迅", cover: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=music%20album%20cover%20lonely%20hero%20dark&image_size=square", orderCount: 1560, duration: "4:13"),
    Song(name: "稻香", singer: "周杰伦", cover: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=music%20album%20cover%20rice%20field%20countryside&image_size=square", orderCount: 890, duration: "3:43"),
    Song(name: "告白气球", singer: "周杰伦", cover: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=music%20album%20cover%20balloon%20romantic%20pink&image_size=square", orderCount: 1120, duration: "3:35"),
    Song(name: "年少有为", singer: "李荣浩", cover: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=music%20album%20cover%20young%20ambition%20city&image_size=square", orderCount: 750, duration: "4:51")
]

// MARK: - HomeView
struct HomeView: View {
    @State private var selectedTabIndex: Int = 0
    @State private var selectedTopTabIndex: Int = 0

    var body: some View {
        ZStack(alignment: .top) {
            Color( "0F0F23")
                .ignoresSafeArea()

            VStack(spacing: 0) {
                // Top tab bar
                topTabBar
                    .padding(.top, 8)

                // Content
                TabView(selection: $selectedTopTabIndex) {
                    RecommendTabContent()
                        .tag(0)
                    DispatchTabContent()
                        .tag(1)
                    HotTabContent()
                        .tag(2)
                    DatingTabContent()
                        .tag(3)
                    SingTabContent()
                        .tag(4)
                }
                .tabViewStyle(.page(indexDisplayMode: .never))
            }
        }
    }

    private var topTabBar: some View {
        HStack(spacing: 0) {
            ForEach(0..<topTabTitles.count, id: \.self) { index in
                VStack(spacing: 5) {
                    Text(topTabTitles[index])
                        .font(.system(size: 16, weight: selectedTopTabIndex == index ? .bold : .regular))
                        .foregroundColor(selectedTopTabIndex == index ? Color( "E879F9") : Color( "8B8BA7"))

                    if selectedTopTabIndex == index {
                        Rectangle()
                            .fill(Color( "E879F9"))
                            .frame(width: 20, height: 2)
                    } else {
                        Rectangle()
                            .fill(Color.clear)
                            .frame(width: 20, height: 2)
                    }
                }
                .frame(maxWidth: .infinity)
                .padding(.vertical, 10)
                .contentShape(Rectangle())
                .onTapGesture {
                    withAnimation(.easeInOut(duration: 0.2)) {
                        selectedTopTabIndex = index
                    }
                }
            }
        }
        .padding(.horizontal, 8)
    }
}

// MARK: - Recommend Tab Content
private struct RecommendTabContent: View {
    @State private var selectedBottomTabIndex: Int = 0

    var body: some View {
        ScrollView(.vertical, showsIndicators: false) {
            VStack(spacing: 12) {
                // Feature cards
                FeatureCardsSection()

                // Secondary tabs
                VStack(spacing: 0) {
                    HStack(spacing: 0) {
                        ForEach(0..<bottomTabTitles.count, id: \.self) { index in
                            Button(action: {
                                selectedBottomTabIndex = index
                            }) {
                                Text(bottomTabTitles[index])
                                    .font(.system(size: 15, weight: selectedBottomTabIndex == index ? .bold : .regular))
                                    .foregroundColor(selectedBottomTabIndex == index ? .white : Color( "8B8BA7"))
                                    .padding(.vertical, 8)
                                    .padding(.horizontal, 12)
                            }
                        }
                        Spacer()
                        Text("筛选")
                            .font(.system(size: 13))
                            .foregroundColor(Color( "8B8BA7"))
                            .padding(.trailing, 12)
                    }
                    .padding(.horizontal, 12)

                    Divider()
                        .background(Color( "8B8BA7").opacity(0.3))
                }

                // Content based on selection
                LazyVStack(spacing: 12) {
                    switch selectedBottomTabIndex {
                    case 0:
                        ForEach(0..<anchors.count, id: \.self) { index in
                            if index % 2 == 0 {
                                AnchorGridRow(
                                    left: anchors[index],
                                    right: index + 1 < anchors.count ? anchors[index + 1] : nil
                                )
                            }
                        }
                    case 1:
                        ForEach(0..<rooms.count, id: \.self) { index in
                            if index % 2 == 0 {
                                RoomGridRow(
                                    left: rooms[index],
                                    right: index + 1 < rooms.count ? rooms[index + 1] : nil
                                )
                            }
                        }
                    case 2:
                        ForEach(0..<activities.count, id: \.self) { index in
                            if index % 2 == 0 {
                                ActivityGridRow(
                                    left: activities[index],
                                    right: index + 1 < activities.count ? activities[index + 1] : nil
                                )
                            }
                        }
                    default:
                        EmptyView()
                    }
                }
                .padding(.horizontal, 12)

                Spacer().frame(height: 80)
            }
            .padding(.top, 12)
        }
    }
}

// MARK: - Feature Cards Section
private struct FeatureCardsSection: View {
    var body: some View {
        VStack(spacing: 12) {
            HStack(spacing: 12) {
                FeatureCardItem(card: featureCards[0])
                FeatureCardItem(card: featureCards[1])
            }
            HStack(spacing: 12) {
                FeatureCardItem(card: featureCards[2])
                FeatureCardItem(card: featureCards[3])
            }
        }
        .padding(.horizontal, 12)
    }
}

private struct FeatureCardItem: View {
    let card: FeatureCard

    var body: some View {
        VStack(spacing: 4) {
            Text(card.title)
                .font(.system(size: 20, weight: .bold))
                .foregroundColor(.white)
            Text(card.subtitle)
                .font(.system(size: 12))
                .foregroundColor(.white.opacity(0.8))
        }
        .frame(maxWidth: .infinity)
        .aspectRatio(1.2, contentMode: .fit)
        .background(
            LinearGradient(
                colors: card.gradientColors,
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        )
        .cornerRadius(16)
    }
}

// MARK: - Dispatch Tab Content
private struct DispatchTabContent: View {
    var body: some View {
        ScrollView(.vertical, showsIndicators: false) {
            LazyVStack(spacing: 12) {
                ForEach(dispatchTasks) { task in
                    DispatchTaskCard(task: task)
                }
                Spacer().frame(height: 80)
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 12)
        }
    }
}

private struct DispatchTaskCard: View {
    let task: DispatchTask

    var body: some View {
        HStack(spacing: 12) {
            // Avatar
            AsyncImage(url: URL(string: task.avatar)) { image in
                image
                    .resizable()
                    .aspectRatio(contentMode: .fill)
            } placeholder: {
                Circle()
                    .fill(Color( "374151"))
            }
            .frame(width: 56, height: 56)
            .clipShape(Circle())

            // Info
            VStack(alignment: .leading, spacing: 4) {
                Text(task.title)
                    .font(.system(size: 15, weight: .semibold))
                    .foregroundColor(.white)
                    .lineLimit(1)

                HStack(spacing: 8) {
                    Text(task.type)
                        .font(.system(size: 11))
                        .foregroundColor(Color( "E879F9"))
                        .padding(.horizontal, 6)
                        .padding(.vertical, 2)
                        .background(Color( "E879F9").opacity(0.2))
                        .cornerRadius(4)

                    Text("发布者: \(task.publisher)")
                        .font(.system(size: 12))
                        .foregroundColor(Color( "8B8BA7"))
                }
            }

            Spacer()

            // Price and status
            VStack(alignment: .trailing, spacing: 6) {
                Text(task.price)
                    .font(.system(size: 16, weight: .bold))
                    .foregroundColor(Color( "E879F9"))

                Text(task.status)
                    .font(.system(size: 11))
                    .foregroundColor(.white)
                    .padding(.horizontal, 8)
                    .padding(.vertical, 2)
                    .background(task.status == "待接单" ? Color( "D97706") : Color( "10B981"))
                    .cornerRadius(10)
            }
        }
        .padding(12)
        .background(Color( "1F1F3A"))
        .cornerRadius(16)
    }
}

// MARK: - Hot Tab Content
private struct HotTabContent: View {
    var body: some View {
        ScrollView(.vertical, showsIndicators: false) {
            LazyVStack(spacing: 12) {
                ForEach(hotItems) { item in
                    HotRankCard(item: item)
                }
                Spacer().frame(height: 80)
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 12)
        }
    }
}

private struct HotRankCard: View {
    let item: HotItem

    private var rankColor: Color {
        switch item.rank {
        case 1: return Color( "EF4444")
        case 2: return Color( "F59E0B")
        case 3: return Color( "EAB308")
        default: return Color( "374151")
        }
    }

    var body: some View {
        HStack(spacing: 12) {
            // Rank badge
            Text("\(item.rank)")
                .font(.system(size: 18, weight: .bold))
                .foregroundColor(item.rank <= 3 ? .white : Color( "8B8BA7"))
                .frame(width: 36, height: 36)
                .background(rankColor)
                .clipShape(Circle())

            // Avatar
            AsyncImage(url: URL(string: item.avatar)) { image in
                image
                    .resizable()
                    .aspectRatio(contentMode: .fill)
            } placeholder: {
                Circle()
                    .fill(Color( "374151"))
            }
            .frame(width: 52, height: 52)
            .clipShape(Circle())

            // Info
            VStack(alignment: .leading, spacing: 4) {
                Text(item.name)
                    .font(.system(size: 15, weight: .semibold))
                    .foregroundColor(.white)
                    .lineLimit(1)

                Text(item.tag)
                    .font(.system(size: 11))
                    .foregroundColor(Color( "E879F9"))
                    .padding(.horizontal, 6)
                    .padding(.vertical, 2)
                    .background(Color( "E879F9").opacity(0.2))
                    .cornerRadius(4)
            }

            Spacer()

            // Hot value
            VStack(alignment: .trailing, spacing: 4) {
                Text(item.hotValue)
                    .font(.system(size: 16, weight: .bold))
                    .foregroundColor(Color( "EF4444"))

                Text("热度")
                    .font(.system(size: 11))
                    .foregroundColor(Color( "8B8BA7"))
            }
        }
        .padding(12)
        .background(Color( "1F1F3A"))
        .cornerRadius(16)
    }
}

// MARK: - Dating Tab Content
private struct DatingTabContent: View {
    var body: some View {
        ScrollView(.vertical, showsIndicators: false) {
            LazyVStack(spacing: 12) {
                ForEach(datingUsers) { user in
                    DatingUserCard(user: user)
                }
                Spacer().frame(height: 80)
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 12)
        }
    }
}

private struct DatingUserCard: View {
    let user: DatingUser

    var body: some View {
        HStack(spacing: 12) {
            // Avatar with online indicator
            ZStack(alignment: .bottomTrailing) {
                AsyncImage(url: URL(string: user.avatar)) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                } placeholder: {
                    Circle()
                        .fill(Color( "374151"))
                }
                .frame(width: 64, height: 64)
                .clipShape(Circle())

                if user.isOnline {
                    Circle()
                        .fill(Color( "10B981"))
                        .frame(width: 14, height: 14)
                        .overlay(
                            Circle()
                                .stroke(Color( "1F1F3A"), lineWidth: 2)
                        )
                }
            }

            // Info
            VStack(alignment: .leading, spacing: 6) {
                HStack(spacing: 8) {
                    Text(user.name)
                        .font(.system(size: 15, weight: .semibold))
                        .foregroundColor(.white)
                        .lineLimit(1)

                    Text(user.gender)
                        .font(.system(size: 11, weight: .bold))
                        .foregroundColor(.white)
                        .frame(width: 18, height: 18)
                        .background(user.gender == "女" ? Color( "EC4899") : Color( "3B82F6"))
                        .clipShape(Circle())

                    Text("\(user.age)岁")
                        .font(.system(size: 12))
                        .foregroundColor(Color( "8B8BA7"))
                }

                HStack(spacing: 8) {
                    Text(user.city)
                        .font(.system(size: 12))
                        .foregroundColor(Color( "8B8BA7"))

                    Text(user.isOnline ? "在线" : "离线")
                        .font(.system(size: 12))
                        .foregroundColor(user.isOnline ? Color( "10B981") : Color( "8B8BA7"))
                }

                Text(user.signature)
                    .font(.system(size: 12))
                    .foregroundColor(Color( "8B8BA7"))
                    .lineLimit(1)
            }

            Spacer()

            // Follow button
            Text("关注")
                .font(.system(size: 13, weight: .medium))
                .foregroundColor(.white)
                .padding(.horizontal, 16)
                .padding(.vertical, 8)
                .background(Color( "E879F9"))
                .cornerRadius(16)
        }
        .padding(12)
        .background(Color( "1F1F3A"))
        .cornerRadius(16)
    }
}

// MARK: - Sing Tab Content
private struct SingTabContent: View {
    var body: some View {
        ScrollView(.vertical, showsIndicators: false) {
            LazyVStack(spacing: 12) {
                ForEach(songs) { song in
                    SongCard(song: song)
                }
                Spacer().frame(height: 80)
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 12)
        }
    }
}

private struct SongCard: View {
    let song: Song

    var body: some View {
        HStack(spacing: 12) {
            // Cover
            AsyncImage(url: URL(string: song.cover)) { image in
                image
                    .resizable()
                    .aspectRatio(contentMode: .fill)
            } placeholder: {
                RoundedRectangle(cornerRadius: 12)
                    .fill(Color( "374151"))
            }
            .frame(width: 56, height: 56)
            .cornerRadius(12)

            // Info
            VStack(alignment: .leading, spacing: 4) {
                Text(song.name)
                    .font(.system(size: 15, weight: .semibold))
                    .foregroundColor(.white)
                    .lineLimit(1)

                Text(song.singer)
                    .font(.system(size: 13))
                    .foregroundColor(Color( "8B8BA7"))
                    .lineLimit(1)

                HStack(spacing: 8) {
                    Text("已点唱 \(song.orderCount)次")
                        .font(.system(size: 11))
                        .foregroundColor(Color( "8B8BA7"))

                    Text(song.duration)
                        .font(.system(size: 11))
                        .foregroundColor(Color( "8B8BA7"))
                }
            }

            Spacer()

            // Order button
            Text("点唱")
                .font(.system(size: 13, weight: .medium))
                .foregroundColor(.white)
                .padding(.horizontal, 16)
                .padding(.vertical, 8)
                .background(Color( "E879F9"))
                .cornerRadius(16)
        }
        .padding(12)
        .background(Color( "1F1F3A"))
        .cornerRadius(16)
    }
}

// MARK: - Grid Row Components
private struct AnchorGridRow: View {
    let left: Anchor
    let right: Anchor?

    var body: some View {
        HStack(spacing: 12) {
            AnchorCard(anchor: left)
            if let right = right {
                AnchorCard(anchor: right)
            } else {
                Color.clear
                    .frame(maxWidth: .infinity)
            }
        }
    }
}

private struct AnchorCard: View {
    let anchor: Anchor

    var body: some View {
        VStack {
            ZStack(alignment: .top) {
                // Background
                if !anchor.avatar.isEmpty {
                    AsyncImage(url: URL(string: anchor.avatar)) { image in
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                    } placeholder: {
                        Color( "374151")
                    }
                } else {
                    Color( "374151")
                    Text(anchor.name.prefix(1))
                        .font(.system(size: 48, weight: .bold))
                        .foregroundColor(.white)
                }

                // Live badge
                if anchor.isLive {
                    Text("推荐主播")
                        .font(.system(size: 10, weight: .bold))
                        .foregroundColor(.white)
                        .padding(.horizontal, 8)
                        .padding(.vertical, 2)
                        .background(Color( "EF4444"))
                        .cornerRadius(10)
                        .padding(.top, 8)
                }

                // Bottom info
                VStack(alignment: .leading, spacing: 4) {
                    Spacer()
                    Text(anchor.name)
                        .font(.system(size: 14, weight: .semibold))
                        .foregroundColor(.white)
                        .lineLimit(1)

                    if anchor.isLive {
                        HStack(spacing: 4) {
                            Image(systemName: "plus.circle.fill")
                                .font(.system(size: 12))
                                .foregroundColor(Color( "EF4444"))
                            Text("小帅 \(anchor.viewerCount)")
                                .font(.system(size: 12))
                                .foregroundColor(.white.opacity(0.8))
                        }
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(12)
                .background(
                    LinearGradient(
                        colors: [.clear, .black.opacity(0.6)],
                        startPoint: .top,
                        endPoint: .bottom
                    )
                )
            }
        }
        .aspectRatio(1, contentMode: .fit)
        .background(Color( "1F1F3A"))
        .cornerRadius(16)
        .clipped()
    }
}

private struct RoomGridRow: View {
    let left: Room
    let right: Room?

    var body: some View {
        HStack(spacing: 12) {
            RoomCard(room: left)
            if let right = right {
                RoomCard(room: right)
            } else {
                Color.clear
                    .frame(maxWidth: .infinity)
            }
        }
    }
}

private struct RoomCard: View {
    let room: Room

    var body: some View {
        VStack {
            ZStack(alignment: .bottomLeading) {
                // Background
                if !room.cover.isEmpty {
                    AsyncImage(url: URL(string: room.cover)) { image in
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                    } placeholder: {
                        Color( "374151")
                    }
                } else {
                    Color( "374151")
                    Text(room.name.prefix(1))
                        .font(.system(size: 48, weight: .bold))
                        .foregroundColor(.white)
                }

                // Bottom info
                VStack(alignment: .leading, spacing: 4) {
                    Spacer()
                    Text(room.name)
                        .font(.system(size: 14, weight: .semibold))
                        .foregroundColor(.white)
                        .lineLimit(1)

                    Text("\(room.hostName) · \(room.memberCount)人")
                        .font(.system(size: 12))
                        .foregroundColor(.white.opacity(0.8))
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(12)
                .background(
                    LinearGradient(
                        colors: [.clear, .black.opacity(0.6)],
                        startPoint: .top,
                        endPoint: .bottom
                    )
                )
            }
        }
        .aspectRatio(1, contentMode: .fit)
        .background(Color( "1F1F3A"))
        .cornerRadius(16)
        .clipped()
    }
}

private struct ActivityGridRow: View {
    let left: Activity
    let right: Activity?

    var body: some View {
        HStack(spacing: 12) {
            ActivityCard(activity: left)
            if let right = right {
                ActivityCard(activity: right)
            } else {
                Color.clear
                    .frame(maxWidth: .infinity)
            }
        }
    }
}

private struct ActivityCard: View {
    let activity: Activity

    var body: some View {
        VStack(spacing: 12) {
            ZStack {
                Circle()
                    .fill(Color( "374151"))
                    .frame(width: 64, height: 64)
                Text(activity.name.prefix(1))
                    .font(.system(size: 28, weight: .bold))
                    .foregroundColor(Color( "E879F9"))
            }

            Text(activity.name)
                .font(.system(size: 14, weight: .semibold))
                .foregroundColor(.white)
                .multilineTextAlignment(.center)
                .lineLimit(2)

            Text("\(activity.participants)人在玩")
                .font(.system(size: 12))
                .foregroundColor(Color( "8B8BA7"))
        }
        .frame(maxWidth: .infinity)
        .aspectRatio(1.2, contentMode: .fit)
        .background(Color( "1F1F3A"))
        .cornerRadius(16)
    }
}

