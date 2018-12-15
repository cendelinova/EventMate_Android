package gr.tei.erasmus.pp.eventmate.helpers

import android.support.design.widget.TextInputLayout

object TextInputLayoutHelper {
	
	fun clearInputs(listOfInputs: MutableList<TextInputLayout>) {
		for (input in listOfInputs) {
			input.error = null
			input.isErrorEnabled = false
		}
	}
	
	fun collectValueFromInput(inputLayout: TextInputLayout) = inputLayout.editText?.text.toString()
	
	fun setRequiredMark(listOfInputs: MutableList<TextInputLayout>) {
		for (input in listOfInputs) {
			input.markRequired()
		}
	}
	
	private fun TextInputLayout.markRequired() {
		hint = "$hint *"
	}
	
	fun getDefaultTextIfEmpty(text: String?) = if (text.isNullOrEmpty()) "-" else text
	
}