package gr.tei.erasmus.pp.eventmate.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_ID
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_NAME
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.showError
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.toggleProgress
import gr.tei.erasmus.pp.eventmate.ui.base.BaseFragment
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import gr.tei.erasmus.pp.eventmate.ui.chat.conversationDetail.ConversationDetailActivity
import gr.tei.erasmus.pp.eventmate.ui.chat.newMessage.NewMessageActivity
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivity
import gr.tei.erasmus.pp.eventmate.ui.userProfile.UserProfileActivity
import kotlinx.android.synthetic.main.fragment_inbox.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class ChatFragment : BaseFragment() {
	
	private lateinit var conversationAdapter: ConversationAdapter
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(ChatViewModel::class.java) }
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		Timber.v("onCreateView() called with: inflater = [$inflater], container = [$container], savedInstanceState = [$savedInstanceState]")
		(activity as MainActivity).setCustomTitle(getString(R.string.title_inbox))
		
		return inflater.inflate(R.layout.fragment_inbox, null)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeRecyclerView()
		observeViewModel()
		handleFabBtn()
		
		viewModel.getMyConversations()
	}
	
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeChatMessages)
		}
	}
	
	private val observeChatMessages = Observer<State> { state ->
		when (state) {
			is ErrorState -> showError(state.error, progress, chat_fragment)
			is ChatViewModel.MessageListState -> {
				swipe_layout.isRefreshing = false
				conversationAdapter.updateConversationList(state.messages)
			}
		}
	}
	
	/**
	 * We have blacklist items obtained, initialize recyclerView and display them.
	 */
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		conversationAdapter = ConversationAdapter(context!!, conversationListener, mutableListOf())
		
		with(conversation_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(conversation_empty_view)
			layoutManager = LinearLayoutManager(context!!)
			adapter = conversationAdapter
		}
		
		swipe_layout.setOnRefreshListener {
			viewModel.getMyConversations()
		}
	}
	
	private fun handleFabBtn() {
		state_fab.setOnClickListener {
			startActivity(Intent(this@ChatFragment.activity, NewMessageActivity::class.java))
		}
	}
	
	private val conversationListener = object : ConversationAdapter.ConversationListener {
		override fun onUserPhotoClick(userId: Long) {
			startActivity(Intent(this@ChatFragment.activity, UserProfileActivity::class.java).apply {
				putExtra(USER_ID, userId)
			})
		}
		
		override fun onConversationClick(user: User) {
			startActivity(Intent(this@ChatFragment.activity, ConversationDetailActivity::class.java).apply {
				putExtra(USER_ID, user.id)
				putExtra(USER_NAME, user.userName)
			})
		}
	}
	
}