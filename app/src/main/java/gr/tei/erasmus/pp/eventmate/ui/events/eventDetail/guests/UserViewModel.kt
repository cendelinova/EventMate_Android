package gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.BuildConfig
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_ID
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_MAIL
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_PASSWORD
import gr.tei.erasmus.pp.eventmate.data.model.*
import gr.tei.erasmus.pp.eventmate.di.AppModule
import gr.tei.erasmus.pp.eventmate.di.DaggerAppComponent
import gr.tei.erasmus.pp.eventmate.di.NetworkModule
import gr.tei.erasmus.pp.eventmate.ui.base.*
import kotlinx.coroutines.launch

class UserViewModel : BaseViewModel() {
	
	private val userRepository = App.COMPONENTS.provideUserRepository()
	
	private val eventRepository = App.COMPONENTS.provideEventRepository()
	private val sharedPreferenceHelper = App.COMPONENTS.provideSharedPreferencesHelper()
	
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
					mStates.postValue(
						UserListState(
							response.body()!!
						)
					)
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
				val response = userRepository.getUser(userId).await()
				val state = if (response.isSuccessful && response.body() != null) {
					UserListState(mutableListOf(response.body()!!))
				} else {
					ErrorState(Throwable("Error"))
				}
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun register(userRequest: UserRequest) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val result = userRepository.register(userRequest).await()
				if (result.isSuccessful && result.body() != null) {
					val user = result.body()!!
					sharedPreferenceHelper.saveLong(USER_ID, user.id!!)
					sharedPreferenceHelper.saveString(USER_MAIL, user.email)
					sharedPreferenceHelper.saveString(USER_PASSWORD, userRequest.password)
					
					val context = App.COMPONENTS.provideContext()
					App.COMPONENTS = DaggerAppComponent.builder()
						.appModule(AppModule(context))
						.networkModule(
							NetworkModule(
								context,
								User(user.userName, user.email, userRequest.password),
								BuildConfig.SERVER_URL
							)
						)
						.build()
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
				val response = userRepository.getMyProfile().await()
				val state = if (response.isSuccessful && response.body() != null) {
					UserListState(mutableListOf(response.body()!!))
				} else {
					ErrorState(Throwable("Error"))
				}
				
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun login() {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = userRepository.getMyProfile().await()
				val state = if (response.isSuccessful && response.body() != null) {
					FinishedState
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
					AppUserState(response.body()!!)
				} else {
					ErrorState(Throwable(response.message()))
				}
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun inviteGuests(eventId: Long, invitations: MutableList<Invitation>) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = eventRepository.inviteGuests(eventId, invitations).await()
				val state = if (response.isSuccessful && response.body() != null) {
					val guests = (response.body()!! as Event).guests
					guests?.let { UserViewModel.UserListState(it) }
				} else {
					ErrorState(Throwable("Error"))
				}
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	data class AppUserState(val appUsers: MutableList<User>) : State()
	
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
