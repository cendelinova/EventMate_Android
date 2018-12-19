package gr.tei.erasmus.pp.eventmate.ui.eventDetail.tasks

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.EVENT_ID
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.TASK_ID
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.showError
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.toggleProgress
import gr.tei.erasmus.pp.eventmate.ui.base.BaseFragment
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import gr.tei.erasmus.pp.eventmate.ui.taskDetail.TaskDetailActivity
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.task_item.*
import kotlinx.android.synthetic.main.task_item_expanded.*
import timber.log.Timber

class TasksFragment : BaseFragment() {
	
	private lateinit var taskAdapter: TaskAdapter
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(TasksViewModel::class.java) }
	
	private var eventId: Long? = null
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		eventId = arguments?.getLong(EVENT_ID)
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_tasks, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeRecyclerView()
		observeViewModel()
		
		eventId?.let {
			viewModel.getTasks(eventId!!)
		}
	}
	
	/**
	 * We have blacklist items obtained, initialize recyclerView and display them.
	 */
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		taskAdapter = TaskAdapter(
			context!!,
			taskItemListener,
			mutableListOf()
		)
		
		with(task_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(tasks_empty_view)
			layoutManager = LinearLayoutManager(context!!)
			adapter = taskAdapter
		}
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeTaskProgressState)
		}
	}
	
	
	private val taskItemListener = object :
		TaskAdapter.TaskListener {
		override fun onToggleLayout(expand: Boolean) {
			if (expand) {
				expand_row.visibility = View.INVISIBLE
				collapsed_view.visibility = View.VISIBLE
			} else {
				expand_row.visibility = View.VISIBLE
				collapsed_view.visibility = View.GONE
			}
		}
		
		override fun onTaskClick(task: Task) {
			startActivity(Intent(activity, TaskDetailActivity::class.java).apply {
				putExtra(TASK_ID, task.id)
			})
		}
		
	}
	
	// Observer
	private val observeTaskProgressState = Observer<State> { state ->
		when (state) {
			is LoadingState -> toggleProgress(progress, true)
			is ErrorState -> showError(state.error, progress, tasks_fragment)
			is TasksViewModel.TaskListState -> {
				toggleProgress(progress, false)
				taskAdapter.updateTaskList(state.tasks)
			}
		}
	}
	
}
