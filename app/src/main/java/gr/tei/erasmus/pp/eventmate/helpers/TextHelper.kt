package gr.tei.erasmus.pp.eventmate.helpers

import android.graphics.drawable.BitmapDrawable
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.User
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
	
	fun createContactChips(
		listOfInput: MutableList<*>,
		chipGroup: ChipGroup,
		onCloseListener: View.OnClickListener?,
		isCloseIconVisible: Boolean = true
	) {
		listOfInput.forEach { contact ->
			
			val chip = Chip(chipGroup.context)
			
			with(chip) {
				
				if (contact is User) {
					chipIcon = if (contact.photo.isNullOrEmpty()) ContextCompat.getDrawable(
						chipGroup.context,
						R.drawable.ic_user_placeholder
					) else BitmapDrawable(resources, FileHelper.decodeImage(contact.photo))
					text = contact.userName
					
				} else {
					text = contact.toString()
				}
				
				if (onCloseListener == null) {
					chipBackgroundColor = ContextCompat.getColorStateList(chipGroup.context, R.color.white )
					chipStrokeWidth = 2f
					chipStrokeColor = ContextCompat.getColorStateList(chipGroup.context, R.color.light_grey)
					chipGroup.chipSpacingVertical = 10
				}
				
				this.isCloseIconVisible = isCloseIconVisible
				isChipIconVisible = true
				setOnCloseIconClickListener(onCloseListener)
				
			}
			
			chipGroup.addView(chip)
		}
		
	}
	
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