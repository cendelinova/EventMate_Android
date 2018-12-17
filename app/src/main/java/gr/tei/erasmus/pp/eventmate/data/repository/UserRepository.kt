package gr.tei.erasmus.pp.eventmate.data.repository

import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.RestHelper
import kotlinx.coroutines.Deferred
import retrofit2.Response

class UserRepository(private val restHelper: RestHelper) {
	
	fun getUser(userId: Long) = restHelper.getUser(userId)
	
	suspend fun register(user: User) : Deferred<Response<Unit>> {
		// todo save userName and password save in phone
		return restHelper.registerUser(user)
	}
}