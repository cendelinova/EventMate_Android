package gr.tei.erasmus.pp.eventmate.ui.assignPoints

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import gr.tei.erasmus.pp.eventmate.R
import kotlinx.android.synthetic.main.activity_assign_points.*
import timber.log.Timber

class AssignPointsActivity : AppCompatActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_assign_points)
		
		initializeRecyclerView()
	}
	
	/**
	 * We have blacklist items obtained, initialize recyclerView and display them.
	 */
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		eventAdapter = AssignPointAdapter(this, mutableListOf())
		
		with(assign_points_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(assignees_empty_view)
			layoutManager = LinearLayoutManager(context!!)
			adapter = eventAdapter
		}
	}
}
