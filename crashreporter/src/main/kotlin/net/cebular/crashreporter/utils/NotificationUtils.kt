package net.cebular.crashreporter.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import net.cebular.crashreporter.CrashReporter
import net.cebular.crashreporter.R
import net.cebular.crashreporter.utils.Constants.LOG_TAG

object NotificationUtils {

    const val CHANNEL_CRASHES = "crashreporter:crashes"
    const val CHANNEL_EXCEPTIONS = "crashreporter:exceptions"

    const val NOTIFICATION_ID = 1

    //

    fun initChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        createNotificationChannel(
            context,
            CHANNEL_CRASHES,
            "Crash reports",
            "Notifications for crash reports",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        createNotificationChannel(
            context,
            CHANNEL_EXCEPTIONS,
            "Exception reports",
            "Notifications for exception reports",
            NotificationManager.IMPORTANCE_DEFAULT
        )
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        context: Context,
        channelId: String,
        name: String,
        description: String,
        importance: Int
    ) {
        val channel = NotificationChannel(channelId, name, importance).apply {
            this.description = description
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    //

    fun showNotification(context: Context, text: String?, reportType: Int) {
        if (!CrashReporter.notificationsEnabled) {
            return
        }

        val channelId: String
        val contentTitle: String
        var contentText: String = text?.trim() ?: ""

        if (reportType == Constants.REPORT_TYPE_CRASH) {
            channelId = CHANNEL_CRASHES
            contentTitle = "View crash report"
            if (contentText.isEmpty()) {
                contentText = "Click here to see crash logs."
            }

        } else if (reportType == Constants.REPORT_TYPE_EXCEPTION) {
            channelId = CHANNEL_EXCEPTIONS
            contentTitle = "View exception report"
            if (contentText.isEmpty()) {
                contentText = "Click here to see exception logs."
            }

        } else {
            Log.e(LOG_TAG, "Invalid report type: " + reportType)
            return
        }

        val intent = CrashReporter.getLaunchIntent(context)
        intent.putExtra(Constants.EXTRA_LANDING, reportType)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_warning_black_24dp)
            .setContentIntent(pendingIntent)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(context, R.color.colorCrashReporterNotification))

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

}