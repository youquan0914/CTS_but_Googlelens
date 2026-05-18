# CTS (Circle To Search)

CTS is a lightweight, open-source Android application that replicates the "Circle to Search" experience. It allows you to instantly search any screen content using Google Lens with a single long-press of your Home button, without repetitive permission prompts.

## Features
- **Instant Search**: Trigger a full-screen Google Lens search from any app.
- **Zero Prompt**: By acting as a Digital Assistant, it avoids the "Start recording" permission dialog every time.
- **Privacy Focused**: Only captures the screen when explicitly triggered by the user.
- **Lightweight**: Minimalist implementation focusing on speed and simplicity.

## How to Install & Setup
1. **Build and Run**: Install the app on your Android device.
2. **Set as Default Assistant**:
   - Open the CTS app.
   - Tap **"Set as Default Assistant"**.
   - In the system settings, select **"Digital assistant app"**.
   - Choose **CTS**.
3. **Enable Context**: Ensure **"Use screenshot"** (or "Analyze on-screen content") is enabled in the assistant settings.

## Usage
- **Long-press the Home button** (or swipe from the bottom corners if using gestures) to capture your screen and open Google Lens.

## Technical Implementation
The app utilizes the Android `VoiceInteractionService` API to act as a system-level assistant. This allows it to use the `onHandleScreenshot()` callback, providing a seamless user experience similar to the official "Circle to Search" feature on Pixel and Samsung devices.

## Acknowledgments
Inspired by the [AKS-Labs/CircleToSearch](https://github.com/AKS-Labs/CircleToSearch) project.
