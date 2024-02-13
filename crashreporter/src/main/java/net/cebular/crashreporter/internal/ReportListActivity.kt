package net.cebular.crashreporter.internal

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import net.cebular.crashreporter.R
import net.cebular.crashreporter.databinding.ActivityReportListBinding

class ReportListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportListBinding
    private lateinit var reportsAdapter: ReportsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReportListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = getString(R.string.crash_reporter)
        binding.toolbar.subtitle = getApplicationName()

        reportsAdapter = ReportsAdapter { reportFile ->
            ReportDetailActivity.launchActivity(this@ReportListActivity, reportFile.name)
        }

        binding.recyclerViewReports.apply {
            layoutManager = LinearLayoutManager(this@ReportListActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(this@ReportListActivity, LinearLayoutManager.VERTICAL))
            adapter = reportsAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        loadReports()
    }

    //

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_report_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_clear_logs -> {
                AlertDialog.Builder(this)
                    .setTitle("Clear logs?")
                    .setMessage("This will permanently remove all logs.")
                    .setPositiveButton(android.R.string.ok) { dialog, _ ->
                        clearReports()
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                        // Do nothing.
                    }
                    .show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    //

    private fun loadReports() {
        getCrashReportsDir()?.listFiles()?.let {
            reportsAdapter.setReports(it.sortedByDescending { f -> f.name })
        }
    }

    private fun clearReports() {
        getCrashReportsDir()?.listFiles()?.forEach {
            it.delete()
        }
        loadReports()
    }

    private fun Context.getApplicationName(): String {
        return applicationInfo.let {
            if (it.labelRes == 0) {
                it.nonLocalizedLabel.toString()
            } else {
                getString(it.labelRes)
            }
        }
    }

}