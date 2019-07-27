package net.iamsilver.crashreporter.utils;

/**
 * Created by bali on 15/08/17.
 */

@SuppressWarnings("WeakerAccess")
public class Constants {

    public static final int REPORT_TYPE_EXCEPTION = 2;
    public static final int REPORT_TYPE_CRASH = 1;

    public static final String ARG_REPORT_TYPE = "reports_type";
    public static final String EXTRA_LANDING = "intent_landing";
    public static final String EXTRA_FILE_PATH = "file_path";

    public static final String CRASH_REPORT_DIR = "crash_reports";
    public static final String EXCEPTION_SUFFIX = "_exception";
    public static final String CRASH_SUFFIX = "_crash";
    public static final String FILE_EXTENSION = ".txt";

    public static final String PREFERENCE_INSTALL_ID = "CrashReporterInstallID";

}
