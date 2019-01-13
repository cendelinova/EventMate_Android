package gr.tei.erasmus.pp.eventmate.ui.chat.conversationDetail

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.Message
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import gr.tei.erasmus.pp.eventmate.ui.chat.MessageListAdapter
import kotlinx.android.synthetic.main.activity_conversation_detail.*
import kotlinx.android.synthetic.main.activity_conversation_detail.view.*
import timber.log.Timber
import java.util.*

class ConversationDetailActivity : BaseActivity() {
	
	private lateinit var messageListAdapter: MessageListAdapter
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_conversation_detail)
		
		setupToolbar(toolbar)
		
		initializeRecyclerView()
	}
	
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		messageListAdapter = MessageListAdapter(
			this,
			mutableListOf(Message(1, User(1, "josef"), User(2, "pepik"), Date(), "ahojooj"))
		)
		
		with(message_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(message_empty_view)
			layoutManager = LinearLayoutManager(context)
			adapter = messageListAdapter
		}
	}
}
