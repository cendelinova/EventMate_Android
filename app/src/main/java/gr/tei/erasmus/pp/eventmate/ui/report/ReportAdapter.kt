package gr.tei.erasmus.pp.eventmate.ui.report

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.Report
import timber.log.Timber

class ReportAdapter(
	private val context: Context,
	private val reportListener: ReportListener,
	private var reports: MutableList<Report>
) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		ReportViewHolder(LayoutInflater.from(context).inflate(R.layout.report_item, parent, false))
	
	override fun getItemCount() = reports.size
	
	override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
		Timber.v("onBindViewHolder() called with: holder = [$holder], position = [$position]")
		displayReportEntry(holder, reports[holder.adapterPosition])
	}
	
	private fun displayReportEntry(viewHolder: ReportViewHolder, report: Report) {
		with(viewHolder.itemView) {
		
		}
	}
	
	inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	interface ReportListener {
		fun onItemClick(report: Report)
	}
}