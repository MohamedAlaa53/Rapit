# رابطة — Android APK project

This is an Android Studio project for the Rabta link saver app. It wraps the app UI in a native Android WebView and accepts Android Share intents (`ACTION_SEND` / `text/plain`).

## Features
- Save links manually.
- Share URLs from other apps into Rabta.
- Open a random saved link.
- Search, shuffle, delete, export and import saved links.
- Uses browser storage in the app WebView; data stays on-device.

## Build APK with GitHub Actions
1. Create a new GitHub repository.
2. Upload all files and folders in this project to the repository.
3. Open the **Actions** tab and allow workflows if GitHub asks.
4. Run **Build Rabta APK** using **Run workflow**, or push to `main`.
5. When it finishes, open the workflow run and download the `Rabta-debug-apk` artifact.
6. Extract the ZIP and install `app-debug.apk` on Android. You may need to allow installing apps from that source.

## Build locally
Open the folder in Android Studio (recent version), allow Gradle sync, then choose Build > Build APK(s). The APK will be in `app/build/outputs/apk/debug/`.

## Note
This environment does not have the Android SDK/Gradle build setup, so no APK binary is included in this archive. The workflow builds the APK on GitHub's Android build runner.
