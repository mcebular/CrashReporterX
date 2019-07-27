package net.iamsilver.crashreporter.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.iamsilver.crashreporter.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener onItemClickListener;
    private ArrayList<File> reports;

    //

    public ReportsAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener= onItemClickListener;
        reports = new ArrayList<>();
    }

    //

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_reports_adapter, viewGroup, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final File file = reports.get(position);

        viewHolder.textViewReportTime.setText(file.getName().replaceAll("[a-zA-Z_.]", ""));
        viewHolder.textViewReportSummary.setText(readFirstLine(file));

        final int pos = position;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(v, pos, file);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    //

    public void setReports(ArrayList<File> reports) {
        this.reports.clear();
        this.reports.addAll(reports);
        notifyDataSetChanged();
    }

    //

    private static String readFirstLine(File file) {
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            line = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    //

    public interface OnItemClickListener {
        void onClick(View view, int position, File reportFile);
    }

    //

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewReportTime, textViewReportSummary;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewReportTime = itemView.findViewById(R.id.textViewReportTime);
            textViewReportSummary = itemView.findViewById(R.id.textViewReportSummary);
        }

    }

}
