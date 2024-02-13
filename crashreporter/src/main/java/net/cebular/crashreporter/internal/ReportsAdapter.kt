package net.cebular.crashreporter.internal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import net.cebular.crashreporter.databinding.ViewholderReportsAdapterBinding
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class ReportsAdapter(private val onItemClickListener: OnItemClickListener?) : RecyclerView.Adapter<ReportsAdapter.ViewHolder>() {

    fun interface OnItemClickListener {
        fun onClick(reportFile: File)
    }

    //

    class ViewHolder(private val binding: ViewholderReportsAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(file: File, onItemClickListener: OnItemClickListener?) {
            binding.textViewReportTime.text = file.name
            binding.textViewReportSummary.text = file.readFirstLine()

            binding.root.setOnClickListener {
                onItemClickListener?.onClick(file)
            }
        }

        fun File.readFirstLine(): String {
            var result = ""
            try {
                val reader = BufferedReader(FileReader(this))
                result = reader.readLine()
                reader.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return result
        }
    }

    //

    private val reports: MutableList<File> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ViewholderReportsAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return reports.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reports[position], onItemClickListener)
    }

    fun setReports(newReports: List<File>) {
        val diff = DiffUtil.calculateDiff(DiffUtilCallback(reports, newReports))
        reports.clear()
        reports.addAll(newReports)
        diff.dispatchUpdatesTo(this)
    }

    private class DiffUtilCallback(val prev: List<File>, val curr: List<File>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = prev.size
        override fun getNewListSize(): Int = curr.size
        override fun areItemsTheSame(prevItemPosition: Int, currItemPosition: Int): Boolean =
            prev[prevItemPosition].name == curr[currItemPosition].name
        override fun areContentsTheSame(prevItemPosition: Int, currItemPosition: Int): Boolean = false
    }

}