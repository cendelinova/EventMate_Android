package gr.tei.erasmus.pp.eventmate.data.repository

import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_ID
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.data.model.UserRequest
import gr.tei.erasmus.pp.eventmate.helpers.RestHelper
import timber.log.Timber

class UserRepository(private val restHelper: RestHelper) {
	
	fun getUser(userId: Long) = restHelper.getUser(userId)
	
//	suspend fun register(user: UserRequest): User? {
//		// todo save userName and password save in phone
//		val response = restHelper.registerUser(user).await()
//		if (response.isSuccessful && response.body() != null) {
//			response.body()?.uid?.let {
//				Timber.v("Saved userId $it")
//				saveUserId(it)
//			}
//		}
//		return response.body()
//	}
	
	fun register(user: UserRequest) = restHelper.registerUser(user)
	
	fun saveUserId(userId: Long) {
		val sharedPreferenceHelper = App.COMPONENTS.provideSharedPreferencesHelper()
		sharedPreferenceHelper.saveLong(USER_ID, userId)
	}
	
	fun getMyProfile() = restHelper.getMyProfile()
}