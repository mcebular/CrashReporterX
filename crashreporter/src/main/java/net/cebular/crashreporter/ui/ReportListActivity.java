package net.cebular.crashreporter.ui;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import net.cebular.crashreporter.R;
import net.cebular.crashreporter.adapter.MainPagerAdapter;
import net.cebular.crashreporter.utils.Constants;

public class ReportListActivity extends AppCompatActivity {

    private MainPagerAdapter mainPagerAdapter;
    private int selectedTabPosition = 0;

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.crash_reporter));
        toolbar.setSubtitle("App: " + getApplicationName());
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    //

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_report_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // menu actions are delegated to ReportsFragment
        return mainPagerAdapter.getItem(selectedTabPosition).onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                selectedTabPosition = position;
            }
            @Override public void onPageScrolled(int i, float v, int i1) { }
            @Override public void onPageScrollStateChanged(int i) { }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.EXTRA_LANDING)) {
            int reportType = intent.getIntExtra(Constants.EXTRA_LANDING, 0);
            if (reportType == Constants.REPORT_TYPE_EXCEPTION) {
                selectedTabPosition = 1;
            } else {
                selectedTabPosition = 0;
            }
        }
        viewPager.setCurrentItem(selectedTabPosition);
    }

    private String getApplicationName() {
        ApplicationInfo applicationInfo = getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : getString(stringId);
    }

}
