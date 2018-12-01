package gr.tei.erasmus.pp.eventmate.ui.signup

import android.support.design.widget.TextInputLayout
import com.mobsandgeeks.saripaar.adapter.ViewDataAdapter
import com.mobsandgeeks.saripaar.exception.ConversionException


class TextInputLayoutAdapter : ViewDataAdapter<TextInputLayout, String> {
	@Throws(ConversionException::class)
	override fun getData(view: TextInputLayout): String {
		return view.editText!!.text.toString()
	}
}