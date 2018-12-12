package gr.tei.erasmus.pp.eventmate.ui.eventDetail.tasks

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.showError
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.toggleProgress
import gr.tei.erasmus.pp.eventmate.ui.base.BaseFragment
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import kotlinx.android.synthetic.main.fragment_tasks.*
import timber.log.Timber

class TasksFragment : BaseFragment() {
	
	private lateinit var taskAdapter: TaskAdapter
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(TasksViewModel::class.java) }
	
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_tasks, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeRecyclerView()
		observeViewModel()
		viewModel.getTasks()
	}
	
	/**
	 * We have blacklist items obtained, initialize recyclerView and display them.
	 */
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		taskAdapter = TaskAdapter(
			context!!,
			onTaskClick,
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
	
	
	private val onTaskClick = object :
		TaskAdapter.TaskListener {
		override fun onItemClick(task: Task) {
			//todo show task detail
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

//	private fun handleFabBtn() {
//		fab.setOnClickListener {
//			startActivity(Intent(activity, NewTaskActivity::class.java))
//		}
//	}
	
	
}
