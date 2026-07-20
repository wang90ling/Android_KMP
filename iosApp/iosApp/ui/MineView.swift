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
import Foundation
import shared

struct MineView: View {
    @EnvironmentObject var viewModelStoreOwner: IOSViewModelStoreOwner
    @EnvironmentObject var appContainer: ObservableValueWrapper<AppContainer>

    @State private var showSettings: Bool = false

    private var viewModel: LoginViewModel {
        viewModelStoreOwner.viewModel(
            factory: appContainer.value.loginViewModelFactory
        )
    }

    var body: some View {
        ZStack(alignment: .top) {
            Color( "0F0F23")
                .ignoresSafeArea()

            ScrollView(.vertical, showsIndicators: false) {
                VStack(spacing: 0) {
                    // Header
                    headerView
                        .padding(.top, 8)

                    // Profile card
                    profileCard
                        .padding(.horizontal, 12)
                        .padding(.top, 20)

                    // Stats section
                    statsSection
                        .padding(.horizontal, 12)
                        .padding(.top, 20)

                    // Menu items
                    menuSection
                        .padding(.top, 24)

                    Spacer().frame(height: 100)
                }
            }
        }
        .sheet(isPresented: $showSettings) {
            SettingsView(onLogout: {
                showSettings = false
                viewModel.logout()
            })
        }
    }

    private var headerView: some View {
        HStack {
            Spacer()

            Button(action: { showSettings = true }) {
                Image(systemName: "gearshape.fill")
                    .font(.system(size: 20))
                    .foregroundColor(Color( "8B8BA7"))
            }
        }
        .padding(.horizontal, 16)
    }

    private var profileCard: some View {
        HStack(spacing: 16) {
            // Avatar
            ZStack {
                Circle()
                    .fill(Color( "374151"))
                    .frame(width: 72, height: 72)

                Image(systemName: "person.fill")
                    .font(.system(size: 32))
                    .foregroundColor(Color( "8B8BA7"))
            }

            // Info
            VStack(alignment: .leading, spacing: 6) {
                Text("未登录用户")
                    .font(.system(size: 18, weight: .bold))
                    .foregroundColor(.white)

                Text("ID: 888888")
                    .font(.system(size: 13))
                    .foregroundColor(Color( "8B8BA7"))

                HStack(spacing: 4) {
                    Image(systemName: "star.fill")
                        .font(.system(size: 12))
                        .foregroundColor(Color( "F59E0B"))
                    Text("Lv.5")
                        .font(.system(size: 12, weight: .medium))
                        .foregroundColor(Color( "F59E0B"))
                }
            }

            Spacer()

            // Edit button
            Button(action: {}) {
                Text("编辑资料")
                    .font(.system(size: 12, weight: .medium))
                    .foregroundColor(.white)
                    .padding(.horizontal, 12)
                    .padding(.vertical, 6)
                    .background(Color( "E879F9"))
                    .cornerRadius(12)
            }
        }
        .padding(16)
        .background(Color( "1F1F3A"))
        .cornerRadius(16)
    }

    private var statsSection: some View {
        HStack(spacing: 0) {
            StatItem(title: "关注", value: "128")
            StatItem(title: "粉丝", value: "1.2w")
            StatItem(title: "获赞", value: "3.5w")
            StatItem(title: "动态", value: "56")
        }
        .padding(.vertical, 16)
        .background(Color( "1F1F3A"))
        .cornerRadius(16)
    }

    private var menuSection: some View {
        VStack(spacing: 1) {
            MenuItemRow(icon: "heart.fill", iconColor: Color( "EF4444"), title: "我的收藏", showBadge: true, badge: 12)
            MenuItemRow(icon: "clock.fill", iconColor: Color( "3B82F6"), title: "浏览历史", showBadge: false)
            MenuItemRow(icon: "wallet.fill", iconColor: Color( "10B981"), title: "我的钱包", showBadge: false)
            MenuItemRow(icon: "star.fill", iconColor: Color( "F59E0B"), title: "我的等级", showBadge: false)
            MenuItemRow(icon: "shield.fill", iconColor: Color( "8B5CF6"), title: "隐私设置", showBadge: false)
            MenuItemRow(icon: "questionmark.circle.fill", iconColor: Color( "EC4899"), title: "帮助与反馈", showBadge: false)
        }
        .background(Color( "1F1F3A"))
        .cornerRadius(16)
        .padding(.horizontal, 12)
    }
}

// MARK: - Stat Item
private struct StatItem: View {
    let title: String
    let value: String

    var body: some View {
        VStack(spacing: 6) {
            Text(value)
                .font(.system(size: 18, weight: .bold))
                .foregroundColor(.white)

            Text(title)
                .font(.system(size: 12))
                .foregroundColor(Color( "8B8BA7"))
        }
        .frame(maxWidth: .infinity)
    }
}

// MARK: - Menu Item Row
private struct MenuItemRow: View {
    let icon: String
    let iconColor: Color
    let title: String
    let showBadge: Bool
    var badge: Int = 0

    var body: some View {
        HStack(spacing: 12) {
            // Icon
            ZStack {
                Circle()
                    .fill(iconColor.opacity(0.2))
                    .frame(width: 36, height: 36)

                Image(systemName: icon)
                    .font(.system(size: 16))
                    .foregroundColor(iconColor)
            }

            Text(title)
                .font(.system(size: 15))
                .foregroundColor(.white)

            Spacer()

            if showBadge && badge > 0 {
                Text("\(badge)")
                    .font(.system(size: 11, weight: .bold))
                    .foregroundColor(.white)
                    .frame(width: 20, height: 20)
                    .background(Color( "EF4444"))
                    .clipShape(Circle())
            }

            Image(systemName: "chevron.right")
                .font(.system(size: 14))
                .foregroundColor(Color( "8B8BA7"))
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 14)
        .background(Color( "1F1F3A"))
        .contentShape(Rectangle())
    }
}

// MARK: - Settings View
struct SettingsView: View {
    @Environment(\.dismiss) var dismiss
    var onLogout: () -> Void

    var body: some View {
        NavigationView {
            ZStack {
                Color( "0F0F23")
                    .ignoresSafeArea()

                ScrollView(.vertical, showsIndicators: false) {
                    VStack(spacing: 24) {
                        // Account section
                        settingsSection(title: "账号设置") {
                            SettingsItemRow(icon: "person.fill", title: "账号与安全")
                            SettingsItemRow(icon: "bell.fill", title: "通知设置")
                            SettingsItemRow(icon: "lock.fill", title: "隐私政策")
                        }

                        // General section
                        settingsSection(title: "通用设置") {
                            SettingsItemRow(icon: "paintbrush.fill", title: "主题设置")
                            SettingsItemRow(icon: "globe", title: "语言设置")
                            SettingsItemRow(icon: "storage.fill", title: "清理缓存")
                        }

                        // About section
                        settingsSection(title: "关于") {
                            SettingsItemRow(icon: "info.circle.fill", title: "关于我们")
                            SettingsItemRow(icon: "doc.text.fill", title: "用户协议")
                        }

                        // Logout button
                        Button(action: {
                            onLogout()
                        }) {
                            Text("退出登录")
                                .font(.system(size: 16, weight: .medium))
                                .foregroundColor(Color( "EF4444"))
                                .frame(maxWidth: .infinity)
                                .frame(height: 50)
                                .background(Color( "1F1F3A"))
                                .cornerRadius(12)
                        }
                        .padding(.horizontal, 12)
                        .padding(.top, 24)

                        Spacer().frame(height: 40)
                    }
                    .padding(.top, 16)
                }
            }
            .navigationTitle("设置")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: { dismiss() }) {
                        Image(systemName: "xmark")
                            .font(.system(size: 16, weight: .medium))
                            .foregroundColor(.white)
                    }
                }
            }
        }
    }

    private func settingsSection(title: String, @ViewBuilder content: () -> some View) -> some View {
        VStack(alignment: .leading, spacing: 0) {
            Text(title)
                .font(.system(size: 13))
                .foregroundColor(Color( "8B8BA7"))
                .padding(.horizontal, 16)
                .padding(.bottom, 8)

            VStack(spacing: 1) {
                content()
            }
            .background(Color( "1F1F3A"))
            .cornerRadius(12)
            .padding(.horizontal, 12)
        }
    }
}

private struct SettingsItemRow: View {
    let icon: String
    let title: String

    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: icon)
                .font(.system(size: 16))
                .foregroundColor(Color( "E879F9"))
                .frame(width: 24)

            Text(title)
                .font(.system(size: 15))
                .foregroundColor(.white)

            Spacer()

            Image(systemName: "chevron.right")
                .font(.system(size: 14))
                .foregroundColor(Color( "8B8BA7"))
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 14)
        .background(Color( "1F1F3A"))
    }
}

