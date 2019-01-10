package gr.tei.erasmus.pp.eventmate.ui.report

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.ReportResponse
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import kotlinx.android.synthetic.main.report_item.view.*
import timber.log.Timber

class ReportAdapter(
	private val context: Context,
	private val reportListener: ReportListener,
	private var reports: MutableList<ReportResponse>
) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		ReportViewHolder(LayoutInflater.from(context).inflate(R.layout.report_item, parent, false))
	
	override fun getItemCount() = reports.size
	
	override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
		Timber.v("onBindViewHolder() called with: holder = [$holder], position = [$position]")
		displayReportEntry(holder, reports[holder.adapterPosition])
	}
	
	private fun displayReportEntry(viewHolder: ReportViewHolder, report: ReportResponse) {
		with(viewHolder.itemView) {
			setOnClickListener {
				if (report_item.getChildAt(0).id == R.id.view_background) {
					reportListener.onReportClick(this, true)
				} else {
					reportListener.onReportClick(this, false)
				}
			}
			report.preview?.let {
				report_photo.setImageBitmap(FileHelper.decodeImage(it))
			}
			
			val reportType = ReportResponse.ReportType.valueOf(report.type)
			
			report_type_icon.setImageResource(reportType.icon)
			
			report_name.text = report.name ?: reportType.name
			report_description.text = report.comment
			
			btn_delete.setOnClickListener { reportListener.onReportDelete(report) }
			btn_download.setOnClickListener { reportListener.onReportDownload(report) }
			btn_view.setOnClickListener { reportListener.onReportShare(report) }
			
		}
		
	}
	
	fun updateReportList(newReports: MutableList<ReportResponse>) {
		reports = newReports
		notifyDataSetChanged()
	}
	
	inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	interface ReportListener {
		fun onReportShare(report: ReportResponse)
		fun onReportDownload(report: ReportResponse)
		fun onReportDelete(report: ReportResponse)
		fun onReportClick(itemView: View, slideIn: Boolean)
	}
}