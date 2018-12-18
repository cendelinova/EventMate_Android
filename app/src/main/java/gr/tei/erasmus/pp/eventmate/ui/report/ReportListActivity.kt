package gr.tei.erasmus.pp.eventmate.ui.report

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.Report
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_report_list.*
import kotlinx.android.synthetic.main.toolbar_event_detail.*
import timber.log.Timber

class ReportListActivity : BaseActivity() {
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(ReportViewModel::class.java) }
	
	private lateinit var reportAdapter: ReportAdapter
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_report_list)
		setupToolbar(toolbar)
		observeViewModel()
		handleAddFab()
		initializeRecyclerView()
	}
	
	private fun observeViewModel() {
//		with(viewModel) {
//			observe(states, observeEventProgressState)
//		}
	}
	
	private fun handleAddFab() {
		fab.setOnClickListener {
			//			startActivity(Intent(this, NewEventActivity::class.java))
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
			mutableListOf(Report("Certificate for Tim", "awesome party"), Report("More certificate", "blbba"))
		)
		
		with(reports_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(reports_empty_view)
			layoutManager = LinearLayoutManager(context!!)
			adapter = reportAdapter
		}
	}
	
	private val onReportClick = object : ReportAdapter.ReportListener {
		override fun onItemClick(report: Report) {
		}
		
	}
}
