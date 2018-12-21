package gr.tei.erasmus.pp.eventmate.ui.assignPoints

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_assign_points.*
import kotlinx.android.synthetic.main.toolbar_event_detail.*
import timber.log.Timber

class AssignPointsActivity : BaseActivity() {
	
	private lateinit var assignPointAdapter: AssignPointAdapter
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(AssignPointViewModel::class.java) }
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_assign_points)
		
		setupToolbar(toolbar)
		
		initializeRecyclerView()
	}
	
	/**
	 * We have blacklist items obtained, initialize recyclerView and display them.
	 */
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		assignPointAdapter = AssignPointAdapter(this, 10, mutableListOf(User("Bill Smith"), User("James Trats")))
		
		with(assign_points_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(assignees_empty_view)
			layoutManager = LinearLayoutManager(context!!)
			adapter = assignPointAdapter
		}
	}
}
