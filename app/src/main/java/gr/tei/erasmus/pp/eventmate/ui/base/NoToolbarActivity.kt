package gr.tei.erasmus.pp.eventmate.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class NoToolbarActivity: AppCompatActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		supportActionBar?.hide()
	}
}