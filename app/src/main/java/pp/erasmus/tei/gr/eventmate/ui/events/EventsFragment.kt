package pp.erasmus.tei.gr.eventmate.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pp.erasmus.tei.gr.eventmate.R
import pp.erasmus.tei.gr.eventmate.ui.base.BaseFragment
import timber.log.Timber

class EventsFragment: BaseFragment() {
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		Timber.v("onCreateView() called with: inflater = [$inflater], container = [$container], savedInstanceState = [$savedInstanceState]")
		
		return inflater.inflate(R.layout.fragment_events, null)
	}
}