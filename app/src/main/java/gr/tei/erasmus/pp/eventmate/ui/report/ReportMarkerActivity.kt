package gr.tei.erasmus.pp.eventmate.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.DialogHelper
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_report_marker.*


class ReportMarkerActivity : BaseActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_report_marker)
		setupToolbar(toolbar)
		setupListeners()
	}
	
	private fun setupListeners() {
		tv_show_event_info.setOnClickListener {
			DialogHelper.showCustomDialog(
				this,
				LayoutInflater.from(this).inflate(R.layout.report_event_info_dialog, null),
				null
			)
		}
		tv_display_guests.setOnClickListener {
			DialogHelper.showDialogWithAdapter(
				this,
				ReportGuestAdapter(
					this@ReportMarkerActivity, null, mutableListOf(User("pepa"), User("jenda"), User("evik"))
				),
				LayoutInflater.from(this).inflate(R.layout.report_guest_dialog, null),
				null
			)
			
		}
		tv_include_tasks.setOnClickListener { }
		
	}
	
}

