package gr.tei.erasmus.pp.eventmate.ui.base

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {
	
	private val job: Job = Job()
	override val coroutineContext: CoroutineContext = job + Dispatchers.IO
	
	override fun onCleared() {
		job.cancel()
		super.onCleared()
	}
}