package gr.tei.erasmus.pp.eventmate.ui.chat.conversationDetail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_ID
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_NAME
import gr.tei.erasmus.pp.eventmate.data.model.ChatMessage
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper
import gr.tei.erasmus.pp.eventmate.helpers.TextHelper
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import gr.tei.erasmus.pp.eventmate.ui.chat.ChatViewModel
import gr.tei.erasmus.pp.eventmate.ui.chat.MessageListAdapter
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserViewModel
import kotlinx.android.synthetic.main.activity_conversation_detail.*
import kotlinx.android.synthetic.main.activity_conversation_detail.view.*
import timber.log.Timber

class ConversationDetailActivity : BaseActivity() {
	
	private lateinit var messageListAdapter: MessageListAdapter
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(ChatViewModel::class.java) }
	
	private var userId: Long? = null
	private var userName: String? = null
	private lateinit var receiverUser: User
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_conversation_detail)
		
		setupToolbar(toolbar)
		
		initializeRecyclerView()
		
		userId = intent.getLongExtra(USER_ID, 0)
		userName = intent.getStringExtra(USER_NAME)
		
		my_title.text = userName
		
		observeViewModel()
		
		viewModel.getConversationDetail(userId!!)
		viewModel.getUser(userId!!)
		
		handleBtnSendMessage()
		
	}
	
	override fun onBackPressed() {
		super.onBackPressed()
		finish()
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeChatMessages)
		}
	}
	
	private fun handleBtnSendMessage() {
		btn_send_message.setOnClickListener {
			val message = input_message.text.toString()
			if (!message.isEmpty()) viewModel.saveMessage(ChatMessage(receiverUser, message))
			input_message.setText("")
			TextHelper.hideKeyboardFrom(this, input_message)
		}
	}
	
	private val observeChatMessages = Observer<State> { state ->
		when (state) {
			is LoadingState -> StateHelper.toggleProgress(progress, true)
			is ErrorState -> StateHelper.showError(state.error, progress, new_message)
			is ChatViewModel.MessageListState -> {
				swipe_layout.isRefreshing = false
				StateHelper.toggleProgress(progress, false)
				messageListAdapter.updateConversationList(state.messages)
				conversation_detail_empty_view.visibility = if (state.messages.isNotEmpty()) View.GONE else View.VISIBLE
			}
			is UserViewModel.UserListState -> {
				StateHelper.toggleProgress(progress, false)
				receiverUser = state.users[0]
			}
		}
	}
	
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		messageListAdapter = MessageListAdapter(this, mutableListOf())
		
		with(message_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(conversation_detail_empty_view)
			layoutManager = LinearLayoutManager(context)
			adapter = messageListAdapter
		}
		
		swipe_layout.setOnRefreshListener {
			userId?.let { viewModel.getConversationDetail(it, false) }
		}
	}
}
