package net.iamsilver.crashreporter.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import net.iamsilver.crashreporter.R;
import net.iamsilver.crashreporter.adapter.ReportsAdapter;
import net.iamsilver.crashreporter.utils.Constants;
import net.iamsilver.crashreporter.utils.CrashUtils;

import java.io.File;
import java.util.ArrayList;

public class ReportsFragment extends Fragment {

    private Context context;
    private RecyclerView recyclerView;
    private ReportsAdapter reportsAdapter;

    //

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));

        reportsAdapter = new ReportsAdapter(context, new ReportsAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position, File reportFile) {
                Intent intent = new Intent(context, ReportDetailActivity.class);
                intent.putExtra(Constants.EXTRA_FILE_PATH, reportFile.getAbsolutePath());
                context.startActivity(intent);
            }
        });
        recyclerView.setAdapter(reportsAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadReports();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        context = null;
        super.onDetach();
    }

    //


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear_logs) {
            clearReports();
            loadReports();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    //

    private void loadReports() {
        ArrayList<File> reports = null;

        if (getArguments() != null) {
            int type = getArguments().getInt(Constants.ARG_REPORT_TYPE);
            if (type == Constants.REPORT_TYPE_CRASH) {
                reports = CrashUtils.getCrashReports();
            } else if (type == Constants.REPORT_TYPE_EXCEPTION) {
                reports = CrashUtils.getExceptionReports();
            }
        }
        if (reports == null) reports = CrashUtils.getReports();
        reportsAdapter.setReports(reports);
    }

    private void clearReports() {
        if (getArguments() != null) {
            int type = getArguments().getInt(Constants.ARG_REPORT_TYPE);
            if (type == Constants.REPORT_TYPE_CRASH) {
                CrashUtils.clearCrashReports();
            } else if (type == Constants.REPORT_TYPE_EXCEPTION) {
                CrashUtils.clearExceptionReports();
            }
        } else {
            CrashUtils.clearReports();
        }
    }
}
