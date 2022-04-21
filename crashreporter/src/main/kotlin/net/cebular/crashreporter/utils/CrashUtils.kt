package net.cebular.crashreporter.utils

import android.content.Context
import android.util.Log
import net.cebular.crashreporter.utils.Constants.LOG_TAG
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object CrashUtils {

    private val crashReportsFilter =
        FilenameFilter { _, name -> name.endsWith(Constants.CRASH_SUFFIX + Constants.FILE_EXTENSION) }

    private val exceptionReportsFilter =
        FilenameFilter { _, name -> name.endsWith(Constants.EXCEPTION_SUFFIX + Constants.FILE_EXTENSION) }

    private val allReportsFilter = FilenameFilter { _, _ -> true }

    //

    @JvmStatic
    fun getReports(context: Context): List<File> {
        return getReports(context, allReportsFilter)
    }

    @JvmStatic
    fun getCrashReports(context: Context): List<File> {
        return getReports(context, crashReportsFilter)
    }

    @JvmStatic
    fun getExceptionReports(context: Context): List<File> {
        return getReports(context, exceptionReportsFilter)
    }

    private fun getReports(context: Context, filter: FilenameFilter): List<File> {
        return getCrashReportsPath(context)?.let { File(it).listFiles(filter)?.toList() } ?: emptyList()
    }

    @JvmStatic
    fun saveCrashReport(context: Context, throwable: Throwable) {
        saveReport(context, throwable, Constants.REPORT_TYPE_CRASH)
    }

    @JvmStatic
    fun saveExceptionReport(context: Context, throwable: Throwable) {
        Thread { saveReport(context, throwable, Constants.REPORT_TYPE_EXCEPTION) }.start()
    }

    private fun saveReport(context: Context, throwable: Throwable, reportType: Int) {
        val suffix = when(reportType) {
            Constants.REPORT_TYPE_CRASH -> Constants.CRASH_SUFFIX
            Constants.REPORT_TYPE_EXCEPTION -> Constants.EXCEPTION_SUFFIX
            else -> ""
        }

        val filename = getCrashLogTime() + suffix + Constants.FILE_EXTENSION
        val reportFilePath = getCrashReportsPath(context) + File.separator + filename
        File(reportFilePath).writeText(getStackTrace(throwable))

        NotificationUtils.showNotification(context, throwable.localizedMessage, reportType)
    }

    @JvmStatic
    fun deleteReport(file: File): Boolean {
        if (!file.exists()) {
            return true
        }

        val deleted = file.delete()
        if (!deleted) {
            Log.e(LOG_TAG, "Could not delete crash report file '" + file.path + "'.")
        }
        return deleted
    }

    @JvmStatic
    fun clearCrashReports(context: Context) {
        clearReports(context, crashReportsFilter)
    }

    @JvmStatic
    fun clearExceptionReports(context: Context) {
        clearReports(context, exceptionReportsFilter)
    }

    @JvmStatic
    fun clearReports(context: Context) {
        clearReports(context, allReportsFilter)
    }

    private fun clearReports(context: Context, filter: FilenameFilter) {
        getCrashReportsPath(context)?.let {
            File(it).listFiles(filter)?.forEach { file ->
                val deleted = file.delete()
                if (!deleted) {
                    Log.e(LOG_TAG, "Could not delete crash report file '" + file.path + "'.")
                }
            }
        }
    }

    //

    private fun getCrashLogTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun getStackTrace(e: Throwable): String {
        val result: Writer = StringWriter()
        val printWriter = PrintWriter(result)

        e.printStackTrace(printWriter)
        val crashLog = result.toString()
        printWriter.close()
        return crashLog
    }

    @JvmStatic
    fun getCrashReportsPath(context: Context): String? {
        val file = File(
            context.getExternalFilesDir(null),
            Constants.CRASH_REPORT_DIR
        )
        if (!file.exists()) {
            val created = file.mkdirs()
            if (!created) {
                Log.e(LOG_TAG, "Could not create crash reports directory '" + file.path + "'.")
                return null
            }
        }
        return file.absolutePath
    }

}