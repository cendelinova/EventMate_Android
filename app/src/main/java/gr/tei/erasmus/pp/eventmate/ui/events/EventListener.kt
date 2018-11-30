package gr.tei.erasmus.pp.eventmate.ui.events

import gr.tei.erasmus.pp.eventmate.models.Event

interface EventListener {
	fun onItemClick(event: Event)
}