package gr.tei.erasmus.pp.eventmate.helpers

import android.support.design.widget.TextInputLayout

object TextInputLayoutHelper {
	
	fun clearInputs(listOfInputs: MutableList<TextInputLayout>) {
		for (input in listOfInputs) {
			input.isErrorEnabled = false
			input.error = null
		}
	}
}