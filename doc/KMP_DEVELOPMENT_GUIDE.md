# Fruitties Kotlin Multiplatform 项目开发指南

> 本文档旨在帮助首次接触 Kotlin Multiplatform (KMP) 的开发者快速理解项目结构，掌握开发技能。

---

## 目录

1. [项目概览](#1-项目概览)
2. [KMP 核心概念](#2-kmp-核心概念)
3. [项目架构](#3-项目架构)
4. [模块详解](#4-模块详解)
5. [核心组件开发指南](#5-核心组件开发指南)
6. [平台特定代码 (expect/actual)](#6-平台特定代码-expectactual)
7. [数据层详解](#7-数据层详解)
8. [UI 层开发](#8-ui-层开发)
9. [依赖注入](#9-依赖注入)
10. [常见开发任务](#10-常见开发任务)
11. [开发环境配置](#11-开发环境配置)
12. [运行与调试](#12-运行与调试)

---

## 1. 项目概览

### 1.1 项目简介

**Fruitties** 是一个 Kotlin Multiplatform 示例应用，展示了如何使用 KMP 同时开发 Android (Jetpack Compose) 和 iOS (SwiftUI) 应用。

### 1.2 技术栈

| 分类 | 技术 | 用途 |
|------|------|------|
| **跨平台框架** | Kotlin Multiplatform | 共享业务逻辑代码 |
| **Android UI** | Jetpack Compose | Android 原生界面 |
| **iOS UI** | SwiftUI | iOS 原生界面 |
| **网络层** | Ktor Client | HTTP 请求 |
| **本地数据库** | Room | SQLite 持久化 |
| **配置存储** | DataStore | 购物车状态 |
| **序列化** | Kotlinx Serialization | JSON 编解码 |
| **异步** | Kotlin Coroutines + Flow | 响应式编程 |
| **状态管理** | ViewModel + StateFlow | UI 状态管理 |
| **iOS 集成** | Skie | Kotlin Flow → SwiftUI 桥接 |

### 1.3 项目结构

```
Fruitties/
├── androidApp/           # Android 应用模块 (Jetpack Compose)
├── iosApp/              # iOS 应用模块 (SwiftUI)
├── shared/              # 共享 Kotlin 代码模块 ⭐
├── build.gradle.kts     # 根构建配置
├── settings.gradle.kts  # 项目设置
└── gradle/libs.versions.toml  # 版本目录 (统一管理依赖版本)
```

---

## 2. KMP 核心概念

### 2.1 什么是 Kotlin Multiplatform (KMP)

KMP 允许你用 Kotlin 编写跨平台代码，编译成：
- **Android**: JVM 字节码 (AAR 库)
- **iOS**: Native Framework (`.framework`)

### 2.2 源码集 (Source Sets)

KMP 通过源码集区分不同平台的代码：

```
shared/src/
├── commonMain/kotlin/    # 所有平台共享代码 ⭐
├── androidMain/kotlin/   # Android 专用代码
├── iosMain/kotlin/       # iOS 专用代码 (Kotlin/Native)
├── commonTest/kotlin/    # 共享测试代码
└── androidDeviceTest/    # Android 设备测试
```

### 2.3 expect/actual 机制

KMP 使用 `expect` 声明接口，`actual` 提供各平台实现：

```kotlin
// commonMain - expect 声明
expect class Factory {
    fun createRoomDatabase(): AppDatabase
    fun createCartDataStore(): CartDataStore
}

// androidMain - Android 实现
actual class Factory(private val app: Application) {
    actual fun createRoomDatabase() = Room.databaseBuilder(...)
    actual fun createCartDataStore() = CartDataStore { app.filesDir.resolve(...) }
}

// iosMain - iOS 实现
actual class Factory {
    actual fun createRoomDatabase() = Room.databaseBuilder(...)
    actual fun createCartDataStore() = CartDataStore { "${fileDirectory()}/cart.json" }
}
```

---

## 3. 项目架构

### 3.1 整体架构 (MVVM + Clean Architecture)

```
┌─────────────────────────────────────────────────────────────────┐
│                          UI Layer                                │
├─────────────────────────────────────────────────────────────────┤
│  Android: Jetpack Compose          iOS: SwiftUI                 │
│  - ListScreen.kt                  - ContentView.swift          │
│  - FruittieScreen.kt              - FruittieScreen.swift       │
│  - CartScreen.kt                  - CartView.swift              │
└─────────────────────────────────────────────────────────────────┘
                              ↓ ViewModel
┌─────────────────────────────────────────────────────────────────┐
│                      ViewModel Layer (shared)                    │
│  - MainViewModel.kt           - CartViewModel.kt                │
│  - FruittieViewModel.kt       - HomeUiState.kt                  │
└─────────────────────────────────────────────────────────────────┘
                              ↓ Repository
┌─────────────────────────────────────────────────────────────────┐
│                     Data Layer (shared)                         │
│  DataRepository ←→ FruittieApi (Network) + AppDatabase + DataStore│
└─────────────────────────────────────────────────────────────────┘
```

### 3.2 数据流向

```
API 请求 ←→ FruittieNetworkApi
                    ↓
            DataRepository (单一数据源)
                    ↓
    ┌───────────────┼───────────────┐
    ↓               ↓               ↓
 Room DB      CartDataStore    其他数据源
 (本地水果列表)  (购物车状态)
    ↓               ↓
    └───────────────┼───────────────┘
                    ↓
            ViewModel (StateFlow)
                    ↓
               UI 更新
```

---

## 4. 模块详解

### 4.1 shared 模块

#### 4.1.1 包结构

```
shared/src/commonMain/kotlin/com/example/fruitties/
├── model/           # 数据模型
│   ├── Fruittie.kt         # 水果实体
│   └── FruittiesResponse.kt
├── network/         # 网络层
│   └── FruittieApi.kt     # API 接口与实现
├── database/        # 数据库层
│   ├── AppDatabase.kt      # Room 数据库
│   ├── FruittieDao.kt      # 数据访问对象
│   └── CartDataStore.kt    # 购物车存储
├── di/              # 依赖注入
│   ├── Factory.kt          # expect 声明
│   ├── Factory.android.kt  # Android 实现
│   ├── Factory.native.kt   # iOS 实现
│   └── AppContainer.kt     # DI 容器
└── viewmodel/       # 视图模型
    ├── MainViewModel.kt
    ├── CartViewModel.kt
    ├── FruittieViewModel.kt
    └── ViewModelFactory.kt
```

#### 4.1.2 shared/build.gradle.kts 关键配置

```kotlin
kotlin {
    // Android Library 目标
    androidLibrary {
        namespace = "com.example.fruitties"
        compileSdk = 36
        minSdk = 26
    }

    // iOS 目标
    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "shared"  // 生成 shared.framework
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                // 跨平台依赖
                implementation(libs.ktor.client.core)
                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.datastore.preferences.core)
                // ...
            }
        }
        androidMain {
            dependencies {
                // Android 专用依赖
                implementation(libs.ktor.client.okhttp)
            }
        }
        iosMain {
            dependencies {
                // iOS 专用依赖
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}
```

### 4.2 androidApp 模块

```
androidApp/
├── build.gradle.kts
└── src/main/java/com/example/fruitties/android/
    ├── MainActivity.kt            # 入口 Activity
    ├── FruittiesAndroidApp.kt    # Application 类
    └── ui/
        ├── ListScreen.kt          # 水果列表
        ├── FruittieScreen.kt      # 水果详情
        ├── CartScreen.kt          # 购物车
        └── FruittiesTheme.kt      # Compose 主题
```

### 4.3 iosApp 模块

```
iosApp/iosApp/
├── iOSApp.swift                   # SwiftUI App 入口
├── ObservableValueWrapper.swift   # Flow → SwiftUI 桥接
├── IOSViewModelStoreOwner.swift   # ViewModel 生命周期管理
├── ViewModelStoreOwnerProvider.swift
└── ui/
    ├── ContentView.swift           # 主界面
    ├── FruittieScreen.swift        # 水果详情
    └── CartView.swift             # 购物车
```

---

## 5. 核心组件开发指南

### 5.1 数据模型 (Model)

水果实体同时用于 Room 数据库和网络 API：

```kotlin
// commonMain/kotlin/com/example/fruitties/model/Fruittie.kt

@Serializable      // Kotlinx Serialization - 网络 JSON
@Entity             // Room 数据库注解
data class Fruittie(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @SerialName("name")         // JSON 字段映射
    val name: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("calories")
    val calories: String,
)

// 购物车项 (仅内存使用，不需要 Room 注解)
data class CartItemDetails(
    val fruittie: Fruittie,
    val count: Int,
)
```

**开发要点**：
- `@Serializable` 用于网络 JSON 序列化
- `@Entity` 用于 Room 数据库
- `@SerialName` 处理 JSON 字段名与 Kotlin 属性名不一致

### 5.2 网络层 (Network)

```kotlin
// commonMain/kotlin/com/example/fruitties/network/FruittieApi.kt

interface FruittieApi {
    suspend fun getData(pageNumber: Int = 0): FruittiesResponse
}

class FruittieNetworkApi(
    private val client: HttpClient,
    private val apiUrl: String,
) : FruittieApi {
    override suspend fun getData(pageNumber: Int): FruittiesResponse {
        val url = "$apiUrl/$pageNumber.json"
        return client.get(url).body()
    }
}
```

**HTTP Client 配置** (在 `AppContainer.kt` 中)：

```kotlin
// commonMain/kotlin/com/example/fruitties/di/AppContainer.kt

internal fun commonCreateApi(): FruittieApi =
    FruittieNetworkApi(
        client = HttpClient {
            install(ContentNegotiation) {
                json(json, contentType = ContentType.Any)
            }
        },
        apiUrl = "https://android.github.io/kotlin-multiplatform-samples/fruitties-api",
    )
```

**平台特定 HTTP 引擎**：

| 平台 | 依赖 | 引擎 |
|------|------|------|
| Android | `ktor-client-okhttp` | OkHttp |
| iOS | `ktor-client-darwin` | Darwin (NSURLSession) |

### 5.3 数据库层 (Database)

#### Room 数据库

```kotlin
// commonMain/kotlin/com/example/fruitties/database/AppDatabase.kt

@Database(entities = [Fruittie::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fruittieDao(): FruittieDao
}

// expect - Room 构造函数
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
```

#### DAO (Data Access Object)

```kotlin
// commonMain/kotlin/com/example/fruitties/database/FruittieDao.kt

@Dao
interface FruittieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fruittie: Fruittie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fruitties: List<Fruittie>)

    @Query("SELECT * FROM Fruittie")
    fun getAllAsFlow(): Flow<List<Fruittie>>  // 返回 Flow 实现响应式查询

    @Query("SELECT * FROM Fruittie WHERE id = :id")
    suspend fun getFruittie(id: Long): Fruittie?

    @Query("SELECT * FROM Fruittie WHERE id in (:ids)")
    suspend fun loadMapped(ids: List<Long>): Map<Long, Fruittie>
}
```

#### DataStore (购物车持久化)

```kotlin
// commonMain/kotlin/com/example/fruitties/database/CartDataStore.kt

@Serializable
data class Cart(
    val items: List<CartItem>,
)

@Serializable
data class CartItem(
    val id: Long,
    val count: Int,
)

class CartDataStore(
    private val produceFilePath: () -> String,  // 平台特定路径
) {
    private val db = DataStoreFactory.create(
        storage = OkioStorage<Cart>(
            fileSystem = FileSystem.SYSTEM,
            serializer = CartJsonSerializer,
            producePath = { produceFilePath().toPath() },
        ),
    )

    val cart: Flow<Cart>
        get() = db.data

    suspend fun add(fruittie: Fruittie) = update(fruittie, 1)
    suspend fun remove(fruittie: Fruittie) = update(fruittie, -1)
}
```

### 5.4 ViewModel

```kotlin
// commonMain/kotlin/com/example/fruitties/viewmodel/MainViewModel.kt

class MainViewModel(
    private val repository: DataRepository,
) : ViewModel() {
    // UI 状态 - StateFlow 实现响应式更新
    val homeUiState: StateFlow<HomeUiState> =
        repository.getData()
            .combine(repository.cartDetails) { fruitties, cartState ->
                HomeUiState(
                    fruitties = fruitties,
                    cartItemCount = cartState.sumOf { item -> item.count },
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState(),
            )

    fun addItemToCart(fruittie: Fruittie) {
        viewModelScope.launch {
            repository.addToCart(fruittie)
        }
    }
}

// UI 状态数据类
data class HomeUiState(
    val fruitties: List<Fruittie> = listOf(),
    val cartItemCount: Int = 0,
)
```

### 5.5 Repository

```kotlin
// commonMain/kotlin/com/example/fruitties/DataRepository.kt

class DataRepository(
    private val api: FruittieApi,
    private var database: AppDatabase,
    private val cartDataStore: CartDataStore,
    private val scope: CoroutineScope,
) {
    // Flow 组合实现复杂业务逻辑
    @OptIn(ExperimentalCoroutinesApi::class)
    val cartDetails: Flow<List<CartItemDetails>>
        get() = cartDataStore.cart.mapLatest { cart ->
            val ids = cart.items.map { it.id }
            val fruitties = database.fruittieDao().loadMapped(ids)
            cart.items.mapNotNull { item ->
                fruitties[item.id]?.let { fruittie ->
                    CartItemDetails(fruittie, item.count)
                }
            }
        }

    suspend fun addToCart(fruittie: Fruittie) = cartDataStore.add(fruittie)

    fun getData(): Flow<List<Fruittie>> {
        scope.launch {
            if (database.fruittieDao().count() < 1) {
                refreshData()  // 首次加载从网络获取
            }
        }
        return loadData()  // 返回本地数据库 Flow
    }

    private suspend fun refreshData() {
        val response = api.getData()
        database.fruittieDao().insert(response.feed)
    }
}
```

---

## 6. 平台特定代码 (expect/actual)

### 6.1 Factory 类

**expect 声明** (`commonMain`):

```kotlin
// shared/src/commonMain/kotlin/com/example/fruitties/di/Factory.kt

expect class Factory {
    fun createRoomDatabase(): AppDatabase
    fun createCartDataStore(): CartDataStore
}
```

**Android 实现** (`androidMain`):

```kotlin
// shared/src/androidMain/kotlin/com/example/fruitties/di/Factory.android.kt

actual class Factory(
    private val app: Application,
) {
    actual fun createRoomDatabase(): AppDatabase {
        val dbFile = app.getDatabasePath(DB_FILE_NAME)
        return Room
            .databaseBuilder<AppDatabase>(
                context = app,
                name = dbFile.absolutePath,
            ).setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    actual fun createCartDataStore(): CartDataStore =
        CartDataStore {
            app.filesDir.resolve("cart.json").absolutePath
        }
}
```

**iOS 实现** (`iosMain`):

```kotlin
// shared/src/iosMain/kotlin/com/example/fruitties/di/Factory.native.kt

actual class Factory {
    actual fun createRoomDatabase(): AppDatabase {
        val dbFile = "${fileDirectory()}/$DB_FILE_NAME"
        return Room
            .databaseBuilder<AppDatabase>(name = dbFile)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    actual fun createCartDataStore(): CartDataStore =
        CartDataStore { "${fileDirectory()}/cart.json" }

    @OptIn(ExperimentalForeignApi::class)
    private fun fileDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory).path!!
    }
}
```

---

## 7. 数据层详解

### 7.1 统一配置

```kotlin
// shared/src/commonMain/kotlin/com/example/fruitties/di/AppContainer.kt

val json = Json { ignoreUnknownKeys = true }

class AppContainer(
    private val factory: Factory,
) {
    val dataRepository: DataRepository by lazy {
        DataRepository(
            api = commonCreateApi(),
            database = factory.createRoomDatabase(),
            cartDataStore = factory.createCartDataStore(),
            scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
        )
    }

    // ViewModel 工厂
    val mainViewModelFactory = viewModelFactory {
        initializer { MainViewModel(repository = dataRepository) }
    }

    val cartViewModelFactory = viewModelFactory {
        initializer { CartViewModel(repository = dataRepository) }
    }

    val fruittieViewModelFactory = viewModelFactory {
        initializer {
            FruittieViewModel(
                fruittieId = this[FRUITTIE_ID_KEY] ?: error("Expected fruittieId!"),
                repository = dataRepository,
            )
        }
    }

    private fun commonCreateApi() = FruittieNetworkApi(
        client = HttpClient {
            install(ContentNegotiation) {
                json(json, contentType = ContentType.Any)
            }
        },
        apiUrl = "https://api.example.com",
    )
}
```

### 7.2 KSP 配置 (Room 编译器)

```kotlin
// shared/build.gradle.kts

dependencies {
    // Room 编译器
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}
```

---

## 8. UI 层开发

### 8.1 Android (Jetpack Compose)

#### Application 类设置 DI

```kotlin
// androidApp/.../FruittiesAndroidApp.kt

class FruittiesAndroidApp : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(Factory(this))
    }
}

// Composition Local 供 Compose 使用
val LocalAppContainer = staticCompositionLocalOf<AppContainer> {
    error("No AppContainer provided!")
}
```

#### Composable 中使用 ViewModel

```kotlin
// androidApp/.../ui/ListScreen.kt

@Composable
fun ListScreen(
    onClickViewCart: () -> Unit,
    onFruittieClick: (Fruittie) -> Unit,
    viewModel: MainViewModel = viewModel(
        factory = LocalAppContainer.current.mainViewModelFactory,
    ),
) {
    val uiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        topBar = {
            LargeTopAppBar(title = { Text("Fruitties") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onClickViewCart) {
                Row {
                    Icon(Icons.Filled.ShoppingCart, null)
                    Text("View Cart (${uiState.cartItemCount})")
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(items = uiState.fruitties, key = { it.id }) { item ->
                FruittieItem(
                    item = item,
                    onClick = onFruittieClick,
                    onAddToCart = viewModel::addItemToCart,
                )
            }
        }
    }
}
```

### 8.2 iOS (SwiftUI)

#### iOS App 入口

```swift
// iosApp/iosApp/iOSApp.swift

@main
struct iOSApp: App {
    let appContainer: ObservableValueWrapper<AppContainer>

    init() {
        self.appContainer = ObservableValueWrapper<AppContainer>(
            value: AppContainer(factory: Factory())
        )
    }

    var body: some Scene {
        WindowGroup {
            ViewModelStoreOwnerProvider {
                ContentView()
            }
            .environmentObject(appContainer)
        }
    }
}
```

#### SwiftUI 中使用 Kotlin ViewModel

```swift
// iosApp/iosApp/ui/ContentView.swift

struct ContentView: View {
    @EnvironmentObject var viewModelStoreOwner: IOSViewModelStoreOwner
    @EnvironmentObject var appContainer: ObservableValueWrapper<AppContainer>

    var body: some View {
        let mainViewModel: MainViewModel = viewModelStoreOwner.viewModel(
            factory: appContainer.value.mainViewModelFactory
        )

        NavigationStack {
            Observing(mainViewModel.homeUiState) { homeUIState in
                List {
                    ForEach(homeUIState.fruitties, id: \.self) { value in
                        HStack {
                            NavigationLink {
                                FruittieScreen(fruittie: value)
                            } label: {
                                FruittieView(fruittie: value)
                            }
                            Spacer()
                            Button("Add", action: {
                                mainViewModel.addItemToCart(fruittie: value)
                            }).buttonStyle(.bordered)
                        }
                    }
                }
            }
            .navigationTitle("Fruitties")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    NavigationLink { CartView() } label: {
                        // ...
                    }
                }
            }
        }
    }
}
```

---

## 9. 依赖注入

### 9.1 简单的 DI 方案

本项目使用手动依赖注入，核心组件：

| 组件 | 职责 |
|------|------|
| `Factory` | 创建平台特定对象 (Database, DataStore) |
| `AppContainer` | 管理所有依赖，提供 ViewModel 工厂 |
| `viewModelFactory` | AndroidX ViewModel 工厂 DSL |

### 9.2 Android DI 设置

```kotlin
// androidApp/.../MainActivity.kt

class MainActivity : ComponentActivity() {
    private val appContainer by lazy {
        (application as FruittiesAndroidApp).container
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(LocalAppContainer provides appContainer) {
                FruittiesApp()
            }
        }
    }
}
```

---

## 10. 常见开发任务

### 10.1 添加新的数据模型

**步骤 1**: 在 `commonMain` 创建数据类

```kotlin
// shared/src/commonMain/kotlin/com/example/fruitties/model/Category.kt

@Serializable
@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @SerialName("name")
    val name: String,
    val description: String,
)
```

**步骤 2**: 创建 DAO

```kotlin
// shared/src/commonMain/kotlin/com/example/fruitties/database/CategoryDao.kt

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Category")
    fun getAllAsFlow(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categories: List<Category>)
}
```

**步骤 3**: 更新 Database

```kotlin
// shared/src/commonMain/kotlin/com/example/fruitties/database/AppDatabase.kt

@Database(entities = [Fruittie::class, Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fruittieDao(): FruittieDao
    abstract fun categoryDao(): CategoryDao  // 添加
}
```

**步骤 4**: 更新 Repository

```kotlin
// shared/src/commonMain/kotlin/com/example/fruitties/DataRepository.kt

class DataRepository(...) {
    // 添加新方法
    fun getCategories(): Flow<List<Category>> =
        database.categoryDao().getAllAsFlow()
}
```

### 10.2 添加新的 API

**步骤 1**: 在 API 接口添加方法

```kotlin
// shared/src/commonMain/kotlin/com/example/fruitties/network/FruittieApi.kt

interface FruittieApi {
    suspend fun getData(pageNumber: Int = 0): FruittiesResponse
    suspend fun getCategories(): CategoriesResponse  // 添加
}
```

**步骤 2**: 实现 API

```kotlin
// FruittieNetworkApi.kt
class FruittieNetworkApi(...) : FruittieApi {
    override suspend fun getCategories(): CategoriesResponse {
        return client.get("$apiUrl/categories.json").body()
    }
}
```

### 10.3 添加新的 ViewModel

```kotlin
// shared/src/commonMain/kotlin/com/example/fruitties/viewmodel/CategoryViewModel.kt

class CategoryViewModel(
    private val repository: DataRepository,
) : ViewModel() {
    val categoriesUiState: StateFlow<CategoriesUiState> =
        repository.getCategories()
            .map { CategoriesUiState(categories = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CategoriesUiState(),
            )
}

data class CategoriesUiState(
    val categories: List<Category> = listOf(),
)
```

在 `AppContainer` 中添加工厂：

```kotlin
// shared/src/commonMain/kotlin/com/example/fruitties/di/AppContainer.kt

val categoryViewModelFactory = viewModelFactory {
    initializer { CategoryViewModel(repository = dataRepository) }
}
```

### 10.4 平台特定功能

如果需要实现仅 Android 或仅 iOS 的功能，使用 expect/actual：

```kotlin
// commonMain - 定义期望的行为
expect fun getPlatformName(): String

// androidMain - Android 实现
actual fun getPlatformName(): String = "Android"

// iosMain - iOS 实现
actual fun getPlatformName(): String = "iOS"
```

---

## 11. 开发环境配置

### 11.1 必需工具

| 工具 | 版本要求 | 用途 |
|------|----------|------|
| Android Studio | 最新稳定版 | 主要 IDE |
| Kotlin Multiplatform Plugin | 最新版 | KMP 支持 |
| Xcode | 15.0+ | iOS 开发 |
| JDK | 17+ | Gradle 编译 |

### 11.2 安装 Kotlin Multiplatform Plugin

1. 打开 Android Studio
2. Settings → Plugins → Marketplace
3. 搜索 "Kotlin Multiplatform"
4. 安装并重启

### 11.3 版本目录 (libs.versions.toml)

所有依赖版本在此统一管理：

```toml
[versions]
kotlin = "2.2.21"
androidx-lifecycle = "2.9.4"
ktorVersion = "3.3.2"

[libraries]
androidx-lifecycle-viewmodel = { module = "androidx.lifecycle:lifecycle-viewmodel", version.ref = "androidx-lifecycle" }
ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktorVersion" }

[plugins]
kotlinMultiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
```

---

## 12. 运行与调试

### 12.1 Android

1. 打开 `Fruitties` 项目
2. 等待 Gradle 同步完成
3. 选择 `androidApp` 运行配置
4. 点击 Run (▶️)

### 12.2 iOS

**方式 1: Android Studio**
1. 安装 Kotlin Multiplatform Plugin
2. 选择 `iosApp` 运行配置
3. 选择目标模拟器
4. 点击 Run

**方式 2: Xcode**
1. 打开 `iosApp/iosApp.xcodeproj`
2. 选择目标设备和模拟器
3. 点击 Run

### 12.3 调试技巧

**Android**:
- 使用 Android Studio 的 Kotlin 调试器
- 在 `commonMain` 代码设置断点

**iOS**:
- Xcode 调试 Swift 代码
- Android Studio 调试 Kotlin 代码 (需要 KMP Plugin)

---

## 附录 A: 关键文件速查

| 文件路径 | 作用 |
|----------|------|
| `shared/build.gradle.kts` | KMP 模块配置 |
| `shared/src/commonMain/.../Factory.kt` | 平台特定对象创建 |
| `shared/src/commonMain/.../AppContainer.kt` | DI 容器 |
| `shared/src/commonMain/.../DataRepository.kt` | 数据仓储 |
| `shared/src/commonMain/.../FruittieApi.kt` | 网络 API |
| `shared/src/commonMain/.../AppDatabase.kt` | Room 数据库 |
| `androidApp/.../MainActivity.kt` | Android 入口 |
| `iosApp/iosApp/iOSApp.swift` | iOS 入口 |

## 附录 B: 推荐学习资源

- [Kotlin Multiplatform 官方文档](https://kotlinlang.org/docs/multiplatform.html)
- [Kotlin Multiplatform for Android Developers](https://www.jetbrains.com/help/kotlin-multiplatform-dev-kit/)
- [Room Database with KMP](https://developer.android.com/kotlin/multiplatform/room)
- [DataStore with KMP](https://developer.android.com/kotlin/multiplatform/datastore)
- [Skie - SwiftUI/Kotlin Interop](https://skie.touchlab.co/)

---

*本文档由 AI 辅助生成，基于 Fruitties 项目结构*
