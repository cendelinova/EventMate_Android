package pp.erasmus.tei.gr.eventmate.ui.mainActivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import pp.erasmus.tei.gr.eventmate.R
import pp.erasmus.tei.gr.eventmate.ui.mainActivity.MainActivityFragmentAdapter.Companion.EVENTS_TAB
import pp.erasmus.tei.gr.eventmate.ui.mainActivity.MainActivityFragmentAdapter.Companion.INBOX_TAB
import pp.erasmus.tei.gr.eventmate.ui.mainActivity.MainActivityFragmentAdapter.Companion.PROFILE_TAB
import timber.log.Timber


class MainActivity : AppCompatActivity() {
	
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
