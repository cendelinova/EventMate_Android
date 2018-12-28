package gr.tei.erasmus.pp.eventmate.ui.eventDetail.guests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.data.model.UserRequest
import gr.tei.erasmus.pp.eventmate.ui.base.*
import kotlinx.coroutines.launch

class UserViewModel : BaseViewModel() {
	
	private val userRepository by lazy { App.COMPONENTS.provideUserRepository() }
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	private var allUsers = mutableListOf<Task>()
	
	fun getGuests(eventId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = userRepository.getGuests(eventId).await()
				if (response.isSuccessful && response.body() != null) {
					mStates.postValue(UserListState(response.body()!!))
				}
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
				mStates.postValue(UserListState(mutableListOf(user)))
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun register(user: UserRequest) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val result = userRepository.register(user).await()
				if (result.isSuccessful && result.body() != null) {
					userRepository.saveUserCredentials(user.email, user.password)
				}
				
				mStates.postValue(FinishedState)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun getMyProfile() {
		launch {
			mStates.postValue(LoadingState)
			try {
				val user = userRepository.getMyProfile().await()
				mStates.postValue(UserListState(mutableListOf(user)))
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	data class UserListState(val users: MutableList<User>) : State() {
		companion object {
			fun from(list: MutableList<User>): UserListState {
				return if (list.isEmpty()) error("event list should not be empty")
				else {
					UserListState(list)
				}
			}
		}
	}
	
}
