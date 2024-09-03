package com.myprojects.videoplayer

sealed class VideoState(val data: String? = null) {
    class Success(data: String) : VideoState(data)
    class Loading(data: String? = null) : VideoState(data)
    class Converting(data: String? = null) : VideoState(data)
    class Error() : VideoState()
    class Idle() : VideoState()
}