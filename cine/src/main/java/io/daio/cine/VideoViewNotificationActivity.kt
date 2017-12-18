package io.daio.cine

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.view.ViewGroup
import android.widget.VideoView
import kotlinx.android.parcel.Parcelize

class VideoViewNotificationActivity : BaseVideoNotificationActivity<VideoItem>() {

    private lateinit var videoView: VideoView

    override fun onReady(container: ViewGroup, content: VideoItem) {
        videoView = layoutInflater.inflate(R.layout.activity_video_view_notification, container, true).findViewById(R.id.videoView)

        val video = Uri.parse(content.url)
        videoView.setVideoURI(video)
        videoView.setOnPreparedListener { mp ->
            mp.isLooping = false
            videoView.start()
        }

        setNotificationTitle(content.title)
        setAppName("BBC Sport")
    }


    override fun onCloseClicked() {
        finish()
    }

    override fun onDismiss() {
        finish()
    }

    override fun onOpenClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}


@Parcelize
data class VideoItem(val title: String,
                     val url: String) : Parcelable

fun createNotificationIntent(context: Context,
                             content: Parcelable,
                             cls : Class<*>) = Intent(context, cls).apply {
    putExtra(BaseVideoNotificationActivity.NOTIFICATION_CONTENT, content)
}