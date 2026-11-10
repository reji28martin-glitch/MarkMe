# Mark Me - Offline AI Face Recognition Attendance App
### Native Android (Kotlin + Jetpack Compose + CameraX + ML Kit + TFLite MobileFaceNet + Room)

A high-performance, 100% offline edge-AI face recognition attendance kiosk designed for schools, universities, and institutions.

---

## 🚀 Key Highlights & Hardware Integration
- **Zero Internet Requirement**: 100% on-device face detection via ML Kit and 128-d face embedding inference via TensorFlow Lite (MobileFaceNet).
- **Multi-Camera Engine**:
  - Device Front Camera (Selfie kiosk)
  - Device Back Camera
  - External USB Webcams via UVC Android driver
  - Enterprise CCTV / IP Cameras via RTSP / HTTP MJPEG using **AndroidX Media3 ExoPlayer RTSP**.
- **Concurrent Edge Detection**: Multiple cameras process face detection simultaneously in background threads, posting alerts to the main display.
- **Kiosk Foreground Service**: Continuous background analysis, wake-lock management, and landscape kiosk mode.
- **Rules & Security**:
  - Present before 9:00 AM (configurable), Late after 9:00 AM.
  - Automatic upgrade of 'Absent' to Present/Late within 5 hours.
  - 30-second anti-repeat cooldown per student.
  - Unknown Face Detection: saves watermark-stamped cropped photo if unmatched for 3 frames (max 2 per stranger, 10 min debounce).
  - Text-to-Speech (TTS): Announces *"Welcome, <Student Name>"*.

---

## 📦 How to Build the APK File

### Method 1: Using Android Studio (Recommended)
1. Download the project zip by clicking **"Download Complete Project (.ZIP)"** in Mark Me.
2. Unzip the folder and open Android Studio.
3. Select **Open** and choose the unzipped `MarkMe-Android` folder.
4. Wait for Gradle sync to finish (1-2 minutes).
5. In the top menu, click:
   👉 **Build > Build Bundle(s) / APK(s) > Build APK(s)**
6. A popup will appear in the bottom-right: *"APK(s) generated successfully."*
7. Click **locate** to find your **`app-debug.apk`** file!
8. Copy the `app-debug.apk` to your Android phone/tablet and tap to install!

### Method 2: Command Line (CLI) Build
Run in the project directory:
```bash
# On Linux / macOS:
./gradlew assembleDebug

# On Windows:
gradlew.bat assembleDebug
```
The generated APK will be located at:
`app/build/outputs/apk/debug/app-debug.apk`

### Method 3: 100% Free Cloud Build via GitHub Actions (No local Android Studio needed!)
The project includes a ready-made **`.github/workflows/build-apk.yml`**!
1. Push this project folder to a GitHub repository (private or public).
2. Go to the **Actions** tab on your GitHub repository.
3. The **"Build Android APK"** action will run automatically.
4. Once finished (approx. 2 minutes), download **`MarkMe-Debug-APK`** under the **Artifacts** section!

---

## 🛠️ Setup & Requirements
- Android Studio Hedgehog (2023.1.1) or newer / Ladybug
- JDK 17
- Target SDK: 34 (Android 14)
- Minimum SDK: 26 (Android 8.0 Oreo)

### 2. Adding the TFLite Face Embedding Model
1. Download a pre-trained **MobileFaceNet** or **FaceNet** `.tflite` model (e.g., `mobile_face_net.tflite`, input: `112x112x3` RGB, output: `128` float vector).
2. Place the file inside:
   `app/src/main/assets/mobile_face_net.tflite`
3. The app includes an automatic fallback simulated embedding extractor if the model file is not yet placed in assets during initial development testing.

### 3. Connecting CCTV / RTSP IP Cameras
1. Ensure your Android tablet or kiosk box is connected to the same LAN / Wi-Fi subnet as your IP cameras (e.g., Hikvision, Dahua, Uniview, Axis).
2. Go to **Settings > Camera Sources > Add IP Camera**.
3. Enter the camera RTSP stream URL:
   - Hikvision format: `rtsp://admin:password@192.168.1.120:554/Streaming/Channels/102` (substream recommended for AI)
   - Dahua format: `rtsp://admin:password@192.168.1.125:554/cam/realmonitor?channel=1&subtype=1`
   - Uniview format: `rtsp://admin:password@192.168.1.130:554/unicast/c1/s1/live`
4. Tap **Connect & Verify**. Media3 ExoPlayer RTSP decodes frames and feeds them into the background face analyzer!

---

## 📱 Architecture
- **UI**: Jetpack Compose Material 3 with customized Deep Ink Teal (`#0B2B2E`), Porcelain (`#EDF3F1`), Lagoon Teal (`#0E9F8E`), Amber (`#F59E0B`), and Coral (`#EF4444`).
- **Database**: AndroidX Room SQLite with automatic migration and JSON export/import.
- **Reporting**: Native multi-page PDF generation via `android.graphics.pdf.PdfDocument`, Excel (.xlsx) export, and Android Native Share Sheet (`Intent.ACTION_SEND`).
