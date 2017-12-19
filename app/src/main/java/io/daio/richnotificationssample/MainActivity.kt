package io.daio.richnotificationssample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.RemoteViews
import com.squareup.picasso.Picasso
import io.daio.cine.VideoItem
import io.daio.cine.VideoViewNotificationActivity
import io.daio.cine.createNotificationIntent
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)
            val radioChannel = NotificationChannel("radio", "radio", NotificationManager.IMPORTANCE_DEFAULT)
            radioChannel.enableVibration(false)
            radioChannel.enableLights(false)
            val defaultChannel = NotificationChannel("default", "default", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(defaultChannel)
            notificationManager.createNotificationChannel(radioChannel)
        }

        val button = findViewById<Button>(R.id.noti_button)
        button.setOnClickListener {
            showSecretNotification()
        }
    }

    private fun showSecretNotification() {
        val notifId = 187683
        val videoItem = VideoItem("Goals Goals Goals", "https://firebasestorage.googleapis.com/v0/b/strangerthings-io-daio.appspot.com/o/BBC_One-2017-12-13_22-49-34.mp4?alt=media&token=cbc3fbef-280b-4bd3-9384-648775eca510")
        val intent = createNotificationIntent(this, videoItem, VideoViewNotificationActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        val contentView = RemoteViews(packageName, R.layout.noti_view)
        val notification = NotificationCompat.Builder(this, "default")
                .setColorized(true)
                .setWhen(Date().time)
                .setSmallIcon(android.R.drawable.ic_menu_close_clear_cancel)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setShowWhen(true)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setCustomBigContentView(contentView)
                .setContentTitle("Custom View").build()

        Picasso.with(applicationContext)
                .load("https://ichef.bbci.co.uk/onesport/cps/800/cpsprodpb/C45C/production/_99186205_froomevuelta_reuters.jpg")
                .into(contentView, R.id.image, notifId, notification)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notifId, notification)
    }

}
