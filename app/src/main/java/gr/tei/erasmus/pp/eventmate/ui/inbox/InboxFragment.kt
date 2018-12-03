package gr.tei.erasmus.pp.eventmate.ui.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.ui.base.BaseFragment
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivity
import timber.log.Timber

class InboxFragment: BaseFragment() {
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		Timber.v("onCreateView() called with: inflater = [$inflater], container = [$container], savedInstanceState = [$savedInstanceState]")
//		(activity as MainActivity).setCustomTitle(getString(R.string.title_))
		
		return inflater.inflate(R.layout.fragment_inbox, null)
	}
}