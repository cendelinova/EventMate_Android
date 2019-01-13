package gr.tei.erasmus.pp.eventmate.ui.taskDetail

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.EVENT_ID
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.SUBMISSION_EXTRA
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.TASK_ID
import gr.tei.erasmus.pp.eventmate.data.model.SubmissionExtra
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.DialogHelper
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper
import gr.tei.erasmus.pp.eventmate.helpers.TextHelper.getDefaultTextIfEmpty
import gr.tei.erasmus.pp.eventmate.ui.base.*
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.EventDetailActivity
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserAdapter
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.tasks.TasksViewModel
import gr.tei.erasmus.pp.eventmate.ui.newTask.NewTaskActivity
import gr.tei.erasmus.pp.eventmate.ui.submission.AssigneeSubmissionListActivity
import kotlinx.android.synthetic.main.activity_task_detail.*
import kotlinx.android.synthetic.main.toolbar_task_detail.*
import timber.log.Timber

class TaskDetailActivity : BaseActivity() {
	
	private var taskId: Long? = null
	
	private lateinit var assigneesAdapter: UserAdapter
	
	private lateinit var task: Task
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(TasksViewModel::class.java) }
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_task_detail)
		
		initializeRecyclerView()
		setupToolbar(toolbar)
		taskId = intent.getLongExtra(TASK_ID, 0)
		
		observeViewModel()
		
		taskId?.let {
			viewModel.getTask(it)
			viewModel.getTask(8)
		}
		
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeTaskProgressState)
		}
	}
	
	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		val inflater = menuInflater
		inflater.inflate(R.menu.menu_fragment_detail, menu)
		return super.onCreateOptionsMenu(menu)
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == R.id.edit) startActivity(
			Intent(
				this@TaskDetailActivity,
				NewTaskActivity::class.java
			).apply {
				putExtra(
					TASK_ID, taskId
				)
			})
		else if (item.itemId == R.id.delete) {
			DialogHelper.showDeleteDialog(this, DialogInterface.OnClickListener { _, _ ->
				taskId?.let {
					viewModel.deleteTask(it)
				}
			})
		}
		return super.onOptionsItemSelected(item)
	}
	
	// Observer
	private val observeTaskProgressState = Observer<State> { state ->
		when (state) {
			is LoadingState -> StateHelper.toggleProgress(progress, true)
			is ErrorState -> StateHelper.showError(state.error, progress, task_detail)
			is FinishedState -> {
				StateHelper.toggleProgress(progress, false)
				startActivity(Intent(this, EventDetailActivity::class.java).apply {
					putExtra(EVENT_ID, 5)
				})
			}
			is TasksViewModel.TaskListState -> {
				StateHelper.toggleProgress(progress, false)
				setupLayout(state.tasks[0])
				task = state.tasks[0]
			}
		}
	}
	
	private val onUserClick = object : UserAdapter.GuestListener {
		override fun onUserClick(user: User) {

//			startActivity(Intent(this@TaskDetailActivity, UserProfileActivity::class.java).apply {
//				putExtra(USER_ID, user.id)
//			})
			
			startActivity(Intent(this@TaskDetailActivity, AssigneeSubmissionListActivity::class.java).apply {
				putExtra(SUBMISSION_EXTRA, SubmissionExtra(user.id!!, task.id))
			})
		}
		
	}
	
	/**
	 * We have blacklist items obtained, initialize recyclerView and display them.
	 */
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		assigneesAdapter = UserAdapter(
			this,
			onUserClick,
			mutableListOf()
		)
		
		with(assignee_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(assignee_empty_view)
			layoutManager = GridLayoutManager(context!!, 3)
			adapter = assigneesAdapter
		}
	}
	
	private fun setupLayout(task: Task) {
		with(task) {
			task_name.text = name
			tv_description.text = getDefaultTextIfEmpty(description)
//			tv_time_limit.text = getDefaultTextIfEmpty(timeLimit?.toString())
			tv_location.text = getDefaultTextIfEmpty(place)
			tv_points.text = points.toString()
			photo?.let {
				task_photo.setImageBitmap(FileHelper.decodeImage(it))
			}
			
			if (!assignees.isNullOrEmpty()) {
				assigneesAdapter.updateUserList(assignees.toMutableList())
			}
		}
	}
}
