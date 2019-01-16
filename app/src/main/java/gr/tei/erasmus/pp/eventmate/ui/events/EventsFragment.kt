package gr.tei.erasmus.pp.eventmate.ui.events

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.constants.Constants
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.EVENT_ID
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.EVENT_SHOW_MENU
import gr.tei.erasmus.pp.eventmate.constants.Constants.EventFilter.*
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.Event.EventState.*
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.showError
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.toggleProgress
import gr.tei.erasmus.pp.eventmate.ui.base.*
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.EventDetailActivity
import gr.tei.erasmus.pp.eventmate.ui.events.newEvent.NewEventActivity
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivity
import kotlinx.android.synthetic.main.fragment_events.*
import timber.log.Timber


class EventsFragment : BaseFragment() {
	
	private lateinit var eventAdapter: EventAdapter
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(EventsViewModel::class.java) }
	private val sharedPreferenceHelper = App.COMPONENTS.provideSharedPreferencesHelper()
	private val userRoleHelper = App.COMPONENTS.provideUserRoleHelper()
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		Timber.v("onCreateView() called with: inflater = [$inflater], container = [$container], savedInstanceState = [$savedInstanceState]")
		
		setHasOptionsMenu(true)
		
		return inflater.inflate(R.layout.fragment_events, null)
	}
	
	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.menu_fragment_events, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == R.id.filter) showFilterDialog()
		return super.onOptionsItemSelected(item)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		(activity as MainActivity).setCustomTitle(getString(R.string.title_my_events))
		
		handleAddFab()
		initializeRecyclerView()
		observeViewModel()
		viewModel.getEvents()
		setupSwipeAction()
		prepareChips()
		
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeEventProgressState)
		}
	}
	
	private fun handleAddFab() {
		state_fab.setOnClickListener {
			startActivity(Intent(this.activity, NewEventActivity::class.java))
		}
	}
	
	private fun prepareChips() {
		filter_who.setOnCloseIconClickListener {
			val newList: MutableList<Event>
			if (filter_state.visibility == View.VISIBLE) {
				filters.visibility = View.VISIBLE
				
				newList = viewModel.filterEvents(
					UNDEFINED_FILTER,
					Event.EventState.valueOf(sharedPreferenceHelper.loadString(Constants.FILTER_EVENT_STATE))
				)
				
			} else {
				filters.visibility = View.GONE
				newList = viewModel.filterEvents(
					UNDEFINED_FILTER,
					UNDEFINED_STATE
				)
			}
			
			filter_who.visibility = View.GONE
			eventAdapter.updateEventList(newList)
		}
		
		filter_state.setOnCloseIconClickListener {
			val newList: MutableList<Event>
			
			if (filter_who.visibility == View.VISIBLE) {
				filters.visibility = View.VISIBLE
				
				newList = viewModel.filterEvents(
					Constants.EventFilter.valueOf(sharedPreferenceHelper.loadString(Constants.FILTER_EVENT_ROLE)),
					UNDEFINED_STATE
				)
				
			} else {
				filters.visibility = View.GONE
				newList = viewModel.filterEvents(
					UNDEFINED_FILTER,
					UNDEFINED_STATE
				)
			}
			
			filter_state.visibility = View.GONE
			eventAdapter.updateEventList(newList)
			
		}
	}
	
	private fun showFilterDialog() {
		val context = App.COMPONENTS.provideContext()
		val viewGroup = activity?.findViewById(android.R.id.content) as ViewGroup
		val dialog = LayoutInflater.from(context)
			.inflate(R.layout.filter_dialog, viewGroup, false)
		
		AlertDialog.Builder(activity!!).apply {
			setView(dialog)
			setPositiveButton(R.string.btn_confirm, applyFilterListener)
			setNegativeButton(R.string.btn_cancel, cancelFilterListener)
			setTitle(R.string.title_select_filters)
		}.also {
			it.create()
		}.show()
	}
	
	
	/**
	 * We have blacklist items obtained, initialize recyclerView and display them.
	 */
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		eventAdapter = EventAdapter(context!!, onEventClick, mutableListOf())
		
		with(event_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(events_empty_view)
			layoutManager = LinearLayoutManager(context!!)
			adapter = eventAdapter
		}
		
		swipe_layout.setOnRefreshListener {
			viewModel.getEvents(false)
		}
	}
	
	private fun setupSwipeAction() {
		Timber.v("setupSwipeAction() called")
		
		val swipeHandler = EventSwipeCallback(0, ItemTouchHelper.LEFT, swipeItemListener)
		ItemTouchHelper(swipeHandler).attachToRecyclerView(event_recycler_view)
	}
	
	/**
	 * User swiped left -> delete swiped contact from CallLog
	 */
	private fun deleteSwipe(viewHolder: RecyclerView.ViewHolder?) {
		if (viewHolder is EventAdapter.EventViewHolder) {
			val event = viewModel.getEventList()[viewHolder.adapterPosition]
			viewModel.deleteEvent(event.id)
		}
	}
	
	/* Listeners *******************************************************************************************************/
	
	private val onEventClick = object : EventListener {
		override fun onItemClick(event: Event) {
			startActivity(Intent(activity, EventDetailActivity::class.java).apply {
				putExtra(EVENT_ID, event.id)
				putExtra(
					EVENT_SHOW_MENU,
					event.state == Event.EventState.EDITABLE.name && userRoleHelper.isSameUser(event.eventOwner)
				)
			})
		}
	}
	
	private val swipeItemListener = object : SwipeItemHandler.RecyclerItemTouchHelperListener {
		override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
			deleteSwipe(viewHolder)
		}
	}
	
	private val applyFilterListener = DialogInterface.OnClickListener { dialog, _ ->
		val roleFilterId = (dialog as AlertDialog).findViewById<RadioGroup>(R.id.filter_who)?.checkedRadioButtonId
		val eventStateFilterId =
			(dialog as AlertDialog).findViewById<RadioGroup>(R.id.filter_event_status)?.checkedRadioButtonId
		
		if (roleFilterId == -1 && eventStateFilterId == -1) return@OnClickListener
		
		val roleFilter: Constants.EventFilter = roleFilterId?.run {
			when (this) {
				R.id.event_guest -> GUEST_FILTER
				R.id.event_owner -> OWNER_FILTER
				else -> UNDEFINED_FILTER
			}
		}!!
		
		val eventStateFilter: Event.EventState = eventStateFilterId?.run {
			when (this) {
				R.id.editable -> EDITABLE
				R.id.ready_to_play -> READY_TO_PLAY
				R.id.in_play -> IN_PLAY
				R.id.evaluation -> UNDER_EVALUATION
				R.id.finished -> FINISHED
				else -> UNDEFINED_STATE
			}
		}!!
		
		filters.visibility = View.VISIBLE
		
		if (roleFilter != UNDEFINED_FILTER) {
			filter_who.text = roleFilter.text
			filter_who.visibility = View.VISIBLE
		}
		
		if (eventStateFilter != UNDEFINED_STATE) {
			filter_state.text = getString(eventStateFilter.statusMessage)
			filter_state.visibility = View.VISIBLE
		}
		
		eventAdapter.updateEventList(viewModel.filterEvents(roleFilter, eventStateFilter))
		
	}
	
	
	private val cancelFilterListener = DialogInterface.OnClickListener { dialog, _ ->
		dialog.cancel()
	}
	
	// Observer
	private val observeEventProgressState = Observer<State> { state ->
		when (state) {
			is LoadingState -> toggleProgress(progress, true)
			is ErrorState -> showError(state.error, progress, events_fragment)
			is EventsViewModel.EventListState -> {
				toggleProgress(progress, false)
				swipe_layout.isRefreshing = false
				eventAdapter.updateEventList(state.events)
				state.deleteEventName?.let {
					Snackbar.make(
						events_fragment,
						getString(R.string.event_item_removed, it),
						Snackbar.LENGTH_LONG
					).show()
				}
			}
		}
	}
	
}