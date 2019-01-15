package gr.tei.erasmus.pp.eventmate.data.model

import org.joda.time.DateTime
import java.util.*

data class ChatMessage(val id: Long?, val from: User?, val to: User, val date: Date?, val content: String) {
	constructor(to: User, content: String) : this(null, null, to, null, content)
}