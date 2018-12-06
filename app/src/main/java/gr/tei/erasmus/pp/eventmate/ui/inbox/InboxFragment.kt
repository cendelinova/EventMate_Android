package gr.tei.erasmus.pp.eventmate.ui.inbox

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.Conversation
import gr.tei.erasmus.pp.eventmate.ui.base.BaseFragment
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivity
import kotlinx.android.synthetic.main.fragment_inbox.*
import timber.log.Timber

class InboxFragment : BaseFragment() {
	
	private lateinit var conversationAdapter: ConversationAdapter
	
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		Timber.v("onCreateView() called with: inflater = [$inflater], container = [$container], savedInstanceState = [$savedInstanceState]")
		(activity as MainActivity).setCustomTitle(getString(R.string.title_inbox))
		
		return inflater.inflate(R.layout.fragment_inbox, null)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeRecyclerView()
	}
	
	/**
	 * We have blacklist items obtained, initialize recyclerView and display them.
	 */
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		conversationAdapter = ConversationAdapter(context!!, conversationListener, prepareConversations())
		
		with(conversation_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(conversation_empty_view)
			layoutManager = LinearLayoutManager(context!!)
			adapter = conversationAdapter
		}
	}
	
	private val conversationListener = object : ConversationAdapter.ConversationListener {
		override fun onConversationClick(conversation: Conversation) {
			startActivity(Intent(this@InboxFragment.activity, ConversationDetailActivity::class.java))
		}
	}
	
	private fun prepareConversations(): MutableList<Conversation> {
		return mutableListOf(
			Conversation(1, "Bill Watson", "", "ahojky", "5:55", false),
			Conversation(1, "james Watson", "", "sebbs", "5:55", false)
		)
	}
	
	
}