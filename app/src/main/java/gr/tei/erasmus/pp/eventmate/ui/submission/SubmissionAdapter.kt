package gr.tei.erasmus.pp.eventmate.ui.submission

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.SubmissionFile
import timber.log.Timber

class SubmissionAdapter(
	private val context: Context,
	private val submissionListener: SubmissionListener,
	private var submissionFiles: MutableList<SubmissionFile>
) : RecyclerView.Adapter<SubmissionAdapter.SubmissionViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		SubmissionViewHolder(LayoutInflater.from(context).inflate(R.layout.submission_item, parent, false))
	
	override fun getItemCount() = submissionFiles.size
	
	override fun onBindViewHolder(holder: SubmissionViewHolder, position: Int) {
		Timber.v("onBindViewHolder() called with: holder = [$holder], position = [$position]")
		displayReportEntry(holder, submissionFiles[holder.adapterPosition])
	}
	
	private fun displayReportEntry(viewHolder: SubmissionViewHolder, submissionFile: SubmissionFile) {
		with(viewHolder.itemView) {
			
		}
		
	}
	
	inner class SubmissionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	interface SubmissionListener {
		fun onReportShare(submissionFile: SubmissionFile)
		fun onReportDownload(submissionFile: SubmissionFile)
		fun onReportDelete(submissionFile: SubmissionFile)
	}
}