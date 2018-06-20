package io.daio.cine

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.behavior.SwipeDismissBehavior

abstract class CineNotificationActivity<in T : Parcelable> : AppCompatActivity() {

    private lateinit var cineButtonsContainer: ViewGroup
    private lateinit var videoContainer: ViewGroup
    private lateinit var parentView: CoordinatorLayout
    private lateinit var appTitleText: TextView
    private lateinit var bodyText: TextView
    private lateinit var appIcon: ImageView

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_video_activity)
        cineButtonsContainer = findViewById(R.id.cine_buttons_group)
        parentView = findViewById(R.id.video_notification_parent)
        appTitleText = findViewById(R.id.app_title)
        appIcon = findViewById(R.id.app_icon)
        bodyText = findViewById(R.id.body_text)
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
            swipeDismissBehavior.onTouchEvent(parentView, swipeView, event)
        }
        onReady(videoContainer, content)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            this.intent = it
            val content = it.getParcelableExtra<T>(NOTIFICATION_CONTENT)
            onNewContent(content)
        }
    }

    fun setAppName(text: String) {
        appTitleText.text = text
    }

    fun setNotificationBodyText(text: String) {
        bodyText.text = text
    }

    fun setAppIcon(@DrawableRes drawableRes: Int) {
        val icon = ContextCompat.getDrawable(this@CineNotificationActivity, drawableRes)
        appIcon.setImageDrawable(icon)
    }

    fun setButtons(vararg buttons: CineButton) {
        if (cineButtonsContainer.childCount > 0) cineButtonsContainer.removeAllViews()
        buttons.forEachIndexed { index, cineButton ->
            val button =
                layoutInflater.inflate(R.layout.cine_button, cineButtonsContainer, false) as Button
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
        const val RATIO_16_9 = 0.56f
        const val NOTIFICATION_CONTENT = "NOTIFICATION_CONTENT"
        const val MAX_CINE_BUTTONS = 2
    }

}

data class CineButton(
    val buttonTitle: String,
    val action: () -> Unit
)
