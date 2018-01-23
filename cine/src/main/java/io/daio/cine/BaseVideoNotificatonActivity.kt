package io.daio.cine

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.CallSuper
import android.support.annotation.DrawableRes
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.SwipeDismissBehavior
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.base_video_activity.*

abstract class BaseVideoNotificationActivity<in T : Parcelable> : AppCompatActivity() {

    private lateinit var cineButtonsContainer: ViewGroup
    private lateinit var videoContainer: ViewGroup

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_video_activity)
        cineButtonsContainer = findViewById(R.id.cine_buttons_group)
        val content = intent.getParcelableExtra<T>(NOTIFICATION_CONTENT)

        val swipeView = findViewById<ViewGroup>(R.id.swipe_view)
        videoContainer = findViewById(R.id.videoContainer)

        val layoutParams = swipeView.layoutParams as CoordinatorLayout.LayoutParams
        val swipeDismissBehavior = SwipeDismissBehavior<View>()
        swipeDismissBehavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END)
        swipeDismissBehavior.setDragDismissDistance(0.3f)
        swipeDismissBehavior.setListener(object : SwipeDismissBehavior.OnDismissListener {

            override fun onDismiss(view: View?) {
                onDismiss()
            }

            override fun onDragStateChanged(state: Int) {
            }
        })
        layoutParams.behavior = swipeDismissBehavior
        swipeView.layoutParams = layoutParams

        swipeView.setOnTouchListener { _, event ->
            swipeDismissBehavior.onTouchEvent(video_notification_parent, swipeView, event)
        }
        onReady(videoContainer, content)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val content = intent?.getParcelableExtra<T>(NOTIFICATION_CONTENT)
        content?.let { onNewContent(it) }
    }

    fun setAppName(text: String) {
        app_title.text = text
    }

    fun setNotificationBodyText(text: String) {
        body_text.text = text
    }

    fun setAppIcon(@DrawableRes drawableRes: Int) {
        val icon = ContextCompat.getDrawable(this@BaseVideoNotificationActivity, drawableRes)
        app_icon.setImageDrawable(icon)
    }

    fun setButtons(vararg buttons: CineButton) {
        if (cineButtonsContainer.childCount > 0) cineButtonsContainer.removeAllViews()
        buttons.forEachIndexed {index, cineButton ->
            val button = layoutInflater.inflate(R.layout.cine_button, cineButtonsContainer, false) as Button
            button.text = cineButton.buttonTitle
            button.setOnClickListener { cineButton.action() }
            cineButtonsContainer.addView(button)
            if (index + 1 >= MAX_CINE_BUTTONS) return
        }
    }

    abstract fun onDismiss()
    abstract fun onReady(container: ViewGroup, content: T)
    open fun onNewContent(content: T) {

    }

    companion object {
        const val NOTIFICATION_CONTENT = "NOTIFICATION_CONTENT"
        const val MAX_CINE_BUTTONS = 2
    }

}

data class CineButton(val buttonTitle: String,
                      val action: () -> Unit)
