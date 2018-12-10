package gr.tei.erasmus.pp.eventmate.ui.events.eventDetail

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.Task
import kotlinx.android.synthetic.main.task_item.view.*
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
			view_foreground.setOnClickListener { taskListener.onItemClick(task) }
			task_name.text = task.name
			task_points.text = task.points.toString()
			task_description.text = task.description
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
		abstract fun onItemClick(task: Task)
	}
	
}