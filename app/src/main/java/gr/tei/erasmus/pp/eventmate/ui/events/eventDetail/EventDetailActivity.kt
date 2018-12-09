package gr.tei.erasmus.pp.eventmate.ui.events.eventDetail

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
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
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_event_detail)
		
		setupToolbar(toolbar)
		setupViewPager()
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
}
