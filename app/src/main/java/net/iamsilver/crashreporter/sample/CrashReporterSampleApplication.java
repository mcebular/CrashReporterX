package net.iamsilver.crashreporter.sample;

import android.app.Application;

import net.iamsilver.crashreporter.CrashReporter;

/**
 * Created by bali on 02/08/17.
 */

public class CrashReporterSampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            // initialize reporter
            CrashReporter.initialize(this);
        }
    }
}
