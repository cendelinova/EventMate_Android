package gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.BuildConfig
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.*
import gr.tei.erasmus.pp.eventmate.di.AppModule
import gr.tei.erasmus.pp.eventmate.di.DaggerAppComponent
import gr.tei.erasmus.pp.eventmate.di.NetworkModule
import gr.tei.erasmus.pp.eventmate.helpers.ErrorHelper
import gr.tei.erasmus.pp.eventmate.ui.base.*
import kotlinx.coroutines.launch
import timber.log.Timber

class UserViewModel : BaseViewModel() {
	
	private val userRepository = App.COMPONENTS.provideUserRepository()
	
	private val eventRepository = App.COMPONENTS.provideEventRepository()
	private val sharedPreferenceHelper = App.COMPONENTS.provideSharedPreferencesHelper()
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	private var allUsers = mutableListOf<Task>()
	
	fun getGuests(eventId: Long, showProgress: Boolean = true) {
		launch {
			if (showProgress) mStates.postValue(LoadingState)
			try {
				val response = userRepository.getGuests(eventId).await()
				val state = if (response.isSuccessful && response.body() != null) {
					UserListState(response.body()!!)
				} else {
					Timber.e(response.errorBody()?.string())
					ErrorState(Throwable(ErrorHelper.getErrorMessageFromHeader(response.headers())))
				}
				Timber.v("AAAA rendering ${response.body()!!.size}")
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
				val response = userRepository.getUser(userId).await()
				val state = if (response.isSuccessful && response.body() != null) {
					UserListState(mutableListOf(response.body()!!))
				} else {
					ErrorState(Throwable(ErrorHelper.getErrorMessageFromHeader(response.headers())))
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
				val response = userRepository.register(userRequest).await()
				val state = if (response.isSuccessful && response.body() != null) {
					userRepository.saveUserToSharedPreferences(response.body()!!, userRequest.password)
					
					val context = App.COMPONENTS.provideContext()
					App.COMPONENTS = DaggerAppComponent.builder()
						.appModule(AppModule(context))
						.networkModule(
							NetworkModule(
								context,
								User(userRequest.email, userRequest.password),
								BuildConfig.SERVER_URL
							)
						)
						.build()
					
					FinishedState
				} else {
					Timber.e(response.errorBody()?.string())
					ErrorState(Throwable(ErrorHelper.getErrorMessageFromHeader(response.headers())))
				}
				
				mStates.postValue(state)
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
					Timber.e(response.errorBody()?.string())
					ErrorState(Throwable(ErrorHelper.getErrorMessageFromHeader(response.headers())))
				}
				
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun login(userRequest: UserRequest) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = userRepository.loginUser(userRequest).await()
				val state = if (response.isSuccessful && response.body() != null) {
					App.COMPONENTS = DaggerAppComponent.builder()
						.appModule(AppModule(App.COMPONENTS.provideContext()))
						.networkModule(
							NetworkModule(
								App.COMPONENTS.provideContext(),
								User(userRequest.email, userRequest.password),
								BuildConfig.SERVER_URL
							)
						)
						.build()
					
					userRepository.saveUserToSharedPreferences(response.body()!!, userRequest.password)
					FinishedState
				} else {
					Timber.e(response.errorBody()?.string())
					ErrorState(Throwable(ErrorHelper.getErrorMessageFromHeader(response.headers())))
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
					Timber.e(response.errorBody()?.string())
					ErrorState(Throwable(ErrorHelper.getErrorMessageFromHeader(response.headers())))
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
					val guests = (response.body()!! as EventDetail).guests
					guests?.let { UserViewModel.UserListState(it) }
				} else {
					Timber.e(response.errorBody()?.string())
					ErrorState(Throwable(ErrorHelper.getErrorMessageFromHeader(response.headers())))
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
