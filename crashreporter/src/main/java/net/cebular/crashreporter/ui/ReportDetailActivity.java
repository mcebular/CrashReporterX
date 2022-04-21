package net.cebular.crashreporter.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import net.cebular.crashreporter.R;
import net.cebular.crashreporter.utils.AppUtils;
import net.cebular.crashreporter.utils.Constants;
import net.cebular.crashreporter.utils.CrashUtils;

import java.io.File;

public class ReportDetailActivity extends AppCompatActivity {

    private TextView textViewDeviceInfo;
    private TextView textViewStackTrace;

    private File reportFile;

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        textViewDeviceInfo = findViewById(R.id.textViewDeviceInfo);
        textViewStackTrace = findViewById(R.id.textViewStackTrace);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        myToolbar.setTitle("Report");
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        if (intent != null) {
            reportFile = new File(intent.getStringExtra(Constants.EXTRA_FILE_PATH));
            // TODO textViewStackTrace.setText(FileUtils.readFromFile(reportFile));
        }

        textViewDeviceInfo.setText(AppUtils.getReporterInformation(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_report_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete_report) {
            boolean deleted = CrashUtils.deleteReport(reportFile);
            // TODO: ignored result of delete() call
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_share_report) {
            shareCrashReport(reportFile);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    //

    private void shareCrashReport(File reportFile) {

        // TODO: fix this with FileProvider
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_TEXT, textViewDeviceInfo.getText().toString());
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(reportFile));
        startActivity(Intent.createChooser(intent, "Share via"));
    }

}
