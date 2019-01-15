package gr.tei.erasmus.pp.eventmate.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : AppCompatActivity(), CoroutineScope {
	
	private val job: Job = Job()
	override val coroutineContext: CoroutineContext = job + Dispatchers.Main
	
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
		job.cancel()
	}
	
	/* Protected methods ****************************************************************************/
	
	protected fun setupToolbar(toolbar: Toolbar, displayHomeAsUp: Boolean = true, displayTitle: Boolean = false) {
		setSupportActionBar(toolbar)
		
		supportActionBar?.run {
			setDisplayHomeAsUpEnabled(displayHomeAsUp)
			setDisplayShowTitleEnabled(displayTitle)
		}
	}
	
	protected fun <R> observe(data: LiveData<R>, observer: Observer<R>) = data.observe(this, observer)
	
}