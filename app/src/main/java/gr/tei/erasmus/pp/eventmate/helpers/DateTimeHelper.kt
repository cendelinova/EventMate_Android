package gr.tei.erasmus.pp.eventmate.helpers

import org.joda.time.DateTime
import org.joda.time.LocalDate

class DateTimeHelper {
	companion object {
		const val DATE_FORMAT = "MM/dd/yyyy"
		const val TIME_FORMAT = "HH:mm"
		const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
		
		fun isToday(dateTime: DateTime): Boolean = (dateTime.toLocalDate()) == (LocalDate())
	}
}