package gr.tei.erasmus.pp.eventmate.ui.events

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.helpers.DateTimeHelper
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import kotlinx.android.synthetic.main.event_item.view.*
import timber.log.Timber

class EventAdapter(
	private val context: Context,
	private val eventListener: EventListener,
	private var events: MutableList<Event>
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
	
	private val userRoleHelper = App.COMPONENTS.provideUserRoleHelper()
	
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
			view_foreground.setOnClickListener { eventListener.onItemClick(event) }
			event_name.text = event.name
			event_date.text = DateTimeHelper.formatDateTimeString(event.date, DateTimeHelper.DATE_TIME_FORMAT)
			event_place.text = if (event.place.isEmpty()) "-" else event.place
			event_guests_count.text = event.usersCount.toString()
			event_tasks_count.text = event.taskCount.toString()
			
			indicator_owner.visibility =
					if (event.eventOwner?.let { userRoleHelper.isSameUser(it) } == true) View.VISIBLE else View.GONE
			
			event.photo?.let {
				if (it.isNotEmpty()) event_photo.setImageBitmap(FileHelper.decodeImage(it))
			}
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