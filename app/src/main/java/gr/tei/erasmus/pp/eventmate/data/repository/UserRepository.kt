package gr.tei.erasmus.pp.eventmate.data.repository

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_ID
import gr.tei.erasmus.pp.eventmate.data.model.UserRequest
import gr.tei.erasmus.pp.eventmate.helpers.RestHelper
import okhttp3.Credentials
import timber.log.Timber


class UserRepository(private val context: Context, private val restHelper: RestHelper) {
	
	companion object {
		private const val TYPE_ACCOUNT = "gr.tei.erasmus.pp.eventmate"
	}
	
	fun getUserCredentials(): String? {
		val accountManager = AccountManager.get(context)
		try {
			val account = accountManager.getAccountsByType(TYPE_ACCOUNT)[0]
			
			return Credentials.basic(account.name, accountManager.getPassword(account))
		} catch (ignored: Exception) {
			Timber.e("Error occured during retrieving user account $ignored")
		}
		
		return null
	}
	
	fun getAppUsers() = restHelper.getAppUsers()
	
	fun getUser(userId: Long) = restHelper.getUser(userId)
	
	fun register(user: UserRequest) = restHelper.registerUser(user)
//		val result = restHelper.registerUser(user)
//		if (result.isSuccessful && result.body() != null && result.body()?.id != null) {
//			saveUserId(result.body()?.id!!)
//			saveUserCredentials(user.email, user.password)
//		}
//	}
	
	fun getMyProfile() = restHelper.getMyProfile()
	
	fun saveUserCredentials(userEmail: String, password: String) {
		val accountManager = AccountManager.get(context)
		val account = Account(userEmail, TYPE_ACCOUNT)
		accountManager.addAccountExplicitly(account, password, null)
	}
	
	fun getGuests(eventId: Long) = restHelper.getEventGuests(eventId)
}