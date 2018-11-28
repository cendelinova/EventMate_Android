package pp.erasmus.tei.gr.eventmate.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.WindowManager
import pp.erasmus.tei.gr.eventmate.R

abstract class BaseActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		setupToolbar()
		
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
	}
	
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
		android.R.id.home -> {
			onBackPressed()
			true
		}
		else -> super.onOptionsItemSelected(item)
	}
	
	override fun onDestroy() {
		super.onDestroy()
	}
	
	/* Private methods ****************************************************************************/
	
	private fun setupToolbar() {
		setSupportActionBar(findViewById(R.id.toolbar))
		
		supportActionBar?.run {
			setDisplayHomeAsUpEnabled(true)
			setDisplayShowTitleEnabled(false)
		}
		
	}
}