package gr.tei.erasmus.pp.eventmate.ui.submission

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.SubmissionFile
import kotlinx.android.synthetic.main.activity_assignee_submission_list.*
import timber.log.Timber

class AssigneeSubmissionListActivity : AppCompatActivity() {
	
	private lateinit var submissionAdapter: SubmissionAdapter
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_assignee_submission_list)
		
		initializeRecyclerView()
	}
	
	/**
	 * We have blacklist items obtained, initialize recyclerView and display them.
	 */
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		submissionAdapter = SubmissionAdapter(this, submissionListener, mutableListOf())
		
		with(submission_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(submission_empty_view)
			layoutManager = LinearLayoutManager(context!!)
			adapter = submissionAdapter
		}
	}
	
	private val submissionListener = object : SubmissionAdapter.SubmissionListener {
		override fun onReportShare(submissionFile: SubmissionFile) {
		
		}
		
		override fun onReportDownload(submissionFile: SubmissionFile) {
		}
		
		override fun onReportDelete(submissionFile: SubmissionFile) {
		}
		
	}
}
