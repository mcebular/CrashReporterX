package net.cebular.crashreporter.sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import net.cebular.crashreporter.CrashReporter;
import net.cebular.crashreporter.sample.databinding.ActivityMainBinding;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonNullPointer.setOnClickListener(v -> {
            throw new NullPointerException();
        });

        binding.buttonIndexOut.setOnClickListener(v -> {
            throw new IndexOutOfBoundsException();
        });

        binding.buttonClassCast.setOnClickListener(v -> {
            throw new ClassCastException();
        });

        binding.buttonSecurity.setOnClickListener(v -> {
            try {
                throw new SecurityException("Sample security exception");
            } catch (SecurityException e) {
                CrashReporter.reportException(MainActivity.this, e);
            }
        });

        binding.buttonIO.setOnClickListener(v -> {
            try {
                throw new IOException("Sample IO Exception");
            } catch (IOException e) {
                CrashReporter.reportException(MainActivity.this, e);
            }
        });

        binding.buttonOpenCrashLog.setOnClickListener(v -> {
            startActivity(CrashReporter.getLaunchIntent(MainActivity.this));
        });

    }
}
