package gr.tei.erasmus.pp.eventmate.ui.base

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.v4.app.Fragment

abstract class BaseFragment: Fragment() {
	
	protected fun <R> observe(data: LiveData<R>, observer: Observer<R>) = data.observe(this, observer)
	
}