package gr.tei.erasmus.pp.eventmate.ui.report

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import gr.tei.erasmus.pp.eventmate.ui.base.AbstractFilterAdapter
import kotlinx.android.synthetic.main.report_task_item.view.*
import timber.log.Timber

class ReportTaskAdapter(
	private val context: Context,
	private val reportListener: ReportListener?,
	private var tasks: MutableList<Task>
) : AbstractFilterAdapter() {
	
	private var filteredTasks = tasks
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		ReportTaskViewHolder(LayoutInflater.from(context).inflate(R.layout.report_task_item, parent, false))
	
	override fun getItemCount() = filteredTasks.size
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		Timber.v("onBindViewHolder() called with: holder = [$holder], position = [$position]")
		displayReportEntry(holder, filteredTasks[holder.adapterPosition])
	}
	
	override fun getFilter() = TaskFilter()
	
	private fun displayReportEntry(viewHolder: RecyclerView.ViewHolder, task: Task) {
		with(viewHolder.itemView) {
			task_name.text = task.name
			task.photo?.let {
				task_photo.setImageBitmap(FileHelper.decodeImage(it))
			}
			setOnClickListener {
				checkbox.isChecked = !checkbox.isChecked
			}
			
			checkbox.isChecked = task.checked
			checkbox.setOnCheckedChangeListener { _, isChecked ->
				task.checked = isChecked
				reportListener?.onReportTaskPick(task.id, isChecked)
			}
		}
		
	}
	
	inner class TaskFilter : Filter() {
		override fun performFiltering(constraint: CharSequence): Filter.FilterResults {
			filteredTasks = if (constraint.isEmpty()) {
				tasks
			} else {
				tasks.filter { task -> task.name.contains(constraint.toString().toLowerCase()) }
					.toMutableList()
			}
			
			return Filter.FilterResults().apply {
				values = filteredTasks
			}
		}
		
		override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
			filteredTasks = results.values as MutableList<Task>
			notifyDataSetChanged()
		}
		
	}
	
	inner class ReportTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	interface ReportListener {
		fun onReportTaskPick(taskId: Long?, isChecked: Boolean)
	}
}