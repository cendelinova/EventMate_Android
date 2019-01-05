package gr.tei.erasmus.pp.eventmate.ui.report

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.helpers.ImageHelper
import kotlinx.android.synthetic.main.report_task_item.view.*
import timber.log.Timber

class ReportTaskAdapter(
	private val context: Context,
	private val reportListener: ReportListener?,
	private var tasks: MutableList<Task>
) : RecyclerView.Adapter<ReportTaskAdapter.ReportTaskViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		ReportTaskViewHolder(LayoutInflater.from(context).inflate(R.layout.report_task_item, parent, false))
	
	override fun getItemCount() = tasks.size
	
	override fun onBindViewHolder(holder: ReportTaskViewHolder, position: Int) {
		Timber.v("onBindViewHolder() called with: holder = [$holder], position = [$position]")
		displayReportEntry(holder, tasks[holder.adapterPosition])
	}
	
	private fun displayReportEntry(viewHolder: ReportTaskViewHolder, task: Task) {
		with(viewHolder.itemView) {
			task_name.text = task.name
			task.photo?.let {
				task_photo.setImageBitmap(ImageHelper.getImageFromString(it))
			}
			setOnClickListener {
				// todo whole row changes checkbox
				checkbox.isChecked = !checkbox.isChecked
			}
			
			checkbox.isChecked = task.checked
			checkbox.setOnCheckedChangeListener { _, isChecked ->
				task.checked = isChecked
				reportListener?.onReportTaskPick(task.id, isChecked)
			}
		}
		
	}
	
	inner class ReportTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	interface ReportListener {
		fun onReportTaskPick(taskId: Long?, isChecked: Boolean)
	}
}