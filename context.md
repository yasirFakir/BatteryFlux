# BatteryFlux - Project Context

## Current Status
- **Project Name:** BatteryFlux
- **Version:** 0.1-alpha (GitHub tag: `v0.1`)
- **Key Features:**
  - Real-time battery monitoring via `BatteryService`.
  - App-specific usage tracking with `UsageStatsManager`.
  - Material 3 Dashboard with circular progress and app lists.
  - Widget Customization (Color picker, Visibility toggles).
  - Responsive 1x1 Glance Battery Widget.
  - Android 11+ compatibility (minSdk 30).

## 🛠️ Infrastructure
- **Tech Stack:** Kotlin, Jetpack Compose, Jetpack Glance, DataStore.
- **Source Control:** GitHub ([yasirFakir/BatteryFlux](https://github.com/yasirFakir/BatteryFlux)).
- **Permissions:** `BATTERY_STATS`, `PACKAGE_USAGE_STATS`, `FOREGROUND_SERVICE`, `POST_NOTIFICATIONS`.

## 📌 Next Session Ideas
- **Advanced UI:** Add more widget layout options (4x2, etc.).
- **Battery History:** Persistence for long-term usage trends beyond 24h.
- **Deep Customization:** Custom fonts, icon packs, and transparency settings.
- **Optimization:** Fine-tune background service battery impact.
- **Release:** Signed APK for production distribution.

## 🗒️ Notes
- Users must manually grant "Usage Access" in Android Settings for app tracking.
- Gradle wrapper files need to be generated locally using `gradle wrapper`.
