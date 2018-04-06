@file:JvmName("CineUtil")

package io.daio.cine

import android.content.Context
import android.content.Intent
import android.os.Parcelable


fun createNotificationIntent(context: Context,
                             content: Parcelable,
                             cls : Class<*>) = Intent(context, cls).apply {
    putExtra(CineNotificationActivity.NOTIFICATION_CONTENT, content)
}