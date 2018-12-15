package gr.tei.erasmus.pp.eventmate.data.repository

import gr.tei.erasmus.pp.eventmate.helpers.RestHelper

class UserRepository(private val restHelper: RestHelper) {
	
	fun getUser(userId : Long) = restHelper.getUser(userId)
}