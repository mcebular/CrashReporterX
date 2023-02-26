package net.cebular.crashreporter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import net.cebular.crashreporter.exception.CrashReporterException;
import net.cebular.crashreporter.ui.ReportListActivity;
import net.cebular.crashreporter.utils.AppUtils;
import net.cebular.crashreporter.utils.CrashUtils;
import net.cebular.crashreporter.utils.NotificationUtils;

public class CrashReporter {

    private static Context applicationContext = null;
    private static boolean notificationsEnabled = true;

    private CrashReporter() {
        // This class in not publicly instantiable
    }

    //


    /**
     * Initialize CrashReporter. Notifications are enabled.
     * @param context
     */
    public static void initialize(@NonNull Context context) {
        initialize(context, true);
    }

    /**
     * Initialize CrashReporter.
     * @param context
     * @param enableNotifications If CrashReporter should make a notification upon crash or
     *                            exception report
     */
    public static void initialize(@NonNull Context context, boolean enableNotifications) {
        applicationContext = context.getApplicationContext();
        notificationsEnabled = enableNotifications;
        AppUtils.setInstallID(applicationContext);
        NotificationUtils.initChannels(applicationContext);
        setUpExceptionHandler();
    }

    //

    /**
     * @return CrashReporter's stored application context
     */
    public static Context getApplicationContext() {
        if (applicationContext == null) {
            try {
                throw new CrashReporterException("No context for CrashReporter! Is CrashReporter initialized? Call CrashReporter.initialize(context)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return applicationContext;
    }

    /**
     * @return true if notifications are enabled, false otherwise
     */
    public static boolean areNotificationsEnabled() {
        return notificationsEnabled;
    }

    /**
     * Stores a report for a caught Throwable. Report is listed under "Exceptions" tab in
     * CrashReporter activity. Sample usage:
     * <pre>{@code
     * try {
     *     throwsException();
     * } catch (Exception e) {
     *     e.printStackTrace();
     *     CrashReporter.reportException(e); // write exception to report file
     * }
     * }</pre>
     * @param throwable throwable exception to make a report of
     */
    public static void reportException(Throwable throwable) {
        CrashUtils.saveExceptionReport(throwable);
    }

    /**
     * Creates and returns an intent that launches reports activity listing all saved reports.
     * <pre>{@code
     * Intent i = CrashReporter.getLaunchIntent();
     * startActivity(i);
     * }</pre>
     * @param context context to use for the Intent
     * @return an intent that can be called with startActivity()
     */
    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, ReportListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    //

    private static void setUpExceptionHandler() {
        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CrashReporter.ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new CrashReporter.ExceptionHandler());
        }
    }

    //

    private static class ExceptionHandler implements Thread.UncaughtExceptionHandler {

        private Thread.UncaughtExceptionHandler exceptionHandler;

        public ExceptionHandler() {
            this.exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            CrashUtils.saveCrashReport(e);
            exceptionHandler.uncaughtException(t, e);
        }

    }

}
