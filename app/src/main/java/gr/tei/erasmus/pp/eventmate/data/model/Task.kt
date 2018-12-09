package gr.tei.erasmus.pp.eventmate.data.model

data class Task(val name: String, val points: Int, private val assigness: List<User>?)
