package net.cebular.crashreporter.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static net.cebular.crashreporter.utils.CrashUtils.getCrashReportsPath;

/**
 * Created by bali on 10/08/17.
 */

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    private FileUtils() {
        //this class is not publicly instantiable
    }

    public static void writeToFile(String filename, String content) {
        if (isExternalStorageWritable()) {
            // use external storage
            String reportFilePath = getCrashReportsPath() + File.separator + filename;

            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(reportFilePath));

                bufferedWriter.write(content);
                bufferedWriter.flush();
                bufferedWriter.close();
                Log.d(TAG, "Crash report saved to: " + filename);
            } catch (IOException e) {
                e.printStackTrace();
                // TODO: what to do when there's an error writing file?
            }
        } else {
            // TODO: what to do when external storage is not writeable?
        }
    }

    public static String readFromFile(File file) {
        StringBuilder crash = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                crash.append(line);
                crash.append('\n');
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return crash.toString();
    }

    //

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}
