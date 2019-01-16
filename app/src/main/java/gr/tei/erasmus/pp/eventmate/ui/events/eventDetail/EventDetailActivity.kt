package gr.tei.erasmus.pp.eventmate.ui.events.eventDetail

import android.content.DialogInterface
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.EVENT_EDITABLE
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.EVENT_ID
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.EVENT_SHOW_MENU
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
import timber.log.Timber


class EventDetailActivity : BaseActivity() {
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(EventsViewModel::class.java) }
	
	private lateinit var fragmentAdapter: EventDetailFragmentAdapter
	
	var eventId: Long? = null
	
	private var event: Event? = null
	
	private var isEditable = false
	private var showMenu = false
	
	private lateinit var menuOptions: Menu
	
	companion object {
		private const val TITLE_FADE_OUT_RANGE = 0.15F
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Timber.d("onCreate")
		setContentView(R.layout.activity_event_detail)
		
		eventId = intent.getLongExtra(EVENT_ID, 0)
		isEditable = intent.getBooleanExtra(EVENT_EDITABLE, false)
		showMenu = intent.getBooleanExtra(EVENT_SHOW_MENU, false)
		
		observeViewModel()
		
		eventId?.let {
			viewModel.getEvent(it, false)
		}
		
		setupToolbar(toolbar)
		setupViewPager()
		handleToolbar()
	}
	
	override fun onBackPressed() {
		finish()
		startActivity(Intent(this@EventDetailActivity, MainActivity::class.java))
	}
	
	
	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		Timber.d("onCreateOptionsMenu")
		if (showMenu) {
			val inflater = menuInflater
			inflater.inflate(R.menu.menu_fragment_detail, menu)
			menuOptions = menu!!
			if (!isEditable) {
				menu.findItem(R.id.edit)?.isVisible = false
			}
		}
		return super.onCreateOptionsMenu(menu)
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == R.id.edit) startActivity(
			Intent(
				this@EventDetailActivity,
				NewEventActivity::class.java
			).apply {
				putExtra(EVENT_ID, eventId)
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
		fragmentAdapter = EventDetailFragmentAdapter(this, supportFragmentManager, eventId)
		
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
			val state = Event.EventState.valueOf(state)
			event_name_title.text = name
			event_status.text = "[" + getString(state.statusMessage) + "]"
			event_name.text = name
			event_date.text = DateTimeHelper.formatDateTimeString(date, DateTimeHelper.DATE_FORMAT)
			event_time.text = DateTimeHelper.formatDateTimeString(date, DateTimeHelper.TIME_FORMAT)
			photo?.let {
				event_photo.setImageBitmap(FileHelper.decodeImage(it))
			}
			
			StateHelper.prepareEventFab(this, state_fab, View.OnClickListener {
				if (state != Event.EventState.FINISHED) {
					viewModel.changeEventStatus(id, this@EventDetailActivity)
				} else {
					finish()
					startActivity(Intent(this@EventDetailActivity, ReportListActivity::class.java).apply {
						putExtra(EVENT_ID, id)
					})
				}
				
				Toast.makeText(
					this@EventDetailActivity,
					getString(Event.EventState.valueOf(this.state).messageResource),
					Toast.LENGTH_LONG
				).show()
				
			})
			
			if (state != Event.EventState.EDITABLE) {
				isEditable = false
			}
		}
		
	}
	
	fun isEditableEvent() = isEditable
	
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
			is EventsViewModel.ReadyToPlayEvent -> {
				StateHelper.toggleProgress(progress, false)
				finish()
				startActivity(Intent(this, EventDetailActivity::class.java).apply {
					putExtra(EVENT_ID, state.event.id)
					putExtra(EVENT_EDITABLE, false)
				})
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
