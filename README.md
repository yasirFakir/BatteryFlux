# BatteryFlux

BatteryFlux is a highly customizable battery widget application for Android 12+ (API 31+). It provides real-time battery monitoring, app-specific usage statistics, and a persistent status bar notification for a personalized experience.

## 🏗️ Architecture

```text
.
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/battify/
│   │   │   │   ├── data/                 # Battery & Settings Repositories
│   │   │   │   ├── service/              # Foreground Battery Monitoring Service
│   │   │   │   ├── ui/                   # Jetpack Compose Screens (Dashboard, Customization)
│   │   │   │   ├── widget/               # Glance-based Widget Implementation
│   │   │   │   └── MainActivity.kt       # Main entry point & Permission handling
│   │   │   ├── res/                      # Android Resources (Layouts, Themes, XML)
│   │   │   └── AndroidManifest.xml       # App Permissions & Component Declarations
│   │   └── test/                         # Unit tests
│   └── build.gradle.kts                  # App-level dependencies
├── build.gradle.kts                      # Project-level configuration
├── settings.gradle.kts                   # Project settings & Module inclusion
└── README.md                             # Documentation
```

The app is built using modern Android development practices:

- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) for a declarative and reactive main application interface.
- **Widgets:** [Jetpack Glance](https://developer.android.com/jetpack/compose/glance) to build app widgets using Compose-like syntax, ensuring consistency and ease of styling.
- **Service Layer:** A **Foreground Service** is used to maintain a persistent notification in the status bar, allowing for custom battery percentage displays where system APIs are limited.
- **Data Layer:** 
    - `BatteryManager`: Interfaces with the system to retrieve real-time battery levels, health, and charging status.
    - `UsageStatsManager`: Aggregates per-app battery consumption data (requires `PACKAGE_USAGE_STATS` permission).
- **Theming:** Material 3 with support for dynamic colors and user-defined custom color schemes.

## 🚀 Features

- **Resizable Widgets:** Support for sizes starting from 1x1 up to full-screen layouts.
- **Customization:** Change colors, toggle visibility of metrics (percentage, health, app usage).
- **App Usage Tracking:** See which apps are draining your battery in a clean, visual list.
- **Status Bar Integration:** A custom persistent notification that mirrors your chosen UI style in the system tray.

## 🛠️ Compilation & Setup

### Prerequisites
- **JDK 17** or higher.
- **Android Studio Iguana** (or newer) recommended.
- **Android SDK 34** (Compile SDK).
- **Minimum SDK 30** (Android 11).

### Building from Command Line
1. Clone the repository:
   ```bash
   git clone https://github.com/[your-username]/BatteryFlux.git
   cd BatteryFlux
   ```
2. Build the project:
   ```bash
   ./gradlew assembleDebug
   ```
3. Install on a connected device:
   ```bash
   ./gradlew installDebug
   ```

### Permissions Note
To view per-app battery usage, the user must manually grant the "Usage Access" permission within Android Settings when prompted by the app.
