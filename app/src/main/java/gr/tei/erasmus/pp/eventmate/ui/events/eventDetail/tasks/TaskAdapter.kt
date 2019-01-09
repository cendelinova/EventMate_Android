package gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.tasks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.helpers.TextInputLayoutHelper
import kotlinx.android.synthetic.main.task_item.view.*
import kotlinx.android.synthetic.main.task_item_expanded.view.*
import timber.log.Timber

class TaskAdapter(
	private val context: Context,
	private val taskListener: TaskListener,
	private var tasks: MutableList<Task>
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
		Timber.d("onCreateViewHolder() called with parent = [$parent], viewType = [$viewType]")
		
		return TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.task_item, parent, false))
	}
	
	override fun getItemCount() = tasks.size
	
	override fun onBindViewHolder(viewHolder: TaskViewHolder, position: Int) {
		Timber.v("onBindViewHolder() called with: holder = [$viewHolder], position = [$position]")
		
		displayTaskEntry(viewHolder, tasks[viewHolder.adapterPosition])
	}
	
	private fun displayTaskEntry(viewHolder: TaskViewHolder, task: Task) {
		with(viewHolder.itemView) {
			view_foreground.setOnClickListener { taskListener.onTaskClick(task) }
			expand_row.setOnClickListener { handleToggle(this, true) }
			collapsing_view.setOnClickListener { handleToggle(this, false) }
			
			task_name.text = task.name
			task_points.text = task.points.toString()
			task_description.text = task.description
			
			if (collapsing_view.visibility == View.VISIBLE) {
				task_location.text = TextInputLayoutHelper.getDefaultTextIfEmpty(task.place)
				task_limit.text = TextInputLayoutHelper.getDefaultTextIfEmpty(task.timeLimit.toString())
			}
			
		}
	}
	
	private fun handleToggle(itemView: View, expand: Boolean) {
		with(itemView) {
			if (expand) {
				expand_row.visibility = View.INVISIBLE
				collapsed_view.visibility = View.VISIBLE
			} else {
				expand_row.visibility = View.VISIBLE
				collapsed_view.visibility = View.GONE
			}
		}
	}
	
	/**
	 * Update and notify data set change.
	 */
	fun updateTaskList(updatedTasks: MutableList<Task>) {
		tasks = updatedTasks
		notifyDataSetChanged()
	}
	
	/* Inner classes ******************************************************************************/
	inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	interface TaskListener {
		fun onTaskClick(task: Task)
	}
	
}