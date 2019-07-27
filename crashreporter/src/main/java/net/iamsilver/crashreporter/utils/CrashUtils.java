package net.iamsilver.crashreporter.utils;

import android.util.Log;

import net.iamsilver.crashreporter.CrashReporter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class CrashUtils {

    private static final String TAG = CrashUtils.class.getSimpleName();

    private static final FilenameFilter crashReportsFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(Constants.CRASH_SUFFIX + Constants.FILE_EXTENSION);
        }
    };

    private static final FilenameFilter exceptionReportsFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(Constants.EXCEPTION_SUFFIX + Constants.FILE_EXTENSION);
        }
    };

    private static final FilenameFilter allReportsFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return true;
        }
    };

    //

    private CrashUtils() {
        // this class is not publicly instantiable
    }

    //

    public static ArrayList<File> getReports() {
        return getReports(allReportsFilter);
    }

    public static ArrayList<File> getCrashReports() {
        return getReports(crashReportsFilter);
    }

    public static ArrayList<File> getExceptionReports() {
        return getReports(exceptionReportsFilter);
    }

    private static ArrayList<File> getReports(final FilenameFilter filter) {
        File[] files = new File(getCrashReportsPath()).listFiles(filter);
        return new ArrayList<>(Arrays.asList(files));
    }

    public static void saveCrashReport(final Throwable throwable) {
        saveReport(throwable, Constants.REPORT_TYPE_CRASH);
    }

    public static void saveExceptionReport(final Throwable throwable) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                saveReport(throwable, Constants.REPORT_TYPE_EXCEPTION);
            }
        }).start();
    }

    private static void saveReport(Throwable throwable, int reportType) {
        String suffix = "";
        if (reportType == Constants.REPORT_TYPE_CRASH) {
            suffix = Constants.CRASH_SUFFIX;
        } else if (reportType == Constants.REPORT_TYPE_EXCEPTION) {
            suffix = Constants.EXCEPTION_SUFFIX;
        }
        String filename = getCrashLogTime() + suffix + Constants.FILE_EXTENSION;
        FileUtils.writeToFile(filename, getStackTrace(throwable));
        NotificationUtils.showNotification(throwable.getLocalizedMessage(), reportType);
    }

    public static boolean deleteReport(File file) {
        if (!file.exists()) return true;

        boolean deleted = file.delete();
        if (!deleted) {
            Log.e(TAG, "Failed to delete report file: " + file.getAbsolutePath());
        }
        return deleted;
    }

    public static void clearCrashReports() {
        clearReports(crashReportsFilter);
    }

    public static void clearExceptionReports() {
        clearReports(exceptionReportsFilter);
    }

    public static void clearReports() {
        clearReports(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return true;
            }
        });
    }

    private static void clearReports(final FilenameFilter filter) {
        File[] files = new File(getCrashReportsPath()).listFiles(filter);
        for (File file : files) {
            boolean deleted = file.delete();
            // TODO: ignored result of delete() call
        }
    }

    //

    private static String getCrashLogTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private static String getStackTrace(Throwable e) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);

        e.printStackTrace(printWriter);
        String crashLog = result.toString();
        printWriter.close();
        return crashLog;
    }

    public static String getCrashReportsPath() {
        File file = new File(CrashReporter.getApplicationContext().getExternalFilesDir(null), Constants.CRASH_REPORT_DIR);

        if (!file.exists()) {
            boolean created = file.mkdirs();
            if (!created) {
                // TODO: what happens if directory was not created?
                return null;
            }
        }
        return file.getAbsolutePath();
    }



}
