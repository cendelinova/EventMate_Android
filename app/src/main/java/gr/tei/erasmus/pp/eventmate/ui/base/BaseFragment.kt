package gr.tei.erasmus.pp.eventmate.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment: Fragment(), CoroutineScope {
	private val job: Job = Job()
	override val coroutineContext: CoroutineContext = job + Dispatchers.Main
	
	protected fun <R> observe(data: LiveData<R>, observer: Observer<R>) = data.observe(this, observer)
	
	override fun onDestroyView() {
		super.onDestroyView()
		job.cancel()
	}
}