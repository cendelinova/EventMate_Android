package gr.tei.erasmus.pp.eventmate.data.model

data class User(
	val id: Long?,
	val userName: String,
	val password: String?,
	val email: String,
	val score: Int,
	val photo: String?,
	val attendedEvents: Int,
	val organizedEvents: Int,
	var checked: Boolean = false
) {
	
	constructor(userName: String) : this(
		null,
		userName,
		null,
		"",
		0,
		null,
		0,
		0
	)
	
	constructor(id: Long?, userName: String) :
			this(
				id,
				userName,
				null,
				"",
				0,
				null,
				0,
				0
			)
	
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

