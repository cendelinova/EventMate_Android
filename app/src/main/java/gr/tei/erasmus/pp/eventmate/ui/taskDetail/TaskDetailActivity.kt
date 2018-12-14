package gr.tei.erasmus.pp.eventmate.ui.taskDetail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import kotlinx.android.synthetic.main.toolbar_task_detail.*

class TaskDetailActivity : BaseActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_task_detail)
		
		setupToolbar(toolbar)
		
	}
}
