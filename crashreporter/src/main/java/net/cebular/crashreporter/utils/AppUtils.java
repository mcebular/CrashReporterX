package net.cebular.crashreporter.utils;

import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;

import net.cebular.crashreporter.BuildConfig;

import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by bali on 12/08/17.
 */

public class AppUtils {

    public static String getReporterInformation(Context context) {

        return "Reporter information:" + "\n" +

                "INSTALLATION_ID=" + getInstallID(context) + "\n" +

                "APP:BUILD_TYPE=" + BuildConfig.BUILD_TYPE + "\n" +
                // TODO these do not work anymore in the lib, must be provided by App's BuildConfig.
                // "APP:FLAVOR=" + BuildConfig.FLAVOR + "\n" +
                // "APP:VERSION_CODE=" + BuildConfig.VERSION_CODE + "\n" +
                // "APP:VERSION_NAME=" + BuildConfig.VERSION_NAME + "\n" +

                "OS:CODENAME=" + Build.VERSION.CODENAME + "\n" +
                "OS:INCREMENTAL=" + Build.VERSION.INCREMENTAL + "\n" +
                "OS:RELEASE=" + Build.VERSION.RELEASE + "\n" +
                "OS:SDK_INT=" + Build.VERSION.SDK_INT + "\n" +

                "DEV:BRAND=" + Build.BRAND + "\n" +
                "DEV:MANUFACTURER=" + Build.MANUFACTURER + "\n" +
                "DEV:MODEL=" + Build.MODEL + "\n" +
                "DEV:PRODUCT=" + Build.PRODUCT + "\n" +

                "MISC:TIMEZONE=" + TimeZone.getDefault().getID() + "\n";
    }

    //

    public static void setInstallID(Context context) {
        if (getInstallID(context).equals("")) {
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString(Constants.PREFERENCE_INSTALL_ID, UUID.randomUUID().toString())
                    .apply();
        }
    }

    public static String getInstallID(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.PREFERENCE_INSTALL_ID, "");
    }

}
