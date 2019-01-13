package gr.tei.erasmus.pp.eventmate.data.model

import java.util.*

data class Message(val id: Long?, val from: User, val to: User, val date: Date, val content: String)