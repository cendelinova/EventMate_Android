package gr.tei.erasmus.pp.eventmate.helpers

import android.content.Context
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.data.repository.UserRepository

class UserRoleHelper(
	val context: Context,
	private val userRepository: UserRepository,
	val sharedPreferenceHelper: SharedPreferenceHelper
) {
	
	
	fun isEventOwner(user: User): Boolean {
		userRepository.getUserCredentials().also {
		
		}
		
		return true
	}
	
	fun isSenderMe(user: User): Boolean {
		// toodo
		return true
	}
	
}