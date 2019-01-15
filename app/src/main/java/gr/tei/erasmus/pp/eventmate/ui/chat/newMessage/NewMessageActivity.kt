package gr.tei.erasmus.pp.eventmate.ui.chat.newMessage

import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.miguelcatalan.materialsearchview.MaterialSearchView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.toggleProgress
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import gr.tei.erasmus.pp.eventmate.ui.chat.ChatViewModel
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserAdapter
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserViewModel
import kotlinx.android.synthetic.main.activity_new_message.*


class NewMessageActivity : BaseActivity() {
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(ChatViewModel::class.java) }
	
	private var users: MutableList<User> = mutableListOf()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_new_message)
		
		setupToolbar(toolbar, true)
		observeViewModel()
		handleBtnSendMessage()
		handleSearchUsers()
		viewModel.getAppUsers()
		
	}
	
	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.new_message_search, menu)
		
		val item = menu.findItem(R.id.action_search)
		search_view.setMenuItem(item)
		
		return true
	}
	
	private fun handleSearchUsers() {
		search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String): Boolean {
				//Do some magic
				return false
			}
			
			override fun onQueryTextChange(newText: String): Boolean {
				//Do some magic
				return false
			}
		})
		
		search_view.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
			override fun onSearchViewShown() {
				//Do some magic
			}
			
			override fun onSearchViewClosed() {
				//Do some magic
			}
		})
		
		search_view.setSuggestions(getResources().getStringArray(R.array.query_suggestions))
	}
	
	private val observeChatMessages = Observer<State> { state ->
		when (state) {
			is LoadingState -> StateHelper.toggleProgress(progress, true)
			is ErrorState -> StateHelper.showError(state.error, progress, new_message)
			is ChatViewModel.MessageListState -> {
				StateHelper.toggleProgress(progress, false)
//				conversationAdapter.updateConversationList(state.messages)
			}
			is UserViewModel.UserListState -> {
				toggleProgress(progress, false)
				users = state.users
			}
		}
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeChatMessages)
		}
	}
	
	private fun handleBtnSendMessage() {
		btn_send_message.setOnClickListener {
			val message = input_message.text.toString()
//			if (!message.isEmpty()) viewModel.saveMessage(ChatMessage(User(), message))
		}
	}
}
