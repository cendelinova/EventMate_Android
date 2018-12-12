package gr.tei.erasmus.pp.eventmate.data.model

data class User(val name: String, val photo: String) {

	constructor(name: String) : this(name, "")
}
