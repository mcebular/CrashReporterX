package net.cebular.crashreporter.sample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import net.cebular.crashreporter.CrashReporter;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonNullPointer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new NullPointerException();
            }
        });

        findViewById(R.id.buttonIndexOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new IndexOutOfBoundsException();
            }
        });

        findViewById(R.id.buttonClassCast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new ClassCastException();
            }
        });

        findViewById(R.id.buttonSecurity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    throw new SecurityException("Sample security exception");
                } catch (SecurityException e) {
                    CrashReporter.reportException(e);
                }
            }
        });

        findViewById(R.id.buttonIO).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    throw new IOException("Sample IO Exception");
                } catch (IOException e) {
                    CrashReporter.reportException(e);
                }
            }
        });

        findViewById(R.id.buttonOpenCrashLog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CrashReporter.getLaunchIntent(MainActivity.this));
            }
        });

    }
}
