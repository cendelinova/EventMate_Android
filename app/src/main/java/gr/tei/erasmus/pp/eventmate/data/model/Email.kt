package gr.tei.erasmus.pp.eventmate.data.model

data class Email(val subject: String, val text: String, val recipients: List<String>)