package net.iamsilver.crashreporter.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import net.iamsilver.crashreporter.CrashReporter;
import net.iamsilver.crashreporter.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtils {

    public static final String CHANNEL_CRASHES = "crashreporter:crashes";
    public static final String CHANNEL_EXCEPTIONS = "crashreporter:exceptions";

    public static final int NOTIFICATION_ID = 1;

    //

    public static void initChannels(Context context) {
        context = context.getApplicationContext();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        createNotificationChannel(
                context,
                CHANNEL_CRASHES,
                "Crash reports",
                "Notifications for crash reports",
                NotificationManager.IMPORTANCE_DEFAULT
        );

        createNotificationChannel(
                context,
                CHANNEL_EXCEPTIONS,
                "Exception reports",
                "Notifications for exception reports",
                NotificationManager.IMPORTANCE_DEFAULT
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createNotificationChannel(Context context, String channelId, String name, String description, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    //

    public static void showNotification(String text, int reportType) {
        if (!CrashReporter.areNotificationsEnabled()) return;

        Context context = CrashReporter.getApplicationContext();

        String channelId, contentTitle, contentText = text == null ? "" : text.trim();
        if (reportType == Constants.REPORT_TYPE_CRASH) {
            channelId = CHANNEL_CRASHES;
            contentTitle = "View crash report";
            if (contentText.isEmpty()) contentText = "Click here to see crash logs.";

        } else if (reportType == Constants.REPORT_TYPE_EXCEPTION) {
            channelId = CHANNEL_EXCEPTIONS;
            contentTitle = "View exception report";
            if (contentText.isEmpty()) contentText = "Click here to see exception logs.";

        } else {
            // TODO: log invalid report type
            return;
        }

        Intent intent = CrashReporter.getLaunchIntent(CrashReporter.getApplicationContext());
        intent.putExtra(Constants.EXTRA_LANDING, reportType);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);

        builder.setSmallIcon(R.drawable.ic_warning_black_24dp)
                .setContentIntent(pendingIntent)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.colorCrashReporterNotification));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}
