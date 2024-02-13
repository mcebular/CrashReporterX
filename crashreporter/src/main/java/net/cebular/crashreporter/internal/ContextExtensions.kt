package net.cebular.crashreporter.internal

import android.content.Context
import android.os.Build
import androidx.preference.PreferenceManager
import net.cebular.crashreporter.BuildConfig
import java.io.File
import java.util.TimeZone
import java.util.UUID

const val CRASH_REPORTS_DIR = "crash_reports"
const val PREFERENCE_INSTALL_ID = "CrashReporterInstallID"

fun Context.getCrashReportsDir(): File? {
    val rootDir = this.getExternalFilesDir(null) ?: this.filesDir

    val crashesDir = File(rootDir, CRASH_REPORTS_DIR)
    if (!crashesDir.exists()) {
        val created = crashesDir.mkdirs()
        if (!created) {
            return null
        }
    }

    return crashesDir
}


fun Context.getDeviceInformation(): String {
    return """
        Reporter information:
        INSTALLATION_ID=${getInstallID()}
        APP:BUILD_TYPE=${BuildConfig.BUILD_TYPE}
        OS:CODENAME=${Build.VERSION.CODENAME}
        OS:INCREMENTAL=${Build.VERSION.INCREMENTAL}
        OS:RELEASE=${Build.VERSION.RELEASE}
        OS:SDK_INT=${Build.VERSION.SDK_INT}
        DEV:BRAND=${Build.BRAND}
        DEV:MANUFACTURER=${Build.MANUFACTURER}
        DEV:MODEL=${Build.MODEL}
        DEV:PRODUCT=${Build.PRODUCT}
        MISC:TIMEZONE=${TimeZone.getDefault().id}
        
        """.trimIndent()
}

fun Context.setInstallID() {
    if (getInstallID() == "") {
        PreferenceManager.getDefaultSharedPreferences(this)
            .edit()
            .putString(PREFERENCE_INSTALL_ID, UUID.randomUUID().toString())
            .apply()
    }
}

fun Context.getInstallID(): String {
    return PreferenceManager.getDefaultSharedPreferences(this)
        .getString(PREFERENCE_INSTALL_ID, "")!!
}
