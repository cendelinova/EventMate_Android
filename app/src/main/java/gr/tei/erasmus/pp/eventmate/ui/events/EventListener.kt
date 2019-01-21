package gr.tei.erasmus.pp.eventmate.ui.events

import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.EventDetail

interface EventListener {
	fun onItemClick(event: Event)
}