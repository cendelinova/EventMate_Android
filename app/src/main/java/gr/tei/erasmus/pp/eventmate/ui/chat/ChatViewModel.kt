package gr.tei.erasmus.pp.eventmate.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.ChatMessage
import gr.tei.erasmus.pp.eventmate.ui.base.BaseViewModel
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserViewModel
import kotlinx.coroutines.launch

class ChatViewModel : BaseViewModel() {
	private val chatRepository = App.COMPONENTS.provideChatRepository()
	private val userRepository = App.COMPONENTS.provideUserRepository()
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	fun saveMessage(chatMessage: ChatMessage) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = chatRepository.saveMessage(chatMessage).await()
				val state = if (response.isSuccessful && response.body() != null) {
					MessageListState(mutableListOf(response.body()!!))
				} else {
					ErrorState(Throwable("Error"))
				}
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun getMyConversations() {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = chatRepository.getMyConversations().await()
				val state = if (response.isSuccessful && response.body() != null) {
					MessageListState(response.body()!!)
				} else {
					ErrorState(Throwable("Error"))
				}
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun getConversationDetail(userId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = chatRepository.getConversationDetail(userId).await()
				val state = if (response.isSuccessful && response.body() != null) {
					MessageListState(response.body()!!)
				} else {
					ErrorState(Throwable("Error"))
				}
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun getAppUsers() {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = userRepository.getAppUsers().await()
				val state = if (response.isSuccessful && response.body() != null) {
					UserViewModel.AppUserState(response.body()!!)
				} else {
					ErrorState(Throwable(response.message()))
				}
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun getUser(userId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val user = userRepository.getUser(userId).await()
				mStates.postValue(
					UserViewModel.UserListState(
						mutableListOf(user)
					)
				)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	data class MessageListState(val messages: MutableList<ChatMessage>) : State()
	
}