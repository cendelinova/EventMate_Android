package gr.tei.erasmus.pp.eventmate.ui.eventDetail.guests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.data.model.UserRequest
import gr.tei.erasmus.pp.eventmate.ui.base.*
import kotlinx.coroutines.launch
import timber.log.Timber

class UserViewModel : BaseViewModel() {
	
	private val userRepository by lazy { App.COMPONENTS.provideUserRepository() }
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	private var allUsers = mutableListOf<Task>()
	
	fun getGuests() {
	
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
				val response = userRepository.register(user).await()
				
				if (response.isSuccessful && response.body() != null) {
					response.body()?.id?.let {
						Timber.v("Saved userId $it")
						userRepository.saveUserId(it)
					}
				}
				mStates.postValue(FinishedState)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	private fun storeUserCredentials(user: User) {
		// todo bude email
//		val account = Account(user.userName, your_account_type)
//		val accountManager = AccountManager.get(App.COMPONENTS.provideContext())
//		accountManager.addAccountExplicitly(account, mPassword, null)
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
