package net.cebular.crashreporter.internal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import net.cebular.crashreporter.R
import net.cebular.crashreporter.databinding.ActivityReportDetailBinding
import java.io.File

class ReportDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_REPORT_FILENAME = "reportFilename"

        fun launchActivity(context: Context, reportFilename: String) {
            val intent = Intent(context, ReportDetailActivity::class.java)
            intent.putExtra(EXTRA_REPORT_FILENAME, reportFilename)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityReportDetailBinding

    private var reportFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReportDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.title = getString(R.string.report_detail_activity_title)

        val ok = loadReportFromIntent()
        if (!ok) {
            binding.textViewStackTrace.text = "Ouch! This report does not exist anymore."
        }
    }

    //

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_report_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_report -> {
                reportFile?.delete()
                finish()
                true
            }

            R.id.action_share_report -> {
                shareReport()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    //

    private fun loadReportFromIntent(): Boolean {
        if (intent == null) {
            return false
        }

        val reportFilename = intent.getStringExtra(EXTRA_REPORT_FILENAME)
            ?: return false

        reportFile = File(getCrashReportsDir(), reportFilename)
        reportFile?.let {
            if (!it.exists()) {
                return false
            }

            binding.textViewStackTrace.text = it.readText()
            binding.textViewDeviceInfo.text = getDeviceInformation()
        }

        return true
    }

    private fun shareReport() {
        // TODO
        /*

        // TODO: fix this with FileProvider
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("* / *");
        intent.putExtra(Intent.EXTRA_TEXT, textViewDeviceInfo.getText().toString());
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(reportFile));
        startActivity(Intent.createChooser(intent, "Share via"));
        */
    }
}