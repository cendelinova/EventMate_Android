package gr.tei.erasmus.pp.eventmate.ui.events

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import android.widget.RadioGroup
import android.widget.Toast
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.ui.base.*
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.EventDetailActivity
import gr.tei.erasmus.pp.eventmate.ui.events.newEvent.NewEventActivity
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivity
import kotlinx.android.synthetic.main.fragment_events.*
import timber.log.Timber


class EventsFragment : BaseFragment() {
	
	private lateinit var eventAdapter: EventAdapter
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(EventsViewModel::class.java) }
	
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		Timber.v("onCreateView() called with: inflater = [$inflater], container = [$container], savedInstanceState = [$savedInstanceState]")
		
		setHasOptionsMenu(true)
		return inflater.inflate(R.layout.fragment_events, null)
	}
	
	override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
		inflater?.inflate(R.menu.menu_fragment_events, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}
	
	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item?.itemId == R.id.filter) showFilterDialog()
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
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeEventProgressState)
		}
	}
	
	private fun handleAddFab() {
		fab.setOnClickListener {
			startActivity(Intent(this.activity, NewEventActivity::class.java))
		}
	}
	
	
	private fun showError(error: Throwable) {
		Timber.e("Error $error while fetching events")
		event_recycler_view.visibility = View.GONE
		toggleProgress(false)
		Snackbar.make(
			events_fragment,
			getString(R.string.loading_error),
			Snackbar.LENGTH_INDEFINITE
		).show()
//			.setAction(R.string.retry) {
//				startActivity(EventsFragment>().clearTop().clearTask().newTask())
//			}
//			.show()
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
	}
	
	private fun setupSwipeAction() {
		Timber.v("setupSwipeAction() called")
		
		val swipeHandler = EventSwipeCallback(0, ItemTouchHelper.LEFT, swipeItemListener)
		ItemTouchHelper(swipeHandler).attachToRecyclerView(event_recycler_view)
	}
	
	private fun toggleProgress(visibility: Boolean) {
		progress.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
	}
	
	/**
	 * User swiped left -> delete swiped contact from CallLog
	 */
	private fun deleteSwipe(viewHolder: RecyclerView.ViewHolder?) {
		if (viewHolder is EventAdapter.EventViewHolder) {
			
			val event = viewModel.getEventList()[viewHolder.adapterPosition]
			val deletedPosition = viewHolder.adapterPosition
			with(event) {
				Snackbar.make(
					events_fragment,
					getString(R.string.event_item_removed, name),
					Snackbar.LENGTH_LONG
				).apply {
					setAction(getString(R.string.undo)) {
						viewModel.addToEventList(
							this@with,
							deletedPosition
						)
					}
					setActionTextColor(ContextCompat.getColor(context, R.color.colorAccent))
				}.show()
				
				viewModel.deleteEvent(this)
			}
		}
	}
	
	
	/* Listeners *******************************************************************************************************/
	
	private val onEventClick = object : EventListener {
		override fun onItemClick(event: Event) {
			startActivity(Intent(activity, EventDetailActivity::class.java))
		}
	}
	
	private val swipeItemListener = object : SwipeItemHandler.RecyclerItemTouchHelperListener {
		override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
			deleteSwipe(viewHolder)
		}
	}
	
	private val applyFilterListener = DialogInterface.OnClickListener { dialog, _ ->
		Toast.makeText(
			activity,
			(dialog as AlertDialog).findViewById<RadioGroup>(R.id.filter_who)?.checkedRadioButtonId!!,
			Toast.LENGTH_LONG
		).show()
	}
	
	private val cancelFilterListener = DialogInterface.OnClickListener { dialog, _ ->
		dialog.cancel()
	}
	
	// Observer
	private val observeEventProgressState = Observer<State> { state ->
		when (state) {
			is LoadingState -> toggleProgress(true)
			is ErrorState -> showError(state.error)
			is EventsViewModel.EventListState -> {
				toggleProgress(false)
				eventAdapter.updateEventList(state.events)
			}
		}
	}
	
}