package gr.tei.erasmus.pp.eventmate.ui.mainActivity

import android.os.Bundle
import android.support.v4.app.Fragment
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import gr.tei.erasmus.pp.eventmate.ui.events.EventsFragment
import gr.tei.erasmus.pp.eventmate.ui.inbox.InboxFragment
import gr.tei.erasmus.pp.eventmate.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*
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

//		viewpager.adapter = MainActivityFragmentAdapter(this, supportFragmentManager)
		bottom_navigation.setOnNavigationItemSelectedListener {
			when (it.itemId) {
				R.id.inbox_tab -> {
					loadFragment(InboxFragment())
					true
				}
				R.id.events_tab -> {
					loadFragment(EventsFragment())

//					viewpager.setCurrentItem(EVENTS_TAB, true)
					true
				}
				R.id.profile_tab -> {
					loadFragment(ProfileFragment())

//					viewpager.setCurrentItem(PROFILE_TAB, true)
					true
				}
				else -> throw IndexOutOfBoundsException()
			}
		}
	}
	
	/**
	 * loading fragment into FrameLayout
	 *
	 * @param fragment
	 */
	private fun loadFragment(fragment: Fragment) {
		// load fragment
		val transaction = supportFragmentManager.beginTransaction()
		transaction.replace(R.id.frame_container, fragment)
		transaction.addToBackStack(null)
		transaction.commit()
	}
}
