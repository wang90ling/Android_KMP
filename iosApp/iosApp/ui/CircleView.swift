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

struct CircleView: View {
    @State private var selectedSegment: Int = 0
    private let segments = ["推荐", "最新", "关注"]

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
                ScrollView(.vertical, showsIndicators: false) {
                    LazyVStack(spacing: 16) {
                        ForEach(0..<5, id: \.self) { index in
                            PostCard(post: mockPosts[index])
                        }
                        Spacer().frame(height: 80)
                    }
                    .padding(.horizontal, 12)
                    .padding(.top, 16)
                }
            }
        }
    }

    private var headerView: some View {
        HStack {
            Text("圈子")
                .font(.system(size: 20, weight: .bold))
                .foregroundColor(.white)

            Spacer()

            Button(action: {}) {
                Image(systemName: "magnifyingglass")
                    .font(.system(size: 20))
                    .foregroundColor(Color( "8B8BA7"))
            }

            Button(action: {}) {
                Image(systemName: "plus")
                    .font(.system(size: 20))
                    .foregroundColor(Color( "8B8BA7"))
            }
            .padding(.leading, 16)
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
}

// MARK: - Post Model
private struct Post: Identifiable {
    let id: Int
    let username: String
    let avatar: String
    let time: String
    let content: String
    let images: [String]
    let likeCount: Int
    let commentCount: Int
    let isLiked: Bool
}

private let mockPosts: [Post] = [
    Post(id: 1, username: "小红", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cute%20anime%20girl%20avatar%20red%20hair&image_size=square", time: "2小时前", content: "今天天气真好呀~出门逛街啦！", images: ["https://picsum.photos/300/300?random=1"], likeCount: 128, commentCount: 23, isLiked: false),
    Post(id: 2, username: "小明", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cute%20anime%20boy%20avatar%20blue%20hair&image_size=square", time: "3小时前", content: "新买的手办到了，太开心了！", images: ["https://picsum.photos/300/400?random=2"], likeCount: 256, commentCount: 45, isLiked: true),
    Post(id: 3, username: "小美", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cute%20anime%20girl%20avatar%20pink%20hair&image_size=square", time: "5小时前", content: "今天学会了做蛋糕，第一次尝试还不错哦！", images: ["https://picsum.photos/400/300?random=3"], likeCount: 89, commentCount: 12, isLiked: false),
    Post(id: 4, username: "阿杰", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=cool%20anime%20boy%20avatar%20black%20hair&image_size=square", time: "6小时前", content: "游戏终于上王者了，太不容易了！", images: ["https://picsum.photos/350/350?random=4"], likeCount: 512, commentCount: 67, isLiked: true),
    Post(id: 5, username: "小雪", avatar: "https://neeko-copilot.bytedance.net/api/text_to_image?prompt=elegant%20anime%20girl%20avatar%20white%20hair&image_size=square", time: "8小时前", content: "周末要去爬山，希望天气好！", images: ["https://picsum.photos/400/300?random=5"], likeCount: 178, commentCount: 34, isLiked: false)
]

private struct PostCard: View {
    let post: Post
    @State private var isLiked: Bool
    @State private var likeCount: Int

    init(post: Post) {
        self.post = post
        _isLiked = State(initialValue: post.isLiked)
        _likeCount = State(initialValue: post.likeCount)
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Header
            HStack(spacing: 12) {
                AsyncImage(url: URL(string: post.avatar)) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                } placeholder: {
                    Circle()
                        .fill(Color( "374151"))
                }
                .frame(width: 44, height: 44)
                .clipShape(Circle())

                VStack(alignment: .leading, spacing: 4) {
                    Text(post.username)
                        .font(.system(size: 15, weight: .semibold))
                        .foregroundColor(.white)

                    Text(post.time)
                        .font(.system(size: 12))
                        .foregroundColor(Color( "8B8BA7"))
                }

                Spacer()

                Button(action: {}) {
                    Text("+ 关注")
                        .font(.system(size: 12, weight: .medium))
                        .foregroundColor(.white)
                        .padding(.horizontal, 12)
                        .padding(.vertical, 6)
                        .background(Color( "E879F9"))
                        .cornerRadius(12)
                }
            }

            // Content
            Text(post.content)
                .font(.system(size: 14))
                .foregroundColor(.white)
                .lineLimit(5)

            // Images
            if !post.images.isEmpty {
                LazyVGrid(columns: [
                    GridItem(.flexible(), spacing: 4),
                    GridItem(.flexible(), spacing: 4),
                    GridItem(.flexible(), spacing: 4)
                ], spacing: 4) {
                    ForEach(post.images, id: \.self) { imageUrl in
                        AsyncImage(url: URL(string: imageUrl)) { image in
                            image
                                .resizable()
                                .aspectRatio(contentMode: .fill)
                        } placeholder: {
                            RoundedRectangle(cornerRadius: 8)
                                .fill(Color( "374151"))
                        }
                        .frame(height: 100)
                        .clipped()
                        .cornerRadius(8)
                    }
                }
            }

            // Actions
            HStack(spacing: 32) {
                Button(action: {
                    isLiked.toggle()
                    likeCount += isLiked ? 1 : -1
                }) {
                    HStack(spacing: 4) {
                        Image(systemName: isLiked ? "heart.fill" : "heart")
                            .foregroundColor(isLiked ? Color( "EF4444") : Color( "8B8BA7"))
                        Text("\(likeCount)")
                            .foregroundColor(Color( "8B8BA7"))
                    }
                }

                Button(action: {}) {
                    HStack(spacing: 4) {
                        Image(systemName: "bubble.right")
                            .foregroundColor(Color( "8B8BA7"))
                        Text("\(post.commentCount)")
                            .foregroundColor(Color( "8B8BA7"))
                    }
                }

                Button(action: {}) {
                    Image(systemName: "square.and.arrow.up")
                        .foregroundColor(Color( "8B8BA7"))
                }

                Spacer()
            }
        }
        .padding(12)
        .background(Color( "1F1F3A"))
        .cornerRadius(16)
    }
}

