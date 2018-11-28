package pp.erasmus.tei.gr.eventmate.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class NoToolbarActivity: AppCompatActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		supportActionBar?.hide()
	}
}