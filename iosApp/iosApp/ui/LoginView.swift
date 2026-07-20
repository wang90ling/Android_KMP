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

struct LoginView: View {
    @EnvironmentObject var viewModelStoreOwner: IOSViewModelStoreOwner
    @EnvironmentObject var appContainer: ObservableValueWrapper<AppContainer>

    @State private var phoneNumber: String = ""
    @State private var verificationCode: String = ""
    @State private var isChecked: Bool = false
    @State private var countdown: Int = 0
    @State private var errorMessage: String?
    @State private var showLoading: Bool = false
    @State private var showErrorDialog: Bool = false

    @State private var timer: Timer?

    private var viewModel: LoginViewModel {
        viewModelStoreOwner.viewModel(
            factory: appContainer.value.loginViewModelFactory
        )
    }

    var body: some View {
        ZStack {
            Color( "FDFBFF")
                .ignoresSafeArea()

            VStack(spacing: 0) {
                // Top bar
                HStack {
                    Image(systemName: "chevron.left")
                        .font(.system(size: 20, weight: .medium))
                        .foregroundColor(Color( "1F1F3A"))
                        .frame(width: 44, height: 44)

                    Spacer()

                    Button(action: {}) {
                        Text("登录遇到问题？")
                            .font(.system(size: 14))
                            .foregroundColor(Color( "8B8BA7"))
                    }
                }
                .padding(.horizontal, 8)
                .padding(.top, 12)

                Spacer().frame(height: 48)

                // Welcome title
                Text("HI~欢迎登录")
                    .font(.system(size: 28, weight: .bold))
                    .foregroundColor(Color( "1F1F3A"))
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal, 32)

                Spacer().frame(height: 48)

                // Input fields
                VStack(spacing: 16) {
                    // Phone number input
                    HStack(spacing: 0) {
                        Text("+86")
                            .font(.system(size: 14))
                            .foregroundColor(Color( "1F1F3A"))
                        Image(systemName: "chevron.down")
                            .font(.system(size: 12))
                            .foregroundColor(Color( "8B8BA7"))
                    }
                    .padding(.leading, 16)
                    .frame(width: 60, alignment: .leading)

                    TextField("请输入手机号", text: $phoneNumber)
                        .keyboardType(.phonePad)
                        .font(.system(size: 14))
                        .onChange(of: phoneNumber) { _, newValue in
                            if newValue.count > 11 {
                                phoneNumber = String(newValue.prefix(11))
                            }
                        }
                }
                .padding(.horizontal, 18)
                .frame(height: 52)
                .background(Color.white)
                .cornerRadius(26)
                .overlay(
                    RoundedRectangle(cornerRadius: 26)
                        .stroke(Color( "D1D5DB"), lineWidth: 1)
                )

                Spacer().frame(height: 16)

                // Verification code input
                HStack(spacing: 0) {
                    Image(systemName: "qrcode")
                        .font(.system(size: 18))
                        .foregroundColor(Color( "8B8BA7"))
                        .padding(.leading, 12)

                    TextField("请输入验证码", text: $verificationCode)
                        .keyboardType(.numberPad)
                        .font(.system(size: 14))
                        .onChange(of: verificationCode) { _, newValue in
                            if newValue.count > 4 {
                                verificationCode = String(newValue.prefix(4))
                            }
                        }

                    Spacer()

                    Button(action: {
                        if countdown == 0 && phoneNumber.count == 11 {
                            sendVerificationCode()
                        }
                    }) {
                        Text(countdown > 0 ? "\(countdown)s后获取" : "获取验证码")
                            .font(.system(size: 14))
                            .foregroundColor(
                                countdown == 0 && phoneNumber.count == 11
                                    ? Color( "9333EA")
                                    : Color( "9333EA").opacity(0.5)
                            )
                    }
                    .padding(.trailing, 8)
                    .disabled(countdown > 0 || phoneNumber.count != 11)
                }
                .frame(height: 52)
                .background(Color.white)
                .cornerRadius(26)
                .overlay(
                    RoundedRectangle(cornerRadius: 26)
                        .stroke(Color( "D1D5DB"), lineWidth: 1)
                )

                Spacer().frame(height: 16)

                // Agreement checkbox
                HStack(alignment: .center, spacing: 4) {
                    Button(action: { isChecked.toggle() }) {
                        Image(systemName: isChecked ? "checkmark.square.fill" : "square")
                            .foregroundColor(isChecked ? Color( "9333EA") : Color( "D1D5DB"))
                    }
                    .buttonStyle(PlainButtonStyle())

                    Text("我已阅读并同意")
                        .font(.system(size: 12))
                        .foregroundColor(Color( "8B8BA7"))

                    Text("用户协议")
                        .font(.system(size: 12))
                        .foregroundColor(Color( "9333EA"))

                    Text("和")
                        .font(.system(size: 12))
                        .foregroundColor(Color( "8B8BA7"))

                    Text("隐私政策")
                        .font(.system(size: 12))
                        .foregroundColor(Color( "9333EA"))
                }
                .padding(.leading, 8)

                Spacer().frame(height: 32)

                // Login button
                Button(action: {
                    performLogin()
                }) {
                    Text("登录")
                        .font(.system(size: 16, weight: .medium))
                        .foregroundColor(loginButtonEnabled ? .white : Color( "9CA3AF"))
                        .frame(maxWidth: .infinity)
                        .frame(height: 56)
                        .background(
                            loginButtonEnabled
                                ? AnyShapeStyle(LinearGradient(
                                    colors: [Color( "3B82F6"), Color( "9333EA"), Color( "EC4899")],
                                    startPoint: .leading,
                                    endPoint: .trailing
                                ))
                                : AnyShapeStyle(Color( "E5E7EB"))
                        )
                        .cornerRadius(28)
                }
                .disabled(!loginButtonEnabled)

                Spacer().frame(height: 24)

                // Account login button
                Button(action: {}) {
                    Text("账号登录")
                        .font(.system(size: 14))
                        .foregroundColor(Color( "8B8BA7"))
                }

                Spacer()

                // Other login methods
                VStack(spacing: 16) {
                    Text("其他登录方式")
                        .font(.system(size: 12))
                        .foregroundColor(Color( "8B8BA7"))

                    HStack(spacing: 48) {
                        // QQ Login
                        Button(action: {}) {
                            ZStack {
                                Circle()
                                    .fill(Color.white)
                                    .frame(width: 48, height: 48)
                                Image(systemName: "qrcode")
                                    .font(.system(size: 24))
                                    .foregroundColor(Color( "1B9AF7"))
                            }
                        }

                        // WeChat Login
                        Button(action: {}) {
                            ZStack {
                                Circle()
                                    .fill(Color.white)
                                    .frame(width: 48, height: 48)
                                Image(systemName: "message.fill")
                                    .font(.system(size: 24))
                                    .foregroundColor(Color( "07C160"))
                            }
                        }
                    }
                }
                .padding(.bottom, 40)
            }
        }
        .overlay {
            if showLoading {
                ZStack {
                    Color.black.opacity(0.3)
                        .ignoresSafeArea()
                    VStack(spacing: 16) {
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: Color( "9333EA")))
                        Text("登录中...")
                            .font(.system(size: 14))
                            .foregroundColor(Color( "9333EA"))
                    }
                    .padding(24)
                    .background(Color.white)
                    .cornerRadius(12)
                }
            }
        }
        .alert("提示", isPresented: $showErrorDialog) {
            Button("确定") {
                errorMessage = nil
            }
        } message: {
            Text(errorMessage ?? "")
        }
        .onAppear {
            startPolling()
        }
        .onDisappear {
            stopPolling()
        }
    }

    private var loginButtonEnabled: Bool {
        isChecked && phoneNumber.count == 11 && verificationCode.count == 4
    }

    private func startPolling() {
        // Poll Kotlin StateFlow values periodically
        timer = Timer.scheduledTimer(withTimeInterval: 0.5, repeats: true) { _ in
            pollState()
        }
    }

    private func stopPolling() {
        timer?.invalidate()
        timer = nil
    }

    private func pollState() {
        // Get current values from Kotlin StateFlow
        let currentCountdown = viewModel.countdown.value
        let currentState = viewModel.loginUiState.value

        // Update countdown
        if countdown != Int(currentCountdown) {
            countdown = Int(currentCountdown)
        }

        // Handle login state
        handleLoginState(currentState)
    }

    private func handleLoginState(_ state: LoginUiStateV2) {
        let stateType = String(describing: type(of: state))

        if stateType.contains("Loading") {
            showLoading = true
        } else if stateType.contains("Success") {
            showLoading = false
        } else if stateType.contains("Error") {
            showLoading = false
            if let errorState = state as? LoginUiStateV2.Error {
                errorMessage = errorState.message
            } else {
                errorMessage = "登录失败，请重试"
            }
            showErrorDialog = true
        } else {
            // Idle state - do nothing
        }
    }

    private func sendVerificationCode() {
        viewModel.sendSmsCode(smsType: SmsType.login)
    }

    private func performLogin() {
        guard loginButtonEnabled else { return }

        showLoading = true
        let req = SmsLoginRequest(
            telephone: phoneNumber,
            code: verificationCode,
            phoneCountryCode: "+86",
            password: nil
        )
        viewModel.smsLogin(req: req)
    }
}
