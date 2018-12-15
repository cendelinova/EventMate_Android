package gr.tei.erasmus.pp.eventmate.helpers.validation

import android.widget.EditText
import gr.tei.erasmus.pp.eventmate.helpers.DateTimeHelper
import org.joda.time.DateTime

class FutureTimeRule(private val pickedDate: DateTime?) {
	
	fun isValid(view: EditText?): Boolean {
		if (pickedDate == null) return false
		pickedDate.run {
			val parsedTime =
				DateTimeHelper.parseDateTimeFromString(view?.text.toString(), DateTimeHelper.TIME_FORMAT)
					?: return false
			
			// set time
			val pickedDateTime = pickedDate.withTime(parsedTime.toLocalTime())
			
			return pickedDateTime.isAfterNow
		}
	}
	
}