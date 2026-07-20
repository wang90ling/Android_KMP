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

enum BottomNavItem: Int, CaseIterable {
    case home = 0
    case circle = 1
    case message = 2
    case mine = 3

    var title: String {
        switch self {
        case .home: return "首页"
        case .circle: return "圈子"
        case .message: return "消息"
        case .mine: return "我的"
        }
    }

    var iconName: String {
        switch self {
        case .home: return "house.fill"
        case .circle: return "person.2.fill"
        case .message: return "message.fill"
        case .mine: return "person.fill"
        }
    }

    var unselectedIconName: String {
        switch self {
        case .home: return "house"
        case .circle: return "person.2"
        case .message: return "message"
        case .mine: return "person"
        }
    }
}

struct MainView: View {
    @State private var selectedTab: BottomNavItem = .home
    @State private var showCart: Bool = false
    @State private var unreadMessageCount: Int = 99

    var body: some View {
        ZStack(alignment: .bottom) {
            TabView(selection: $selectedTab) {
                HomeView()
                    .tag(BottomNavItem.home)

                CircleView()
                    .tag(BottomNavItem.circle)

                MessageView()
                    .tag(BottomNavItem.message)

                MineView()
                    .tag(BottomNavItem.mine)
            }
            .ignoresSafeArea()
            .tabViewStyle(.page(indexDisplayMode: .never))

            // Custom bottom navigation bar
            CustomBottomNavigationBar(
                selectedTab: $selectedTab,
                unreadCount: unreadMessageCount
            )
        }
        .ignoresSafeArea(.keyboard)
        .sheet(isPresented: $showCart) {
            CartView()
        }
    }
}

struct CustomBottomNavigationBar: View {
    @Binding var selectedTab: BottomNavItem
    let unreadCount: Int

    var body: some View {
        HStack(spacing: 0) {
            ForEach(BottomNavItem.allCases, id: \.rawValue) { item in
                tabItem(for: item)
            }
        }
        .padding(.horizontal, 8)
        .padding(.top, 8)
        .padding(.bottom, 20)
        .background(
            Color( "1F1F3A")
                .ignoresSafeArea(edges: .bottom)
        )
    }

    @ViewBuilder
    private func tabItem(for item: BottomNavItem) -> some View {
        let isSelected = selectedTab == item

        Button(action: {
            withAnimation(.easeInOut(duration: 0.2)) {
                selectedTab = item
            }
        }) {
            VStack(spacing: 4) {
                ZStack {
                    Image(systemName: isSelected ? item.iconName : item.unselectedIconName)
                        .font(.system(size: 22))

                    if item == .message && unreadCount > 0 {
                        Text(unreadCount > 99 ? "99+" : "\(unreadCount)")
                            .font(.system(size: 10, weight: .bold))
                            .foregroundColor(.white)
                            .padding(4)
                            .background(Color( "FF6B6B"))
                            .clipShape(Circle())
                            .offset(x: 12, y: -8)
                    }
                }
                .foregroundColor(isSelected ? Color( "E879F9") : Color( "8B8BA7"))

                Text(item.title)
                    .font(.system(size: 12))
                    .foregroundColor(isSelected ? Color( "E879F9") : Color( "8B8BA7"))
            }
            .frame(maxWidth: .infinity)
        }
        .buttonStyle(PlainButtonStyle())
    }
}
