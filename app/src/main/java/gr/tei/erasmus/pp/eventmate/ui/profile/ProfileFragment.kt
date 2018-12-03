package gr.tei.erasmus.pp.eventmate.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.ui.base.BaseFragment
import gr.tei.erasmus.pp.eventmate.ui.gameRank.GameRankActivity
import gr.tei.erasmus.pp.eventmate.ui.help.HelpActivity
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivity
import gr.tei.erasmus.pp.eventmate.ui.settings.SettingsActivity
import timber.log.Timber


class ProfileFragment : BaseFragment() {
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		Timber.v("onCreateView() called with: inflater = [$inflater], container = [$container], savedInstanceState = [$savedInstanceState]")
		setHasOptionsMenu(true)
		(activity as MainActivity).setCustomTitle("")
		
		return inflater.inflate(R.layout.fragment_profile, null)
	}
	
	
	override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
		activity?.menuInflater?.run {
			inflate(R.menu.menu_fragment_profile, menu)
		}
		super.onCreateOptionsMenu(menu, inflater)
	}
	
	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		when (item?.itemId) {
			R.id.settings -> openActivity(SettingsActivity::class.java)
			R.id.game_rank -> openActivity(GameRankActivity::class.java)
			R.id.help -> openActivity(HelpActivity::class.java)
			R.id.logout -> logOut()
		}
		
		return true
	}
	
	private fun <T : Any> openActivity(uri: Class<T>) = startActivity(Intent(this.activity, uri))
	
	private fun logOut() {
		// todo logout
	}
}