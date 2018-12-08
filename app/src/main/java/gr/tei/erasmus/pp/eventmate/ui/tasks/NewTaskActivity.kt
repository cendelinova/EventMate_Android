package gr.tei.erasmus.pp.eventmate.ui.tasks

import android.os.Bundle
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_new_task.*

class NewTaskActivity : BaseActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_new_task)
		setupToolbar(toolbar)
		
	}
	
}
