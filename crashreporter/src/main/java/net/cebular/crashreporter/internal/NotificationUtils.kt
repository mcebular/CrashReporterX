package net.cebular.crashreporter.internal

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import net.cebular.crashreporter.CrashReporter
import net.cebular.crashreporter.CrashReporter.ReportType
import net.cebular.crashreporter.CrashReporter.getLaunchIntent
import net.cebular.crashreporter.R


const val CHANNEL_CRASHES = "crashreporter:crashes"
const val CHANNEL_EXCEPTIONS = "crashreporter:exceptions"

const val NOTIFICATION_ID = 1

fun Context.initNotificationChannels() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        return
    }

    createNotificationChannel(
        CHANNEL_CRASHES,
        getString(R.string.channel_title_crashes),
        getString(R.string.channel_description_crashes),
        NotificationManager.IMPORTANCE_DEFAULT
    )
    createNotificationChannel(
        CHANNEL_EXCEPTIONS,
        getString(R.string.channel_title_exceptions),
        getString(R.string.channel_description_exceptions),
        NotificationManager.IMPORTANCE_DEFAULT
    )
}

@RequiresApi(api = Build.VERSION_CODES.O)
private fun Context.createNotificationChannel(
    channelId: String,
    name: String,
    description: String,
    importance: Int
) {
    getSystemService(NotificationManager::class.java)?.createNotificationChannel(
        NotificationChannel(channelId, name, importance).apply {
            this.description = description
        }
    )
}

fun Context.showReportNotification(text: String?, reportType: ReportType) {
    if (!CrashReporter.notificationsEnabled) {
        return
    }

    val channelId: String
    val contentTitle: String
    var contentText: String? = text?.trim()

    when (reportType) {
        ReportType.CRASH -> {
            channelId = CHANNEL_CRASHES
            contentTitle = getString(R.string.notification_title_crash)
            contentText = contentText ?: getString(R.string.notification_description_crash)
        }

        ReportType.EXCEPTION -> {
            channelId = CHANNEL_EXCEPTIONS
            contentTitle = getString(R.string.notification_title_exception)
            contentText = contentText ?: getString(R.string.notification_description_exception)
        }
    }

    val pendingIntent = PendingIntent.getActivity(
        this,
        0,
        getLaunchIntent(),
        PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(R.drawable.ic_warning_black_24dp)
        .setContentIntent(pendingIntent)
        .setContentTitle(contentTitle)
        .setContentText(contentText)
        .setAutoCancel(true)
        .setColor(ContextCompat.getColor(this, R.color.colorCrashReporterNotification))

    getSystemService(NotificationManager::class.java)?.notify(NOTIFICATION_ID, builder.build())
}
