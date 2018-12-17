package gr.tei.erasmus.pp.eventmate.data.model

data class User(
	val uid: Long?,
	val userName: String,
	val password: String?,
	val email: String,
	val score: Int,
	val photo: String?,
	val attendedEvents: Int,
	val organizedEvents: Int
) {
	constructor(userName: String, password: String?, email: String, photo: String?) : this(
		null,
		userName,
		password,
		email,
		0,
		photo,
		0,
		0
	)
	
}

