package gr.tei.erasmus.pp.eventmate.ui.taskDetail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.TASK_ID
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_ID
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper
import gr.tei.erasmus.pp.eventmate.helpers.TextInputLayoutHelper.getDefaultTextIfEmpty
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import gr.tei.erasmus.pp.eventmate.ui.eventDetail.guests.GuestAdapter
import gr.tei.erasmus.pp.eventmate.ui.eventDetail.tasks.TasksViewModel
import gr.tei.erasmus.pp.eventmate.ui.userProfile.UserProfileActivity
import kotlinx.android.synthetic.main.activity_task_detail.*
import kotlinx.android.synthetic.main.toolbar_task_detail.*
import timber.log.Timber

class TaskDetailActivity : BaseActivity() {
	
	private var taskId: Long? = null
	
	private lateinit var guestAdapter: GuestAdapter
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(TasksViewModel::class.java) }
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_task_detail)
		
		setupToolbar(toolbar)
		taskId = intent.getLongExtra(TASK_ID, 0)
		
		observeViewModel()
		
		taskId?.let {
			viewModel.getTask(taskId!!)
		}
		
		initializeRecyclerView()
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeTaskProgressState)
		}
	}
	
	// Observer
	private val observeTaskProgressState = Observer<State> { state ->
		when (state) {
			is LoadingState -> StateHelper.toggleProgress(progress, true)
			is ErrorState -> StateHelper.showError(state.error, progress, task_detail)
			is TasksViewModel.TaskListState -> {
				StateHelper.toggleProgress(progress, false)
				setupLayout(state.tasks[0])
			}
		}
	}
	
	private val onUserClick = object : GuestAdapter.GuestListener {
		override fun onUserClick(user: User) {
			startActivity(Intent(this@TaskDetailActivity, UserProfileActivity::class.java).apply {
				putExtra(USER_ID, user.uid)
			})
		}
		
	}
	
	/**
	 * We have blacklist items obtained, initialize recyclerView and display them.
	 */
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		guestAdapter = GuestAdapter(
			this,
			onUserClick,
//			mutableListOf(User("blablik"), User("nanan"), User("daaaa"), User("daslsi"), User("sesmem"))
			mutableListOf()
		)
		
		with(assignee_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(assignee_empty_view)
			layoutManager = GridLayoutManager(context!!, 3)
			adapter = guestAdapter
		}
	}
	
	private fun setupLayout(task: Task) {
		with(task) {
			task_name.text = name
			tv_description.text = getDefaultTextIfEmpty(description)
			tv_time_limit.text = getDefaultTextIfEmpty(timeLimit?.toString())
			tv_location.text = getDefaultTextIfEmpty(place)
			tv_points.text = points.toString()
			
		}
	}
}
