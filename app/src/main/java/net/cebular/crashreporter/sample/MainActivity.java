package net.cebular.crashreporter.sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import net.cebular.crashreporter.CrashReporter;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonNullPointer).setOnClickListener(v -> {
            throw new NullPointerException();
        });

        findViewById(R.id.buttonIndexOut).setOnClickListener(v -> {
            throw new IndexOutOfBoundsException();
        });

        findViewById(R.id.buttonClassCast).setOnClickListener(v -> {
            throw new ClassCastException();
        });

        findViewById(R.id.buttonSecurity).setOnClickListener(v -> {
            try {
                throw new SecurityException("Sample security exception");
            } catch (SecurityException e) {
                CrashReporter.reportException(e);
            }
        });

        findViewById(R.id.buttonIO).setOnClickListener(v -> {
            try {
                throw new IOException("Sample IO Exception");
            } catch (IOException e) {
                CrashReporter.reportException(e);
            }
        });

        findViewById(R.id.buttonOpenCrashLog).setOnClickListener(v -> {
            startActivity(CrashReporter.getLaunchIntent());
        });

    }
}
