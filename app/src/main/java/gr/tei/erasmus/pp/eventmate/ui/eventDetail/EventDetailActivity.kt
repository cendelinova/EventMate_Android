package gr.tei.erasmus.pp.eventmate.ui.eventDetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.EVENT_ID
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.helpers.DateTimeHelper
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import gr.tei.erasmus.pp.eventmate.ui.events.EventsViewModel
import gr.tei.erasmus.pp.eventmate.ui.events.newEvent.NewEventActivity
import gr.tei.erasmus.pp.eventmate.ui.newTask.NewTaskActivity
import kotlinx.android.synthetic.main.activity_event_detail.*


class EventDetailActivity : BaseActivity() {
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(EventsViewModel::class.java) }
	
	private var eventId: Long? = null
	private lateinit var eventName : String
	
	companion object {
		private const val TITLE_FADE_OUT_RANGE = 0.15F
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_event_detail)
		
		eventId = intent.getLongExtra(EVENT_ID, 0)
		
		observeViewModel()
		
		eventId?.let {
			viewModel.getEvent(it)
		}
		
		setupToolbar(toolbar)
		setupViewPager()
		handleToolbar()
		
		fab.setOnClickListener {
			startActivity(Intent(this, NewTaskActivity::class.java))
		}
	}
	
	
	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		val inflater = menuInflater
		inflater.inflate(R.menu.menu_fragment_event_detail, menu)
		return super.onCreateOptionsMenu(menu)
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == R.id.edit) startActivity(Intent(this@EventDetailActivity, NewEventActivity::class.java))
		return super.onOptionsItemSelected(item)
	}
	
	private fun setupViewPager() {
		val fragmentAdapter = EventDetailFragmentAdapter(this, supportFragmentManager)
		view_pager.adapter = fragmentAdapter
		tabs.setupWithViewPager(view_pager)
		tabs.addOnTabSelectedListener(selectTabListener)
		setupTabIcons()
	}
	
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeEventProgressState)
		}
	}
	
	private fun setupTabIcons() {
		tabs.getTabAt(EventDetailFragmentAdapter.TASKS_TAB)?.setIcon(R.drawable.ic_tasks_20dp_selected)
		tabs.getTabAt(EventDetailFragmentAdapter.GUESTS_TAB)?.setIcon(R.drawable.ic_guests_grey)
	}
	
	private val selectTabListener = object : TabLayout.OnTabSelectedListener {
		override fun onTabReselected(tab: TabLayout.Tab?) {
		
		}
		
		override fun onTabUnselected(tab: TabLayout.Tab?) {
			tab?.icon?.setColorFilter(
				ContextCompat.getColor(this@EventDetailActivity, R.color.grey),
				PorterDuff.Mode.SRC_IN
			)
		}
		
		override fun onTabSelected(tab: TabLayout.Tab?) {
			tab?.icon?.setColorFilter(
				ContextCompat.getColor(this@EventDetailActivity, R.color.colorPrimary),
				PorterDuff.Mode.SRC_IN
			)
		}
	}
	
	
	private fun handleToolbar() {
		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		
		appbar.post { appbar.addOnOffsetChangedListener(onOffsetChangedListener) }
	}
	
	private fun setupLayout(event: Event) {
		eventName = event.name
		event_name.text = event.name
		event_date.text = DateTimeHelper.formatDateTimeString(event.date, DateTimeHelper.DATE_FORMAT)
		event_time.text = DateTimeHelper.formatDateTimeString(event.date, DateTimeHelper.TIME_FORMAT)
	}
	
	// Observer
	private val observeEventProgressState = Observer<State> { state ->
		when (state) {
			is LoadingState -> StateHelper.toggleProgress(progress, true)
			is ErrorState -> StateHelper.showError(state.error, progress, event_detail)
			is EventsViewModel.EventListState -> {
				StateHelper.toggleProgress(progress, false)
				setupLayout(state.events[0])
			}
		}
	}
	
	/**
	 * Toolbar title text opacity changes according to actual scroll position of collapsible toolbar.
	 * In range between totalScrollRange to TITLE_FADE_OUT_RANGE * totalScrollRange text is not visible.
	 * In range between TITLE_FADE_OUT_RANGE * totalScrollRange to 0 text gets linearly more opacity.
	 */
	private val onOffsetChangedListener = AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
		val scroll = Math.abs(Math.abs(verticalOffset) - appBarLayout.totalScrollRange)
		val titleFadeOutRange = (appbar.totalScrollRange * TITLE_FADE_OUT_RANGE).toInt()
		if (scroll < titleFadeOutRange) {
			event_name_title.alpha = Math.abs(scroll / titleFadeOutRange.toFloat() - 1)
		} else {
			event_name_title.alpha = 0.0F
			event_name_title.text = eventName
		}
	}
}
