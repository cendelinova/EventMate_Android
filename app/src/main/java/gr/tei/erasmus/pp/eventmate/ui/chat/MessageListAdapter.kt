package gr.tei.erasmus.pp.eventmate.ui.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.ChatMessage
import gr.tei.erasmus.pp.eventmate.helpers.DateTimeHelper
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import org.joda.time.DateTime


class MessageListAdapter(private val context: Context, private var messages: List<ChatMessage>) :
	RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	
	override fun getItemCount() = messages.size
	
	// Determines the appropriate ViewType according to the sender of the message.
	override fun getItemViewType(position: Int): Int {
		val message = messages[position]
		// todo current user
		return if (message.from?.id == 2L) {
			// If the current user is the sender of the message
			VIEW_TYPE_MESSAGE_SENT
		} else {
			// If some other user sent the message
			VIEW_TYPE_MESSAGE_RECEIVED
		}
	}
	
	// Inflates the appropriate layout according to the ViewType.
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val view: View
		var viewHolder: RecyclerView.ViewHolder? = null
		if (viewType == VIEW_TYPE_MESSAGE_SENT) {
			view = LayoutInflater.from(parent.context)
				.inflate(R.layout.item_message_sent, parent, false)
			viewHolder = SentMessageHolder(view)
		} else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
			view = LayoutInflater.from(parent.context)
				.inflate(R.layout.item_message_received, parent, false)
			viewHolder = ReceivedMessageHolder(view)
		}
		return viewHolder!!
	}
	
	// Passes the message object to a ViewHolder so that the contents can be bound to UI.
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		val message = messages[position]
		
		when (holder.itemViewType) {
			VIEW_TYPE_MESSAGE_SENT -> (holder as SentMessageHolder).bind(message)
			VIEW_TYPE_MESSAGE_RECEIVED -> (holder as ReceivedMessageHolder).bind(message)
		}
	}
	
	fun updateConversationList(messages: MutableList<ChatMessage>) {
		this.messages = messages
		notifyDataSetChanged()
	}
	
	private inner class SentMessageHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
		internal var messageText: TextView = itemView.findViewById(R.id.text_message_body)
		internal var timeText: TextView = itemView.findViewById(R.id.text_message_time)
		
		internal fun bind(message: ChatMessage) {
			messageText.text = message.content
			
			// Format the stored timestamp into a readable String using method.
			timeText.text = DateTimeHelper.convertDateTimeToString(DateTime(message.date), DateTimeHelper.TIME_FORMAT)
		}
	}
	
	private inner class ReceivedMessageHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
		internal var messageText: TextView = itemView.findViewById(R.id.text_message_body)
		internal var timeText: TextView = itemView.findViewById(R.id.text_message_time)
		internal var profileImage: ImageView = itemView.findViewById(R.id.image_message_profile) as ImageView
		
		internal fun bind(message: ChatMessage) {
			messageText.text = message.content
			timeText.text = DateTimeHelper.convertDateTimeToString(DateTime(message.date), DateTimeHelper.TIME_FORMAT)
//			// Insert the profile image from the URL into the ImageView.
			profileImage.setImageBitmap(message.from?.photo?.let { FileHelper.decodeImage(it) })
		}
	}
	
	companion object {
		private const val VIEW_TYPE_MESSAGE_SENT = 1
		private const val VIEW_TYPE_MESSAGE_RECEIVED = 2
	}
}