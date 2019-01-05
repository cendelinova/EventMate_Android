package gr.tei.erasmus.pp.eventmate.ui.report

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.Report
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_report_list.*
import kotlinx.android.synthetic.main.report_item.view.*
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
			startActivity(Intent(this, ReportMarkerActivity::class.java))
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
			mutableListOf(
				Report("Certificate for Tim", "awesome party", null),
				Report("More certificate", "blbba", null)
			)
		)
		
		with(reports_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(reports_empty_view)
			layoutManager = LinearLayoutManager(context!!)
			adapter = reportAdapter
		}
	}
	
	private val onReportClick = object : ReportAdapter.ReportListener {
		override fun onReportShare(report: Report) {
			Timber.d("onReportShare called")
		}
		
		override fun onReportDownload(report: Report) {
			Timber.d("onReportDownload called")
			
		}
		
		override fun onReportDelete(report: Report) {
			Timber.d("onReportDelete called")
			
		}
		
		override fun onReportClick(itemView: View, slideIn: Boolean) {
			if (slideIn) {
				YoYo.with(Techniques.SlideInLeft)
					.duration(700)
					.repeat(0)
					.onStart {
						itemView.view_background.bringToFront()
					}
					.playOn(itemView.view_background)
				
			} else {
				YoYo.with(Techniques.SlideOutRight)
					.duration(700)
					.repeat(0)
					.onEnd {
						itemView.view_foreground.bringToFront()
					}
					.playOn(itemView.view_background)
			}
		}
		
	}
}
