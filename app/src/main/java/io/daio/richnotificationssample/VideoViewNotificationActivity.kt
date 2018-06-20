package io.daio.richnotificationssample

import android.net.Uri
import android.os.Parcelable
import android.view.ViewGroup
import android.widget.VideoView
import io.daio.cine.CineNotificationActivity
import io.daio.cine.CineButton
import kotlinx.android.parcel.Parcelize

class VideoViewNotificationActivity : CineNotificationActivity<VideoItem>() {

    private lateinit var videoView: VideoView

    override fun onReady(container: ViewGroup, content: VideoItem) {
        videoView = layoutInflater.inflate(R.layout.activity_video_view_notification, container, true).findViewById(R.id.videoView)

        val video = Uri.parse(content.url)
        videoView.setVideoURI(video)
        videoView.setOnPreparedListener { mp ->
            mp.isLooping = false
            videoView.start()
        }

        setNotificationBodyText(content.title)
        setAppName("BBC Sport")
        setAppIcon(R.drawable.abc_ic_star_black_36dp)
        setButtons(CineButton("OK") {
            finish()
        }, CineButton("Close") {
            finish()
        })
    }


    override fun onDismiss() {
        finish()
    }
}

@Parcelize
data class VideoItem(val title: String,
                     val url: String) : Parcelable