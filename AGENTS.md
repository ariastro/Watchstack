# AGENTS.md — Watchstack

KMP anime tracker (Android + iOS). Display name: **Watchstack**. UI + business logic live in `:shared`; `:androidApp` is a thin host.

## Modules

| Module | Role |
|--------|------|
| `shared` | Compose Multiplatform UI, domain, data, Koin, SQLDelight |
| `androidApp` | `MainActivity` → `initKoinAndroid` → `App()` |
| `iosApp` | Xcode host; `MainViewController` starts Koin once |

Root package: `io.sws.watchstack`.

## Commands

```bash
# Fast shared compile (default verify after Kotlin edits)
./gradlew :shared:compileAndroidMain
# or: rtk gradlew :shared:compileAndroidMain

# Android app
./gradlew :androidApp:assembleDebug
./gradlew :androidApp:installDebug

# Full multiplatform (slow; needs Xcode toolchain for iOS)
./gradlew :shared:compileKotlinIosSimulatorArm64
```

Unit tests: `./gradlew :shared:testAndroidHostTest` (`commonTest`, kotlin-test). No CI config in repo.

Versions catalog: `gradle/libs.versions.toml` (AGP 9, Kotlin 2.4, Compose MP 1.11, Koin 4, SQLDelight 2.3, Ktor 3, Navigation 3 `1.1.1` via `org.jetbrains.androidx.navigation3`).

## Architecture (do not invent parallel patterns)

```
shared/.../io/sws/watchstack/
  core/           expect/actual + helpers
  domain/         models, repository interfaces, use cases
  data/           remote (Jikan), local (SQLDelight + memory), repository impls
  di/             KoinModule + platform modules
  presentation/   screens (MVI), theme, navigation, components
  Main.kt         App() shell
```

- **MVI**: `BaseViewModel<State, Intent, Effect>` or `SimpleViewModel` (no effects). Screen + `*Contract.kt` (UiState/Intent) + ViewModel.
- **DI**: Koin. Platform bindings (`AnimeDatabase`, `ThemePreferences`, Android `Context`) injected via `initKoin(platformModule)`.
- **Nav**: Navigation 3 (`NavDisplay` + `NavKey` routes). `Navigator` holds per-tab stacks; nested screens push on current tab. Bottom bar only on top-level tabs (Home/Search/List). Back from Settings/Detail pops to previous — never wipes stack to empty.
- **Remote**: Jikan API v4 (`JikanApi`). Rate limit **400ms** between calls, retry on 429. List endpoints return `PagedAnime`.
- **Local**: SQLDelight `AnimeDatabase` = tracked library + remote list/detail disk cache (24h TTL) + search history. Hot path still uses in-memory `MemoryAnimeCache` (5 min TTL); disk is fallback after process death.
- **Theme**: `BrutalTheme` + `LocalBrutalColors` / `Typography` / `Dimensions` — not raw Material colors for product UI.

## Hard-won gotchas

### Koin — prefer explicit factories

`singleOf` / `factoryOf` / `viewModelOf` resolve **all** ctor params. Default primitives (`ttlMs: Long`, `maxItems: Int`) have no binding → `InstanceCreationException` cascade.

Use:

```kotlin
single { MemoryAnimeCache() }
factory { GetTopAnimeUseCase(get()) }
viewModel { (category: BrowseCategory) -> BrowseViewModel(category, get(), ...) }
```

Platform: `AndroidKoin.kt` / iOS `MainViewController.kt` register `AnimeDatabase` + `ThemePreferences`.

### ViewModel keys

- Detail: `koinViewModel(key = malId.toString())` — avoids stale state across titles.
- Browse: `koinViewModel(key = category.name, parameters = { parametersOf(category) })`.

### Lazy lists / scroll

Jikan can return **duplicate `malId`**. Always `distinctBy { malId }` before `items(..., key = ...)`. Multi-rail home: key with rail prefix (`"airing-$malId"`), cap rail size, set `contentType`. Shared-element keys must be unique on screen (don't put same poster key on hero + rail).

### Compose resources

Generated package: `watchstack.shared.generated.resources` (`Res.drawable.*`). Drawables under `shared/src/commonMain/composeResources/`.

### SQLDelight

- Schema: `shared/src/commonMain/sqldelight/io/sws/watchstack/db/AnimeDatabase.sq`
- Generated package: `io.sws.watchstack.db`
- Driver: expect/actual `DatabaseFactory`

Changing `.sq` requires rebuild so generated sources update.

### Expect/actual

`ThemePreferences`, `DatabaseFactory`, `currentTimeMillis()`, `performHaptic()`, `shareText()` — implement both androidMain + iosMain when adding new expects. Android haptics/share resolve `Context` via Koin.

### SQLDelight migrations

New tables/columns: update `AnimeDatabase.sq` **and** add `shared/src/commonMain/sqldelight/migrations/N.sqm` for existing installs. Fresh installs use `.sq`; upgrades use `.sqm`.

## Screen map

| Route | Screen | Notes |
|-------|--------|-------|
| Home | multi-rail discovery | parallel load 4 endpoints |
| Search | debounced search + history | pagination |
| Tracked (List) | library tabs | +1, undo remove, sort |
| Browse | category grid | sort/type/score filters |
| Detail | track/edit | trailer, share (MAL URL) |
| Settings | theme mode | System/Light/Dark |
| Stats | library aggregates | |

## Style

- No drive-by refactors / unsolicited docs.
- Match existing naming: `*UseCase`, `*Repository` / `*Impl`, `*ViewModel`, `*Contract`.
- Prefer domain models in presentation; map DTO → domain in `data/remote/mapper`.
- Don't add comments unless asked.
