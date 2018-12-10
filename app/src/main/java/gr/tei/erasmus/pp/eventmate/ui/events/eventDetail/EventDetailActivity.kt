package gr.tei.erasmus.pp.eventmate.ui.events.eventDetail

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import gr.tei.erasmus.pp.eventmate.ui.events.newEvent.NewEventActivity
import gr.tei.erasmus.pp.eventmate.ui.tasks.NewTaskActivity
import kotlinx.android.synthetic.main.activity_event_detail.*


class EventDetailActivity : BaseActivity() {
	
	companion object {
		private const val TITLE_FADE_OUT_RANGE = 0.15F
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_event_detail)
		
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
