package gr.tei.erasmus.pp.eventmate.helpers

import androidx.appcompat.widget.SearchView
import com.google.android.material.textfield.TextInputLayout
import gr.tei.erasmus.pp.eventmate.ui.base.AbstractFilterAdapter

object TextHelper {
	
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
	
	fun getDefaultTextIfEmpty(text: String?) = if (text.isNullOrEmpty() || text.contains("null", false)) "-" else text
	
	fun getQueryTextListener(adapter: AbstractFilterAdapter): SearchView.OnQueryTextListener =
		object : SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String?): Boolean {
				return false
			}
			
			override fun onQueryTextChange(newText: String?): Boolean {
				adapter.filter?.filter(newText)
				return false
			}
			
		}
	
}