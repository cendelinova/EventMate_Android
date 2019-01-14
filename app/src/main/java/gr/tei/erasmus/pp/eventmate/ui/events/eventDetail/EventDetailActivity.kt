package gr.tei.erasmus.pp.eventmate.ui.events.eventDetail

import android.content.DialogInterface
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.EVENT_ID
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.helpers.DateTimeHelper
import gr.tei.erasmus.pp.eventmate.helpers.DialogHelper
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper
import gr.tei.erasmus.pp.eventmate.ui.base.*
import gr.tei.erasmus.pp.eventmate.ui.events.EventsViewModel
import gr.tei.erasmus.pp.eventmate.ui.events.newEvent.NewEventActivity
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivity
import gr.tei.erasmus.pp.eventmate.ui.report.ReportListActivity
import kotlinx.android.synthetic.main.activity_event_detail.*


class EventDetailActivity : BaseActivity() {
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(EventsViewModel::class.java) }
	
	var eventId: Long? = null
	
	private var event: Event? = null
	
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
	}
	
	
	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		val inflater = menuInflater
		inflater.inflate(R.menu.menu_fragment_detail, menu)
		if (event?.state != Event.EventState.EDITABLE.name) {
			menu?.findItem(R.id.edit)?.isVisible = false
		}
		return super.onCreateOptionsMenu(menu)
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == R.id.edit) startActivity(
			Intent(
				this@EventDetailActivity,
				NewEventActivity::class.java
			).apply {
				putExtra(
					EVENT_ID, eventId
				)
			})
		else if (item.itemId == R.id.delete) {
			DialogHelper.showDeleteDialog(this, DialogInterface.OnClickListener { _, _ ->
				eventId?.let {
					viewModel.deleteEvent(it)
				}
			})
		}
		return super.onOptionsItemSelected(item)
	}
	
	private fun setupViewPager() {
		val fragmentAdapter = EventDetailFragmentAdapter(
			this,
			supportFragmentManager,
			eventId
		)
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
	
	private fun setupLayout(event: Event?) {
		event?.run {
			event_name_title.text = name
			event_status.text = "[" + getString(Event.EventState.valueOf(state).statusMessage) + "]"
			event_name.text = name
			event_date.text = DateTimeHelper.formatDateTimeString(date, DateTimeHelper.DATE_FORMAT)
			event_time.text = DateTimeHelper.formatDateTimeString(date, DateTimeHelper.TIME_FORMAT)
			photo?.let {
				event_photo.setImageBitmap(FileHelper.decodeImage(it))
			}
			
			StateHelper.prepareEventFab(this, state_fab, View.OnClickListener {
				if (state != Event.EventState.FINISHED.name) {
					viewModel.changeEventStatus(id)
				} else {
					startActivity(Intent(this@EventDetailActivity, ReportListActivity::class.java).apply {
						putExtra(EVENT_ID, id)
					})
				}
				
			})
		}
		
	}
	
	fun getEvent(): Event? = event
	
	// Observer
	private val observeEventProgressState = Observer<State> { state ->
		when (state) {
			is LoadingState -> StateHelper.toggleProgress(progress, true)
			is ErrorState -> StateHelper.showError(state.error, progress, event_detail)
			is DeletedState -> {
				StateHelper.toggleProgress(progress, true)
				startActivity(Intent(this, MainActivity::class.java))
			}
			is EventsViewModel.EventListState -> {
				StateHelper.toggleProgress(progress, false)
				event = state.events[0]
				setupLayout(event)
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
		}
	}
	
}
