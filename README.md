# Video Player - M3U8 Playback and MP4 to M3U8 Conversion

This repository contains the source code for a video player app built using [ExoPlayer](https://exoplayer.dev/), capable of playing M3U8 videos and converting MP4 files to M3U8 format within the app.

## Features

- **M3U8 Video Playback**: Play M3U8 (HLS) streams seamlessly using ExoPlayer.
- **MP4 to M3U8 Conversion**: Convert user-selected MP4 files to M3U8 format with a simple in-app interface.
- **User-friendly Interface**: Easy to navigate and select videos for playback and conversion.
- **Progress Feedback**: A circular progress indicator is displayed during video conversion to inform the user of the ongoing process.

## Screenshots

1. **Home Screen**  
   ![Home Screen](link-to-image)  
   *The main screen where users can select video files or choose to start a new conversion.*

2. **Video Playback**  
   ![Video Playback](link-to-image)  
   *Playback of an M3U8 video using ExoPlayer.*

3. **MP4 to M3U8 Conversion**  
   ![Conversion Screen](link-to-image)  
   *Circular progress indicator shown during video conversion.*

4. **Conversion Complete**  
   ![Conversion Complete](link-to-image)  
   *Success message when the conversion process finishes.*

## Getting Started

### Prerequisites

- Android Studio
- Kotlin 1.9
- Gradle 7.x
- [FFmpeg](https://www.ffmpeg.org/download.html) for video conversion

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/videoplayer.git
Open the project in Android Studio and sync the Gradle files.

Build and run the app on an emulator or a physical Android device.

Usage
Playing M3U8 Videos:

On the home screen, tap on the "Play M3U8" button.
Select an M3U8 file from your internal storage or enter the URL of the HLS stream.
The video will start playing using ExoPlayer.
Converting MP4 to M3U8:

Tap on the "Convert MP4" button.
Select an MP4 file from your device.
A circular progress bar will indicate the conversion process.
Once the conversion is complete, the M3U8 file will be saved and ready for playback.

Dependencies
ExoPlayer for video playback.
FFmpegKit for video conversion.
