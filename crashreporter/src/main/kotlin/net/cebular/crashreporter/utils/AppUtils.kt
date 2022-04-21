package net.cebular.crashreporter.utils

import android.content.Context
import android.os.Build
import androidx.preference.PreferenceManager
import net.cebular.crashreporter.BuildConfig
import java.util.*

object AppUtils {

    @JvmStatic
    fun getReporterInformation(context: Context): String {
        return """
             Reporter information:
             INSTALLATION_ID=${getInstallID(context)}
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

        /*
        TODO these don't work anymore?
        APP:FLAVOR=${BuildConfig.FLAVOR}
        APP:VERSION_CODE=${BuildConfig.VERSION_CODE}
        APP:VERSION_NAME=${BuildConfig.VERSION_NAME}
        */
    }

    fun setInstallID(context: Context) {
        if (getInstallID(context) == "") {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(Constants.PREFERENCE_INSTALL_ID, UUID.randomUUID().toString())
                .apply()
        }
    }

    fun getInstallID(context: Context): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(Constants.PREFERENCE_INSTALL_ID, "")
    }

}