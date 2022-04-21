package net.cebular.crashreporter.utils

object Constants {

    const val LOG_TAG = "CrashReporterX"

    const val REPORT_TYPE_EXCEPTION = 2
    const val REPORT_TYPE_CRASH = 1

    const val ARG_REPORT_TYPE = "reports_type"
    const val EXTRA_LANDING = "intent_landing"
    const val EXTRA_FILE_PATH = "file_path"

    const val CRASH_REPORT_DIR = "crash_reports"
    const val EXCEPTION_SUFFIX = "_exception"
    const val CRASH_SUFFIX = "_crash"
    const val FILE_EXTENSION = ".txt"

    const val PREFERENCE_INSTALL_ID = "CrashReporterInstallID"

}