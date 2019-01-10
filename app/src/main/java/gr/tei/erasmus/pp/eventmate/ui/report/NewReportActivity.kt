package gr.tei.erasmus.pp.eventmate.ui.report

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.*
import gr.tei.erasmus.pp.eventmate.helpers.DialogHelper
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.showError
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.toggleProgress
import gr.tei.erasmus.pp.eventmate.helpers.TextInputLayoutHelper
import gr.tei.erasmus.pp.eventmate.ui.base.*
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserViewModel
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.tasks.TasksViewModel
import kotlinx.android.synthetic.main.activity_new_report.*


class NewReportActivity : BaseActivity() {
	
	private val listOfGuestIds by lazy { mutableListOf<Long>() }
	private val listOfTaskIds by lazy { mutableListOf<Long>() }
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(ReportViewModel::class.java) }
	
	private lateinit var users: MutableList<User>
	
	private lateinit var tasks: MutableList<Task>
	
	private var eventReportInfo = EventReportInfo()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_new_report)
		setupToolbar(toolbar)
		
		observeViewModel()
		// todo get real eventId
		viewModel.getEventGuests(5)
		viewModel.getEventTasks(5)
		setupListeners()
		handleReportCreation()
	}
	
	private fun handleReportCreation() {
		btn_generate_report.setOnClickListener {
			val name = TextInputLayoutHelper.collectValueFromInput(input_report_name)
			val comment = TextInputLayoutHelper.collectValueFromInput(input_report_comment)
			val type = when (report_category.checkedRadioButtonId) {
				R.id.full_report -> ReportResponse.ReportType.FULL_SUMMARY.name
				R.id.certificate -> ReportResponse.ReportType.CERTIFICATE.name
				else -> ""
			}
			
			eventReportInfo.listOfIncludedGuests = listOfGuestIds
			eventReportInfo.listOfIncludedTasks = listOfTaskIds
			
			viewModel.saveEventReport(11, ReportRequest(name, comment, type, eventReportInfo))
		}
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeReportState)
		}
	}
	
	private fun setupListeners() {
		tv_show_event_info.setOnClickListener {
			DialogHelper.showEventReportDialog(
				this,
				layoutInflater,
				eventReportInfo,
				eventReportListener
			)
		}
		
		tv_display_guests.setOnClickListener {
			val reportGuestAdapter = ReportGuestAdapter(
				this@NewReportActivity,
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
				this@NewReportActivity,
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
		
		show_event_info.setOnCheckedChangeListener { _, isChecked ->
			with(eventReportInfo) {
				includeDate = isChecked
				includeName = isChecked
				includeOwner = isChecked
				includeReportCreatedDate = isChecked
				includePlace = isChecked
				includeReportCreator = isChecked
				
			}
			
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
			is FinishedState -> {
				StateHelper.toggleProgress(progress, false)
				Toast.makeText(this, R.string.success_report_create, Toast.LENGTH_LONG).show()
				startActivity(Intent(this, ReportListActivity::class.java))
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
	
	private val eventReportListener = DialogInterface.OnClickListener { dialog, _ ->
		(dialog as AlertDialog).findViewById<LinearLayout>(R.id.checkboxes)?.run {
			for (i in 0 until childCount) {
				val v = getChildAt(i)
				if (v is CheckBox) {
					with(v) {
						when (id) {
							R.id.event_name -> eventReportInfo.includeName = isChecked
							R.id.event_date -> eventReportInfo.includeDate = isChecked
							R.id.event_place -> eventReportInfo.includePlace = isChecked
							R.id.event_owner -> eventReportInfo.includeOwner = isChecked
							R.id.report_creator -> eventReportInfo.includeReportCreator = isChecked
							R.id.report_created_date -> eventReportInfo.includeReportCreatedDate = isChecked
						}
						show_event_info.isChecked = true
					}
					
				}
			}
		}
	}
	
	private fun prepareEventReportInfo(): EventReportInfo {
		return EventReportInfo()
	}
	
}

