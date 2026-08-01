# How to Run in Android Studio

Follow these steps to open and run your SafeSearch AI app in Android Studio.

## Prerequisites
- **Android Studio** (Koala Feature Drop or newer recommended)
- **Java Development Kit (JDK)** version 17 or higher (usually bundled with Android Studio)

## Steps

1. **Open Android Studio**.
2. **Open the Project**:
   - Click mostly **File > Open**.
   - Navigate to your project folder: `c:\Users\HP\Desktop\SafeSearchAI_Android`.
   - Click **OK**.
   - Wait for Gradle Sync to complete (look at the bottom right bar).

3. **Set Up an Emulator (Virtual Device)**:
   - Go to **Tools > Device Manager**.
   - Click **+** or **Create Virtual Device**.
   - Select a Phone (e.g., *Pixel 7*).
   - Click **Next**.
   - Download a System Image (e.g., *UpsideDownCake* or *Tiramisu*).
   - Click **Next**, then **Finish**.

4. **Run the App**:
   - In the top toolbar, ensure your app module (usually `app`) is selected in the dropdown.
   - Select your new Emulator (e.g., *Pixel 7 API 34*) in the device dropdown.
   - Click the green **Run** triangle button (or press `Shift + F10`).

## Troubleshooting
- **Gradle Errors**: If you see errors about "SDK location not found", create a file named `local.properties` in the root folder with `sdk.dir=C:\\Users\\HP\\AppData\\Local\\Android\\Sdk` (verify this path on your machine).
- **Import Errors**: If code is red, try **File > Invalidate Caches / Restart**.
