package gr.tei.erasmus.pp.eventmate.data.model

data class UserRequest(
	val userName: String,
	val password: String,
	val email: String,
	val photo: String?
) {
	constructor(password: String, email: String) : this("", password, email, null)
}