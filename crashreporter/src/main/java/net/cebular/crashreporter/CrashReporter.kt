package net.cebular.crashreporter

import android.content.Context
import android.content.Intent
import android.util.Log
import net.cebular.crashreporter.internal.ReportListActivity
import net.cebular.crashreporter.internal.getCrashReportsDir
import net.cebular.crashreporter.internal.initNotificationChannels
import net.cebular.crashreporter.internal.setInstallID
import net.cebular.crashreporter.internal.showReportNotification
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CrashReporter {

    private const val LOG_TAG = "CrashReporter"

    const val FilenameTimestampDateFormatPattern = "yyyyMMdd-HHmmss"

    @JvmStatic
    var applicationContext: Context? = null
        private set

    @JvmStatic
    var notificationsEnabled: Boolean = false
        private set

    /**
     * Initializes CrashReporter.
     *
     * @param enableNotifications if true, a notification will appear when crash happens.
     */
    @JvmStatic
    @JvmOverloads
    fun initialize(context: Context, enableNotifications: Boolean = false) {
        applicationContext = context.applicationContext
        notificationsEnabled = enableNotifications

        setupExceptionHandler()
        applicationContext!!.setInstallID()
        if (notificationsEnabled) {
            applicationContext!!.initNotificationChannels()
        }
    }

    /**
     * Creates and returns an intent that launches reports activity listing all saved reports.
     *
     * ```
     * Intent i = CrashReporter.getLaunchIntent();
     * startActivity(i);
     * ```
     *
     * @return An intent that can be called with startActivity().
     */
    @JvmStatic
    fun getLaunchIntent(): Intent {
        return Intent(
            requireApplicationContext(),
            ReportListActivity::class.java
        ).setFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
        )
    }

    /**
     * Stores a report for a caught Throwable. Report can then be found on the list of crash reports.
     *
     * Sample usage:
     * ```
     * try {
     *     throwsException();
     * } catch (Exception e) {
     *     CrashReporter.reportException(e); // write exception to report file
     * }
     * ```
     *
     * @param throwable Throwable exception to make a report of.
     */
    @JvmStatic
    fun reportException(throwable: Throwable) {
        Thread {
            saveReport(throwable, ReportType.EXCEPTION)
        }.start()
    }

    /**
     * Returns CrashReporter's context it was initialized with, or throws an exception of context is null (or if
     * CrashReporter has not been initialized).
     *
     * @throws IllegalStateException if CrashReporter's context is null.
     */
    @JvmStatic
    fun requireApplicationContext(): Context {
        applicationContext.let {
            if (it == null) {
                throw IllegalStateException("CrashReporter has no context. Is CrashReporter initialized?")
            }
            return it
        }
    }

    //

    private fun setupExceptionHandler() {
        if (Thread.getDefaultUncaughtExceptionHandler() !is ExceptionHandler) {
            Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler())
        }
    }

    //

    private class ExceptionHandler : Thread.UncaughtExceptionHandler {
        private val exceptionHandler: Thread.UncaughtExceptionHandler? = Thread.getDefaultUncaughtExceptionHandler()

        override fun uncaughtException(t: Thread, e: Throwable) {
            saveReport(e, ReportType.CRASH)
            exceptionHandler?.uncaughtException(t, e)
        }
    }

    enum class ReportType(val suffix: String) {
        CRASH("crash"),
        EXCEPTION("exception");
    }

    private fun saveReport(throwable: Throwable, reportType: ReportType) {
        val filename = "${getFilenameTimestamp()}-${reportType.suffix}.txt"

        applicationContext?.getCrashReportsDir()?.let {
            val reportFile = File(it, filename)
            try {
                val bufferedWriter = BufferedWriter(FileWriter(reportFile))
                bufferedWriter.write(throwable.stackTraceToString())
                bufferedWriter.flush()
                bufferedWriter.close()

                Log.d(LOG_TAG, "Crash report saved to: $filename")
                applicationContext?.showReportNotification(throwable.localizedMessage, reportType)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getFilenameTimestamp(): String {
        val dateFormat = SimpleDateFormat(FilenameTimestampDateFormatPattern, Locale.getDefault())
        return dateFormat.format(Date())
    }


}