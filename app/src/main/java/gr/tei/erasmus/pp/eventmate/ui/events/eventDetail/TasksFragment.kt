package gr.tei.erasmus.pp.eventmate.ui.events.eventDetail

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.ui.base.BaseFragment
import gr.tei.erasmus.pp.eventmate.ui.tasks.NewTaskActivity
import kotlinx.android.synthetic.main.fragment_tasks.*
import timber.log.Timber

class TasksFragment : BaseFragment() {
	
	private lateinit var taskAdapter: TaskAdapter
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_tasks, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeRecyclerView()
		handleFabBtn()
	}
	
	/**
	 * We have blacklist items obtained, initialize recyclerView and display them.
	 */
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		taskAdapter = TaskAdapter(context!!, onTaskClick, prepareTasks())
		
		with(task_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(tasks_empty_view)
			layoutManager = LinearLayoutManager(context!!)
			adapter = taskAdapter
		}
	}
	
	private fun prepareTasks(): MutableList<Task> {
		return mutableListOf(Task("Bartender selfie", 5, null), Task("Rakia shots", 10, null))
	}
	
	private val onTaskClick = object : TaskAdapter.TaskListener {
		override fun onItemClick(task: Task) {
			//todo show task detail
		}
		
	}
	
	private fun handleFabBtn() {
		fab.setOnClickListener {
			startActivity(Intent(activity, NewTaskActivity::class.java))
		}
	}
	
	
}
