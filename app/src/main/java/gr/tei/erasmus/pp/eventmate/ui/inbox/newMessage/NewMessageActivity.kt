package gr.tei.erasmus.pp.eventmate.ui.inbox.newMessage

import android.os.Bundle
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : BaseActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_new_message)
		
		setupToolbar(toolbar, true)
		
	}
}
