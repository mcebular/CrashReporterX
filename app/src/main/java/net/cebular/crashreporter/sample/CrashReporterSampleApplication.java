package net.cebular.crashreporter.sample;

import android.app.Application;

import net.cebular.crashreporter.CrashReporter;

public class CrashReporterSampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            CrashReporter.initialize(this, true);
        }
    }
}
