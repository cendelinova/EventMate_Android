package gr.tei.erasmus.pp.eventmate.data.model

data class User(val uid: Long?, val name: String, val email : String, val score: Int, val photo: String?) {

//	constructor(name: String) : this(null, name,"", null)
}
