package gr.tei.erasmus.pp.eventmate.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {
	
	protected fun <R> observe(data: LiveData<R>, observer: Observer<R>) = data.observe(this, observer)
	
}