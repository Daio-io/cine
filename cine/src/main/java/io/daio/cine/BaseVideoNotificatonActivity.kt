package io.daio.cine

import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.CallSuper
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.SwipeDismissBehavior
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.base_video_activity.*


abstract class BaseVideoNotificationActivity<in T : Parcelable> : AppCompatActivity() {

    companion object {
        const val NOTIFICATION_CONTENT = "NOTIFICATION_CONTENT"
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.slide_in_top, R.anim.abc_slide_out_top)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_video_activity)

        val closeButton = findViewById<View>(R.id.close_button)
        val openButton = findViewById<View>(R.id.open_button)
        closeButton.setOnClickListener { onCloseClicked() }
        openButton.setOnClickListener { onOpenClicked() }

        val content = intent.getParcelableExtra<T>(NOTIFICATION_CONTENT)

        val swipeView = findViewById<ViewGroup>(R.id.swipe_view)
        val videoContainer = findViewById<ViewGroup>(R.id.videoContainer)

        val layoutParams = swipeView.layoutParams as CoordinatorLayout.LayoutParams
        val swipeDismissBehavior = SwipeDismissBehavior<View>()
        swipeDismissBehavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END)//Swipe direction i.e any direction, here you can put any direction LEFT or RIGHT
        swipeDismissBehavior.setDragDismissDistance(0.3f)
        swipeDismissBehavior.setListener(object : SwipeDismissBehavior.OnDismissListener {

            override fun onDismiss(view: View?) {
                onDismiss()
            }

            override fun onDragStateChanged(state: Int) {
            }
        })
        layoutParams.behavior = swipeDismissBehavior

        swipeView.setOnTouchListener { _, event ->
            swipeDismissBehavior.onTouchEvent(video_notification_parent, swipeView, event)
        }

        onReady(videoContainer, content)
    }

    fun setAppName(text: String) {
        app_title.text = text
    }

    fun setNotificationTitle(text: String) {
        title_text.text = text
    }

    abstract fun onOpenClicked()
    abstract fun onCloseClicked()
    abstract fun onDismiss()
    abstract fun onReady(container: ViewGroup, content: T)

}
