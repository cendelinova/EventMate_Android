package gr.tei.erasmus.pp.eventmate.ui.report

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.EVENT_ID
import gr.tei.erasmus.pp.eventmate.data.model.*
import gr.tei.erasmus.pp.eventmate.helpers.DialogHelper
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper
import gr.tei.erasmus.pp.eventmate.helpers.TextHelper
import gr.tei.erasmus.pp.eventmate.helpers.TextHelper.getQueryTextListener
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import gr.tei.erasmus.pp.eventmate.ui.events.EventsViewModel
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserViewModel
import kotlinx.android.synthetic.main.activity_report_list.*
import kotlinx.android.synthetic.main.report_item.view.*
import kotlinx.android.synthetic.main.share_report_dialog.view.*
import kotlinx.android.synthetic.main.toolbar_event_detail.*
import timber.log.Timber

class ReportListActivity : BaseActivity() {
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(ReportViewModel::class.java) }
	
	private lateinit var reportAdapter: ReportAdapter
	
	private lateinit var users: MutableList<User>
	private var eventId: Long? = null
	
	private val userEmails by lazy { mutableListOf<String>() }
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_report_list)
		setupToolbar(toolbar)
		observeViewModel()
		handleAddFab()
		initializeRecyclerView()
		
		eventId = intent.getLongExtra(Constants.EVENT_ID, 0)
		
		
		eventId?.let {
			viewModel.getEventReports(it)
			viewModel.getEventGuests(it)
			viewModel.getEvent(it)
		}
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeReportState)
		}
	}
	
	private fun handleAddFab() {
		state_fab.setOnClickListener {
			startActivity(Intent(this, NewReportActivity::class.java).apply {
				putExtra(EVENT_ID, eventId)
			})
		}
	}
	
	/**
	 * We have blacklist items obtained, initialize recyclerView and display them.
	 */
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		reportAdapter = ReportAdapter(
			this,
			onReportClick,
			mutableListOf()
		)
		
		with(reports_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(reports_empty_view)
			layoutManager = LinearLayoutManager(context!!)
			adapter = reportAdapter
		}
		
		swipe_layout.setOnRefreshListener {
			eventId?.let { viewModel.getEventReports(it, false) }
		}
	}
	
	private val onReportClick = object : ReportAdapter.ReportListener {
		override fun onReportPreviewClick(report: ReportResponse) {
			Timber.d("onReportPreviewClick called")
			DialogHelper.showDialogWithPhotoPreview(this@ReportListActivity, layoutInflater, null, report.preview)
		}
		
		override fun onReportShare(report: ReportResponse) {
			Timber.d("onReportShare called")
			val reportGuestAdapter = ReportGuestAdapter(
				this@ReportListActivity,
				reportGuestListener, users
			)
			val layout = LayoutInflater.from(this@ReportListActivity).inflate(R.layout.share_report_dialog, null)
			DialogHelper.showDialogWithAdapter(
				this@ReportListActivity, reportGuestAdapter,
				layout,
				getString(R.string.msg_share_reports), View.OnClickListener {
					if (userEmails.isNullOrEmpty()) return@OnClickListener
					report.id?.let {
						val subject = TextHelper.collectValueFromInput(layout.input_mail_subject)
						val text = TextHelper.collectValueFromInput(layout.input_mail_text)
						viewModel.shareReport(report.id, Email(subject, text, userEmails))
					}
				}, getQueryTextListener(reportGuestAdapter)
			)
		}
		
		override fun onReportDownload(report: ReportResponse) {
			Timber.d("onReportDownload called")
			report.id?.let { report.content?.let { it1 -> report.name?.let { it2 ->
				viewModel.saveFileLocally(this@ReportListActivity, it1,
					it2
				)
			} } }
		}
		
		override fun onReportDelete(report: ReportResponse) {
			Timber.d("onReportDelete called")
			report.id?.let { id ->
				DialogHelper.showDeleteDialog(
					this@ReportListActivity,
					DialogInterface.OnClickListener { _, _ ->
						viewModel.deleteReport(id)
					})
			}
		}
		
		override fun onReportClick(itemView: View, slideIn: Boolean) {
			if (slideIn) {
				YoYo.with(Techniques.SlideInLeft)
					.duration(700)
					.repeat(0)
					.onStart {
						itemView.view_background.visibility = View.VISIBLE
						itemView.view_background.bringToFront()
					}
					.playOn(itemView.view_background)
				
			} else {
				YoYo.with(Techniques.SlideOutRight)
					.duration(700)
					.repeat(0)
					.onEnd {
						itemView.view_foreground.bringToFront()
						itemView.view_background.visibility = View.GONE
					}
					.playOn(itemView.view_background)
			}
		}
		
	}
	
	private val observeReportState = Observer<State> { state ->
		when (state) {
			is LoadingState -> StateHelper.toggleProgress(progress, true)
			is ErrorState -> StateHelper.showError(state.error, progress, main)
			is ReportViewModel.ReportState -> {
				swipe_layout.isRefreshing = false
				StateHelper.toggleProgress(progress, false)
				reportAdapter.updateReportList(state.reports)
			}
			is UserViewModel.UserListState -> {
				StateHelper.toggleProgress(progress, false)
				users = state.users
			}
			is EventsViewModel.EventList2State -> {
				StateHelper.toggleProgress(progress, false)
				setupHeader(state.events[0])
			}
		}
	}
	
	private fun setupHeader(event: Event) {
		with(event) {
			event_name.text = name
			photo?.let {
				event_photo.setImageBitmap(FileHelper.decodeImage(it))
			}
		}
	}
	
	private val reportGuestListener = object : ReportGuestAdapter.ReportListener {
		override fun onReportGuestPick(user: User, isChecked: Boolean) {
			if (isChecked) {
				userEmails.add(user.email)
			} else {
				userEmails.remove(user.email)
			}
		}
	}
	
}
