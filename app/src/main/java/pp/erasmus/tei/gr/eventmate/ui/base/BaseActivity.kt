package pp.erasmus.tei.gr.eventmate.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager

abstract class BaseActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
	}
}