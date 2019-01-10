package gr.tei.erasmus.pp.eventmate.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.DialogHelper
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.showError
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.toggleProgress
import gr.tei.erasmus.pp.eventmate.ui.base.*
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserViewModel
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.tasks.TasksViewModel
import kotlinx.android.synthetic.main.activity_report_marker.*


class ReportMarkerActivity : BaseActivity() {
	
	private val listOfGuestIds by lazy { mutableListOf<Long>() }
	private val listOfTaskIds by lazy { mutableListOf<Long>() }
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(ReportViewModel::class.java) }
	
	private lateinit var users: MutableList<User>
	
	private lateinit var tasks: MutableList<Task>
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_report_marker)
		setupToolbar(toolbar)
		
		observeViewModel()
		viewModel.getEventGuests(5)
		viewModel.getEventTasks(5)
		setupListeners()
	}
	
	private fun handleReportCreation() {
		btn_generate_report.setOnClickListener {
			// todo viewmodel create call
		}
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeReportState)
		}
	}
	
	private fun setupListeners() {
		tv_show_event_info.setOnClickListener {
			DialogHelper.showCustomDialog(
				this,
				LayoutInflater.from(this).inflate(R.layout.report_event_info_dialog, null),
				null
			)
		}
		
		tv_display_guests.setOnClickListener {
			val reportGuestAdapter = ReportGuestAdapter(
				this@ReportMarkerActivity,
				reportGuestListener, users
			)
			
			DialogHelper.showDialogWithAdapter(
				this, reportGuestAdapter,
				LayoutInflater.from(this).inflate(R.layout.report_pick_dialog, null),
				getString(R.string.msg_report_guests), confirmGuestListener, getQueryTextListener(reportGuestAdapter)
			)
		}
		
		tv_include_tasks.setOnClickListener {
			val reportTaskAdapter = ReportTaskAdapter(
				this@ReportMarkerActivity,
				reportTaskListener,
				tasks
			)
			DialogHelper.showDialogWithAdapter(
				this,
				reportTaskAdapter,
				LayoutInflater.from(this).inflate(R.layout.report_pick_dialog, null),
				getString(R.string.msg_report_tasks), confirmTasksListener, getQueryTextListener(reportTaskAdapter)
			)
		}
		
		include_tasks.setOnClickListener {
			if (!include_tasks.isChecked) {
				tasks.filter { task -> task.checked }.forEach { task -> task.checked = false }
				listOfTaskIds.clear()
				include_tasks.isChecked = false
			} else {
				listOfTaskIds.clear()
				tasks.forEach { task ->
					task.checked = true
					listOfTaskIds.add(task.id)
				}
				include_tasks.isChecked = true
			}
		}
		
		display_guests.setOnClickListener {
			if (!display_guests.isChecked) {
				users.filter { user -> user.checked }.forEach { user -> user.checked = false }
				listOfGuestIds.clear()
				display_guests.isChecked = false
			} else {
				listOfGuestIds.clear()
				users.forEach { user ->
					user.checked = true
					listOfGuestIds.add(user.id!!)
				}
				display_guests.isChecked = true
			}
		}
		
	}
	
	private val reportGuestListener = object : ReportGuestAdapter.ReportListener {
		override fun onReportGuestPick(userId: Long?, isChecked: Boolean) {
			userId?.run {
				if (isChecked) {
					listOfGuestIds.add(this)
				} else {
					listOfGuestIds.remove(this)
				}
			}
		}
	}
	
	private val reportTaskListener = object : ReportTaskAdapter.ReportListener {
		override fun onReportTaskPick(taskId: Long?, isChecked: Boolean) {
			taskId?.run {
				if (isChecked) {
					listOfTaskIds.add(this)
				} else {
					listOfTaskIds.remove(this)
				}
			}
		}
	}
	
	private val observeReportState = Observer<State> { state ->
		when (state) {
			is LoadingState -> toggleProgress(progress, true)
			is ErrorState -> showError(state.error, progress, main)
			is UserViewModel.UserListState -> {
				toggleProgress(progress, false)
				users = state.users
			}
			is TasksViewModel.TaskListState -> {
				toggleProgress(progress, false)
				tasks = state.tasks
			}
		}
	}
	
	private val confirmGuestListener = View.OnClickListener {
		display_guests.isChecked = !listOfGuestIds.isEmpty()
	}
	
	private val confirmTasksListener = View.OnClickListener {
		include_tasks.isChecked = !listOfTaskIds.isEmpty()
	}
	
	private fun getQueryTextListener(adapter: AbstractFilterAdapter): OnQueryTextListener =
		object : OnQueryTextListener {
			override fun onQueryTextSubmit(query: String?): Boolean {
				return false
			}
			
			override fun onQueryTextChange(newText: String?): Boolean {
				adapter.filter?.filter(newText)
				return false
			}
			
		}
	
}

