package gr.tei.erasmus.pp.eventmate.ui.inbox

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.Conversation
import kotlinx.android.synthetic.main.conversation_item.view.*
import timber.log.Timber

class ConversationAdapter(
	private val context: Context,
	private val conversationListener: ConversationAdapter.ConversationListener,
	private var conversations: MutableList<Conversation>
) : RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {
	override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ConversationViewHolder {
		Timber.d("onCreateViewHolder() called with parent = [$viewGroup], viewType = [$viewType]")
		
		return ConversationViewHolder(LayoutInflater.from(context).inflate(R.layout.conversation_item, viewGroup, false))
	}
	
	override fun getItemCount() = conversations.size
	
	override fun onBindViewHolder(viewHolder: ConversationViewHolder, position: Int) {
		Timber.v("onBindViewHolder() called with: holder = [$viewHolder], position = [$position]")
		
		displayEventEntry(viewHolder, conversations[viewHolder.adapterPosition])
	}
	
	private fun displayEventEntry(viewHolder: ConversationViewHolder, conversation: Conversation) {
		
		with(viewHolder.itemView) {
			conversation_item.setOnClickListener { conversationListener.onConversationClick(conversation) }
//			Picasso.get().load(conversation.photo).into(conversation_photo)
			conversation_name.text = conversation.name
			message.text = conversation.lastMessageText
			sent_time.text = conversation.lastMessageTime
		}
	}
	
	/**
	 * Update and notify data set change.
	 */
	fun updateConversationList(updatedConversations: MutableList<Conversation>) {
		conversations = updatedConversations
		notifyDataSetChanged()
	}
	
	interface ConversationListener {
		fun onConversationClick(conversation: Conversation)
	}
	
	
	inner class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}