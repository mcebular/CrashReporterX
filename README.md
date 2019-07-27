# CrashReporter

This is a practically complete rewrite of [MindorksOpenSource](https://github.com/MindorksOpenSource/CrashReporter) crash reporter library.

[![](https://jitpack.io/v/mc0239/CrashReporterX.svg)](https://jitpack.io/#mc0239/CrashReporterX)
![API 19+](https://img.shields.io/badge/API-19%2B-informational)
![Apache 2.0 license](https://img.shields.io/badge/License-Apache%202.0-informational)
![Uses AndroidX](https://img.shields.io/badge/Uses-AndroidX-red)

![Sample screenshots](https://raw.githubusercontent.com/mc0239/CrashReporterX/master/screenshots.jpg)

## Why CrashReporter? 

While developing features we get crashes, and if device is not connected to logcat we miss the crash log. In worst case scenario we might not be able to reproduce the crash and end up wasting effort. This library captures all unhandled crashes and saves them locally on device. The purpose of this library is to use it as a debug feature to capture crashes locally and immediately.

Crashes are stored in path returned by `Context.getExternalFilesDir()`, thus requiring no special read/write permissions. You can get a path to this folder by calling `CrashUtils.getCrashReportsPath()` (in a normal scenario, this should point to `/Android/data/your-app-id/files/crash_reports`).

## Sample app

There's a sample app available in `app/` folder for you to try out CrashReporter. Clone this repository, open the project in Android Studio, and run the app.

## Usage

### Initilaizing CrashReporter

1. Add Jitpack to repositories in `build.gradle` and add ComboBox dependency to dependencies:

```gradle
repositories {
    // your other repos...
    maven { url "https://jitpack.io" }
}

dependencies {
    // your other deps...
    implementation 'com.github.mc0239:CrashReporter:{{latest-version}}'
}
```

2. Initialize CrashReporter in your Application class:

```java
public class CrashReporterSampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            CrashReporter.initialize(this);
        }
    }
}
```

3. Done! Now upon application crash, a crash report will be generated and stored to a file.

### Accessing Crash Reports via CrashReporter's activity

CrashReporter library comes with an Activity which enables viewing and sharing crash logs. You can access this activity by clicking on CrashReporter's notification after crash report has been made.

You can also access this activity directly, by retrieving CrashReporter's activity intent and calling `startActivity()`:

```java
Intent i = CrashReporter.getLaunchIntent(context);
startActivity(i);
```

**Note:** You _must_ call `CrashReporter.initialize()` in your application, otherwise calling `CrashReporter.getLaunchIntent()` will result in a crash.

### Reporting caught exceptions

While runtime exceptions that result in a crash are saved automatically, you can also save a report about an Exception that was caught. In the catch block, call `CrashReporter.reportException()` to save a crash report about the exception:

```java
try {
    // ...
    throwsException();
    // ...
} catch (Exception e) {
    CrashReporter.reportException(e);
}
```

**Note:** You _must_ call `CrashReporter.initialize()` in your application, otherwise calling `CrashReporter.reportException()` will result in a crash.

# License

```
Copyright (C) 2019 Martin Čebular
Copyright (C) 2016 Bal Sikandar
Copyright (C) 2011 Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
