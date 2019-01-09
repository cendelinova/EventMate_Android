package gr.tei.erasmus.pp.eventmate.ui.submission

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.SubmissionFile
import kotlinx.android.synthetic.main.submission_item.view.*
import org.ocpsoft.prettytime.PrettyTime
import timber.log.Timber
import java.util.*

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
			setOnClickListener {
				if (submission_item.getChildAt(0).id == R.id.view_background) {
					submissionListener.onSubmissionClick(this, true)
				} else {
					submissionListener.onSubmissionClick(this, false)
				}
			}
			
			submission_icon.setImageResource(SubmissionFile.FileType.valueOf(submissionFile.type).icon)
			submission_icon.setOnClickListener { submissionListener.onSubmissionIconClick(submissionFile) }
			
			submission_name.text = submissionFile.name ?:
					context.getString(SubmissionFile.FileType.valueOf(submissionFile.type).defaultString)
			
			submissionFile.comment?.let {
				submission_description.text = it
			}
			
			created.text = PrettyTime().apply { locale = Locale.ENGLISH }.format(submissionFile.created)
			
			btn_delete.setOnClickListener { submissionListener.onSubmissionDelete(submissionFile) }
			btn_download.setOnClickListener { submissionListener.onSubmissionDownload(submissionFile) }
			btn_view.setOnClickListener { submissionListener.onSubmissionView(submissionFile) }
		}
		
	}
	
	fun updateSubmissionList(newList: MutableList<SubmissionFile>) {
		submissionFiles = newList
		notifyDataSetChanged()
	}
	
	inner class SubmissionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	interface SubmissionListener {
		fun onSubmissionView(submissionFile: SubmissionFile)
		fun onSubmissionDownload(submissionFile: SubmissionFile)
		fun onSubmissionDelete(submissionFile: SubmissionFile)
		fun onSubmissionClick(itemView: View, slideIn: Boolean)
		fun onSubmissionIconClick(submissionFile: SubmissionFile)
	}
}