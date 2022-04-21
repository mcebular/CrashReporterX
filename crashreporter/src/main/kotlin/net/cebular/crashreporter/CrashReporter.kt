package net.cebular.crashreporter

import android.content.Context
import android.content.Intent
import net.cebular.crashreporter.ui.ReportListActivity
import net.cebular.crashreporter.utils.AppUtils
import net.cebular.crashreporter.utils.CrashUtils
import net.cebular.crashreporter.utils.NotificationUtils

object CrashReporter {

    var notificationsEnabled: Boolean = true
        private set

    @JvmStatic
    fun initialize(context: Context) {
        initialize(context, true)
    }

    @JvmStatic
    fun initialize(context: Context, enableNotifications: Boolean) {
        notificationsEnabled = enableNotifications
        AppUtils.setInstallID(context)
        NotificationUtils.initChannels(context)
        setupExceptionHandler(context.applicationContext)
    }

    @JvmStatic
    fun reportException(context: Context, throwable: Throwable) {
        CrashUtils.saveExceptionReport(context, throwable)
    }

    @JvmStatic
    fun getLaunchIntent(context: Context?): Intent {
        return Intent(context, ReportListActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    //

    private fun setupExceptionHandler(context: Context) {
        if (Thread.getDefaultUncaughtExceptionHandler() !is ExceptionHandler) {
            Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(context))
        }
    }

    //

    private class ExceptionHandler(val context: Context) : Thread.UncaughtExceptionHandler {
        private val exceptionHandler: Thread.UncaughtExceptionHandler? = Thread.getDefaultUncaughtExceptionHandler()

        override fun uncaughtException(t: Thread, e: Throwable) {
            CrashUtils.saveCrashReport(context, e)
            exceptionHandler?.uncaughtException(t, e)
        }

    }

}