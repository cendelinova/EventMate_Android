package gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.tasks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import gr.tei.erasmus.pp.eventmate.helpers.TextHelper
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
			
			val taskState = Task.TaskState.valueOf(task.taskState)
			task_name.text = task.name
			task_points.text = task.points.toString()
			task_description.text = task.description
			task_state.text = "[" + context.getString(taskState.taskItemStatusMessage) + "]"
			
			if (collapsing_view.visibility == View.VISIBLE) {
				task_location.text = TextHelper.getDefaultTextIfEmpty(task.place)
//				task_limit.text = TextHelper.getDefaultTextIfEmpty(task.timeLimit.toString())
			}
			
			if (!task.assignees.isNullOrEmpty()) {
				addAssignees(this, task.assignees.toMutableList())
				TextHelper.createContactChips(task.assignees.toMutableList(), chip_group, null, false)
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
	
	private fun addAssignees(itemView: View, assignees: MutableList<User>) {
		val photoSize = context.resources.getDimension(R.dimen.icon_size).toInt()
		val marginEnd = context.resources.getDimension(R.dimen.spacing_tiny).toInt()
		var counter = 0
		assignees.forEachIndexed {index, user ->
			counter++
			if (counter == 3) return
			val assigneeImage = CircleImageView(context).apply {
				layoutParams = LinearLayout.LayoutParams(photoSize, photoSize, 0f).also {
					it.marginEnd = marginEnd
				}
			}
			
			if (user.photo == null) {
				assigneeImage.setImageResource(R.drawable.ic_user_placeholder)
			} else {
				assigneeImage.setImageBitmap(FileHelper.decodeImage(user.photo))
			}
			
			itemView.assignees_list.addView(assigneeImage)
			
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