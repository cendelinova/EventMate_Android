package gr.tei.erasmus.pp.eventmate.helpers

import android.content.Context
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_ID
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_MAIL
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.data.repository.UserRepository

class UserRoleHelper(
	val context: Context,
	private val userRepository: UserRepository,
	private val sharedPreferenceHelper: SharedPreferenceHelper
) {
	
	
	fun isSameUser(user: User): Boolean {
		return sharedPreferenceHelper.loadLong(USER_ID) == user.id && sharedPreferenceHelper.loadString(USER_MAIL) == user.email
	}
	
}