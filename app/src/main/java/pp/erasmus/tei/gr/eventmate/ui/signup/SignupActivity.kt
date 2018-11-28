package pp.erasmus.tei.gr.eventmate.ui.signup

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import pp.erasmus.tei.gr.eventmate.R
import pp.erasmus.tei.gr.eventmate.ui.base.BaseActivity
import pp.erasmus.tei.gr.eventmate.ui.base.NoToolbarActivity

class SignupActivity : NoToolbarActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_signup)
	}
}
