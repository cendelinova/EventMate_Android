package gr.tei.erasmus.pp.eventmate.ui.events

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.helpers.DateTimeHelper
import gr.tei.erasmus.pp.eventmate.data.model.Event
import kotlinx.android.synthetic.main.event_item.view.*
import timber.log.Timber

class EventAdapter(
	private val context: Context,
	private val eventListener: EventListener,
	private var events: MutableList<Event>
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
		Timber.d("onCreateViewHolder() called with parent = [$parent], viewType = [$viewType]")
		
		return EventViewHolder(LayoutInflater.from(context).inflate(R.layout.event_item, parent, false))
	}
	
	override fun getItemCount() = events.size
	
	override fun onBindViewHolder(viewHolder: EventViewHolder, position: Int) {
		Timber.v("onBindViewHolder() called with: holder = [$viewHolder], position = [$position]")
		
		displayEventEntry(viewHolder, events[viewHolder.adapterPosition])
	}
	
	private fun displayEventEntry(viewHolder: EventViewHolder, event: Event) {
		with(viewHolder.itemView) {
			events_fragment.setOnClickListener { eventListener.onItemClick(event) }
			event_name.text = event.name
			event_date.text = event.date
		}
	}
	
	/**
	 * Update and notify data set change.
	 */
	fun updateEventList(updatedEvents: MutableList<Event>) {
		events = updatedEvents
		notifyDataSetChanged()
	}
	
	/* Inner classes ******************************************************************************/
	inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
}