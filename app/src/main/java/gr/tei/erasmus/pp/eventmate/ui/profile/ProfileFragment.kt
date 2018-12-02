package gr.tei.erasmus.pp.eventmate.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.ui.base.BaseFragment
import timber.log.Timber
import android.view.MenuInflater
import gr.tei.erasmus.pp.eventmate.ui.gameRank.GameRankActivity
import gr.tei.erasmus.pp.eventmate.ui.help.HelpActivity
import gr.tei.erasmus.pp.eventmate.ui.settings.SettingsActivity


class ProfileFragment : BaseFragment() {
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		Timber.v("onCreateView() called with: inflater = [$inflater], container = [$container], savedInstanceState = [$savedInstanceState]")
		
		return inflater.inflate(R.layout.fragment_profile, null)
	}
	
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		setHasOptionsMenu(true)
	}
	
	override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
		super.onCreateOptionsMenu(menu, inflater)
		activity?.menuInflater?.run {
			inflate(R.menu.profile_menu, menu)
		}
		
	}
	
	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		when(item?.itemId) {
			R.id.settings -> openActivity(SettingsActivity::class.java)
			R.id.game_rank -> openActivity(GameRankActivity::class.java)
			R.id.help -> openActivity(HelpActivity::class.java)
			R.id.logout -> logOut()
		}
		
		return true
	}
	
	private fun <T: Any> openActivity(uri: Class<T>) = startActivity(Intent(this.activity, uri))
	
	private fun logOut() {
		// todo logout
	}
}