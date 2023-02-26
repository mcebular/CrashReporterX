package net.cebular.crashreporter.adapter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import net.cebular.crashreporter.ui.ReportsFragment;
import net.cebular.crashreporter.utils.Constants;

/**
 * Created by bali on 11/08/17.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    private ReportsFragment crashesFragment, exceptionsFragment;
    private String[] titles;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);

        Bundle args;

        this.crashesFragment = new ReportsFragment();
        args = new Bundle(); args.putInt(Constants.ARG_REPORT_TYPE, Constants.REPORT_TYPE_CRASH);
        this.crashesFragment.setArguments(args);

        this.exceptionsFragment = new ReportsFragment();
        args = new Bundle(); args.putInt(Constants.ARG_REPORT_TYPE, Constants.REPORT_TYPE_EXCEPTION);
        this.exceptionsFragment.setArguments(args);

        this.titles = new String[]{"Crashes", "Exceptions"};
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return crashesFragment;
        } else if (position == 1) {
            return exceptionsFragment;
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}