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

import Foundation
import SwiftUI

final class LoginStateManager: ObservableObject {
    static let shared = LoginStateManager()

    private let IS_LOGGED_IN_KEY = "isLoggedIn"
    private let TOKEN_KEY = "authToken"
    private let REFRESH_TOKEN_KEY = "refreshToken"
    private let USER_INFO_KEY = "userInfo"

    private let defaults = UserDefaults.standard

    @Published var isLoggedIn: Bool = false

    private init() {
        isLoggedIn = defaults.bool(forKey: IS_LOGGED_IN_KEY)
    }

    var authToken: String? {
        get { defaults.string(forKey: TOKEN_KEY) }
        set { defaults.set(newValue, forKey: TOKEN_KEY) }
    }

    var refreshToken: String? {
        get { defaults.string(forKey: REFRESH_TOKEN_KEY) }
        set { defaults.set(newValue, forKey: REFRESH_TOKEN_KEY) }
    }

    func login() {
        defaults.set(true, forKey: IS_LOGGED_IN_KEY)
        DispatchQueue.main.async { [weak self] in
            self?.isLoggedIn = true
        }
        NotificationCenter.default.post(name: Notification.Name("LoginStateChanged"), object: nil)
    }

    func logout() {
        defaults.set(false, forKey: IS_LOGGED_IN_KEY)
        authToken = nil
        refreshToken = nil
        defaults.removeObject(forKey: USER_INFO_KEY)
        DispatchQueue.main.async { [weak self] in
            self?.isLoggedIn = false
        }
        NotificationCenter.default.post(name: Notification.Name("LoginStateChanged"), object: nil)
    }

    func saveAuthData(token: String, refreshToken: String?) {
        authToken = token
        self.refreshToken = refreshToken
        login()
    }
}
