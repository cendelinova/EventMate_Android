package gr.tei.erasmus.pp.eventmate.ui.mainActivity

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivityFragmentAdapter.Companion.EVENTS_TAB
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivityFragmentAdapter.Companion.INBOX_TAB
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivityFragmentAdapter.Companion.PROFILE_TAB
import timber.log.Timber


class MainActivity : BaseActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Timber.v("onCreate() called with: savedInstanceState = [$savedInstanceState]")
		
		setContentView(R.layout.activity_main)
		
		setupPages()
	}
	
	private fun setupPages() {
		Timber.v("setupPages() called")
		
		viewpager.adapter = MainActivityFragmentAdapter(this, supportFragmentManager)
		bottom_navigation.setOnNavigationItemSelectedListener {
			when (it.itemId) {
				R.id.inbox_tab -> {
					viewpager.setCurrentItem(INBOX_TAB, true)
					true
				}
				R.id.events_tab -> {
					viewpager.setCurrentItem(EVENTS_TAB, true)
					true
				}
				R.id.profile_tab -> {
					viewpager.setCurrentItem(PROFILE_TAB, true)
					true
				}
				else -> throw IndexOutOfBoundsException()
			}
		}
	}
}
