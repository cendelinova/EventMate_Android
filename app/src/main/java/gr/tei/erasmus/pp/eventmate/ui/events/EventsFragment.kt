package gr.tei.erasmus.pp.eventmate.ui.events

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.AdapterView
import android.widget.Toast
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.models.Event
import gr.tei.erasmus.pp.eventmate.ui.base.BaseFragment
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivity
import kotlinx.android.synthetic.main.fragment_events.*
import timber.log.Timber


class EventsFragment : BaseFragment() {
	
	private lateinit var eventAdapter: EventAdapter
	
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
	}
	
	private fun handleAddFab() {
		fab.setOnClickListener {
			startActivity(Intent(this.activity, NewEventActivity::class.java))
		}
	}
	
	
	private fun showFilterDialog() {
		val context = App.COMPONENTS.provideContext()
		val viewGroup = activity?.findViewById(android.R.id.content) as ViewGroup
		val dialog = LayoutInflater.from(context)
			.inflate(R.layout.filter_dialog, viewGroup, false)
		
		AlertDialog.Builder(activity!!).apply {
			setView(dialog)
			setOnItemSelectedListener(itemDialogLister)
		}.also {
			it.create()
		}.show()
	}
	
	private val itemDialogLister = object : AdapterView.OnItemSelectedListener {
		override fun onNothingSelected(parent: AdapterView<*>?) {
		
		}
		
		override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
			Toast.makeText(context, "bllaa $position $id", Toast.LENGTH_LONG).show()
		}
		
	}
	
	
	/**
	 * We have blacklist items obtained, initialize recyclerView and display them.
	 */
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		eventAdapter = EventAdapter(context!!, onEventClick, prepareEvents())
		
		with(event_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(events_empty_view)
			layoutManager = LinearLayoutManager(context!!)
			adapter = eventAdapter
		}
	}
	
	private val onEventClick = object : EventListener {
		override fun onItemClick(event: Event) {
			startActivity(Intent(activity, EventDetailActivity::class.java))
		}
	}
	
	private fun prepareEvents(): MutableList<Event> {
		return mutableListOf(
			Event("blala"),
			Event("esgge"),
			Event("semei"),
			Event("semei"),
			Event("semei"),
			Event("semei"),
			Event("semei"),
			Event("semei")
		)
	}
}