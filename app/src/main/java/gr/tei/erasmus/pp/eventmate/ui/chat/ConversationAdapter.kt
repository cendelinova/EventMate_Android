package gr.tei.erasmus.pp.eventmate.ui.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.ChatMessage
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import kotlinx.android.synthetic.main.conversation_item.view.*
import org.ocpsoft.prettytime.PrettyTime
import timber.log.Timber

class ConversationAdapter(
	private val context: Context,
	private val conversationListener: ConversationAdapter.ConversationListener,
	private var conversations: MutableList<ChatMessage>
) : RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {
	
	private val userRoleHelper = App.COMPONENTS.provideUserRoleHelper()
	
	override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ConversationViewHolder {
		Timber.d("onCreateViewHolder() called with parent = [$viewGroup], viewType = [$viewType]")
		
		return ConversationViewHolder(
			LayoutInflater.from(context).inflate(
				R.layout.conversation_item,
				viewGroup,
				false
			)
		)
	}
	
	override fun getItemCount() = conversations.size
	
	override fun onBindViewHolder(viewHolder: ConversationViewHolder, position: Int) {
		Timber.v("onBindViewHolder() called with: holder = [$viewHolder], position = [$position]")
		
		displayEventEntry(viewHolder, conversations[viewHolder.adapterPosition])
	}
	
	private fun displayEventEntry(viewHolder: ConversationViewHolder, chatMessage: ChatMessage) {
		
		with(viewHolder.itemView) {
			
			val contact =
				if (chatMessage.from?.let { userRoleHelper.isSenderMe(it) }!!) chatMessage.to else chatMessage.from
			conversation_name.text = contact.userName
			contact.photo?.let {
				conversation_photo.setImageBitmap(FileHelper.decodeImage(it))
			}
			
			conversation_item.setOnClickListener { contact.id?.let { conversationListener.onConversationClick(contact) } }
			
			conversation_photo.setOnClickListener {
				contact.id?.let { it1 -> conversationListener.onUserPhotoClick(it1) }
			}
			
			message.text = chatMessage.content
			sent_time.text = PrettyTime().format(chatMessage.date)
		}
	}
	
	/**
	 * Update and notify data set change.
	 */
	fun updateConversationList(updatedConversations: MutableList<ChatMessage>) {
		conversations = updatedConversations
		notifyDataSetChanged()
	}
	
	interface ConversationListener {
		fun onConversationClick(user: User)
		fun onUserPhotoClick(userId: Long)
	}
	
	inner class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

