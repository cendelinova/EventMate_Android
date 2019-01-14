package gr.tei.erasmus.pp.eventmate.ui.assignPoints

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.data.model.UserSubmissionPoints
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.showError
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.toggleProgress
import gr.tei.erasmus.pp.eventmate.ui.base.*
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.tasks.TasksViewModel
import kotlinx.android.synthetic.main.activity_assign_points.*
import kotlinx.android.synthetic.main.toolbar_task_detail.*
import timber.log.Timber

class AssignPointsActivity : BaseActivity() {
	
	private lateinit var assignPointAdapter: AssignPointAdapter
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(AssignPointViewModel::class.java) }
	
	private var assignees = mutableListOf<User>()
	private var maxPoints = 0
	
	private val userSubmissionPointList = mutableListOf<UserSubmissionPoints>()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_assign_points)
		
		setupToolbar(toolbar)
		observeViewModel()
		// todo real
		viewModel.getTask(8)
		handleFab()
	}
	
	private fun handleFab() {
		state_fab.setOnClickListener {
			viewModel.assignPoints(8, userSubmissionPointList)
		}
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeAssignPointsState)
		}
	}
	
	private val observeAssignPointsState = Observer<State> { state ->
		when (state) {
			is LoadingState -> toggleProgress(progress, true)
			is ErrorState -> showError(state.error, progress, main)
			is FinishedState -> {
				toggleProgress(progress, false)
//				Toast.makeText(this, R.string.success_task_created, Toast.LENGTH_LONG).show()
//				startActivity(Intent(this, EventDetailActivity::class.java).apply {
//					putExtra(EVENT_ID, eventId)
//				})
			}
			is TasksViewModel.TaskListState -> {
				toggleProgress(progress, false)
				setupLayout(state.tasks[0])
			}
		}
	}
	
	private fun setupLayout(task: Task) {
		with(task) {
			task_name.text = name
			task_photo.setImageBitmap(photo?.let { FileHelper.decodeImage(it) })
			this@AssignPointsActivity.assignees = assignees?.toMutableList() ?: mutableListOf()
			this@AssignPointsActivity.maxPoints = points
		}
		initializeRecyclerView()
		assignPointAdapter.updateList(assignees)
		
	}
	
	/**
	 * We have blacklist items obtained, initialize recyclerView and display them.
	 */
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		assignPointAdapter = AssignPointAdapter(this, maxPoints, assignPointListener, assignees)
		
		with(assign_points_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(assignees_empty_view)
			layoutManager = LinearLayoutManager(context!!)
			adapter = assignPointAdapter
		}
	}
	
	private val assignPointListener = object : AssignPointAdapter.AssignPointListener {
		override fun onSetPoint(userId: Long, points: Int) {
			
			try {
				userSubmissionPointList.first { p -> p.idUser == userId }.also {
					it.points = points.toLong()
				}
			} catch (e: NoSuchElementException) {
				userSubmissionPointList.add(
					UserSubmissionPoints(userId, points.toLong())
				)
			}
			
		}
	}
}
