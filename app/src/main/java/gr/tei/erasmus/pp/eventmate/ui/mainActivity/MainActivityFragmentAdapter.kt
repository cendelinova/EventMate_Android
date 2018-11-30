package gr.tei.erasmus.pp.eventmate.ui.mainActivity

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.ui.events.EventsFragment
import gr.tei.erasmus.pp.eventmate.ui.inbox.InboxFragment
import gr.tei.erasmus.pp.eventmate.ui.profile.ProfileFragment

class MainActivityFragmentAdapter(
	private val context: Context,
	fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {
	
	
	/* Constants **********************************************************************************/
	
	companion object {
		const val INBOX_TAB = 0
		const val EVENTS_TAB = 1
		const val PROFILE_TAB = 2
		private const val TAB_COUNT = 3
	}
	
	
	override fun getItem(position: Int) = when (position) {
		INBOX_TAB -> InboxFragment()
		EVENTS_TAB -> EventsFragment()
		PROFILE_TAB -> ProfileFragment()
		else -> throw IndexOutOfBoundsException()
	}
	
	override fun getCount() = TAB_COUNT
	
	/**
	 * Get valid title fot given position.
	 *
	 * @param position The position of actually active tab.
	 * @return tab title String
	 */
	override fun getPageTitle(position: Int): String = context.getString(
		when (position) {
			INBOX_TAB -> R.string.tab_inbox
			EVENTS_TAB -> R.string.tab_events
			PROFILE_TAB -> R.string.tab_profile
			else -> throw IndexOutOfBoundsException()
		}
	)
	
	/**
	 * Get icon for given position.
	 *
	 * @param position The position of actually active tab.
	 * @return tab icon Drawable
	 */
	fun getPageIcon(position: Int) = ContextCompat.getDrawable(
		context, when (position) {
			INBOX_TAB -> R.drawable.ic_message
			EVENTS_TAB -> R.drawable.ic_events
			PROFILE_TAB -> R.drawable.ic_user
			else -> throw IndexOutOfBoundsException()
		}
	)
	
}