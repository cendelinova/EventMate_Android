package gr.tei.erasmus.pp.eventmate.ui.eventDetail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.EVENT_ID
import gr.tei.erasmus.pp.eventmate.ui.eventDetail.guests.GuestsFragment
import gr.tei.erasmus.pp.eventmate.ui.eventDetail.tasks.TasksFragment

class EventDetailFragmentAdapter(
	private val context: Context,
	fragmentManager: FragmentManager,
	var eventId: Long?
) : FragmentPagerAdapter(fragmentManager) {
	
	/* Constants **********************************************************************************/
	
	companion object {
		const val TASKS_TAB = 0
		const val GUESTS_TAB = 1
		private const val TAB_COUNT = 2
	}
	
	/* Public methods *****************************************************************************/
	
	/**
	 * Get correct fragment for given tab.
	 *
	 * @param position The position of actually active tab.
	 * @return Valid fragment to display for given position.
	 */
	override fun getItem(position: Int) = when (position) {
		TASKS_TAB -> TasksFragment().apply {
			arguments = Bundle().apply {
				putLong(EVENT_ID, eventId!!)
			}
		}
		GUESTS_TAB -> GuestsFragment()
		else -> throw IndexOutOfBoundsException()
	}
	
	/**
	 * Get the total number of tabs.
	 */
	override fun getCount() = TAB_COUNT
	
	/**
	 * Get valid title fot given position.
	 *
	 * @param position The position of actually active tab.
	 * @return tab title String
	 */
	override fun getPageTitle(position: Int) = context.getString(
		when (position) {
			TASKS_TAB -> R.string.tasks_tab_title
			GUESTS_TAB -> R.string.guests_tab_title
			else -> throw IndexOutOfBoundsException()
		}
	)
	
}