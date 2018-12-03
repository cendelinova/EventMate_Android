package gr.tei.erasmus.pp.eventmate.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.WindowManager

abstract class BaseActivity : AppCompatActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
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
	
	protected fun setupToolbar(toolbar: Toolbar, displayHomeAsUp: Boolean = true, displayTitle: Boolean = false) {
		setSupportActionBar(toolbar)
		
		supportActionBar?.run {
			setDisplayHomeAsUpEnabled(displayHomeAsUp)
			setDisplayShowTitleEnabled(displayTitle)
		}
		
	}
}