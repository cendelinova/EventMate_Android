package gr.tei.erasmus.pp.eventmate.ui.inbox

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_ID
import gr.tei.erasmus.pp.eventmate.data.model.Conversation
import gr.tei.erasmus.pp.eventmate.ui.base.BaseFragment
import gr.tei.erasmus.pp.eventmate.ui.inbox.conversationDetail.ConversationDetailActivity
import gr.tei.erasmus.pp.eventmate.ui.inbox.newMessage.NewMessageActivity
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivity
import gr.tei.erasmus.pp.eventmate.ui.userProfile.UserProfileActivity
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
		handleFabBtn()
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
	
	private fun handleFabBtn() {
		fab.setOnClickListener {
			startActivity(Intent(this@InboxFragment.activity, NewMessageActivity::class.java))
		}
	}
	
	private val conversationListener = object : ConversationAdapter.ConversationListener {
		override fun onUserPhotoClick(userId: Long) {
			startActivity(Intent(this@InboxFragment.activity, UserProfileActivity::class.java).apply {
				putExtra(USER_ID, userId)
			})
		}
		
		override fun onConversationClick(conversation: Conversation) {
			startActivity(Intent(this@InboxFragment.activity, ConversationDetailActivity::class.java))
		}
	}
	
	
	private fun prepareConversations(): MutableList<Conversation> {
		return mutableListOf(
			Conversation(1, 1, "Bill Watson", "", "blaba", "5:55", false),
			Conversation(1, 2, "james Watson", "", "sebbs", "5:55", false)
		)
	}
	
	
}