package gr.tei.erasmus.pp.eventmate.helpers

import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import timber.log.Timber
import java.text.ParseException

class DateTimeHelper {
	companion object {
		const val DATE_FORMAT = "MM/dd/yyyy"
		const val TIME_FORMAT = "HH:mm"
		const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
		
		fun isToday(dateTime: DateTime): Boolean = (dateTime.toLocalDate()) == (LocalDate())
		
		fun parseDateTimeFromString(dateTimeString: String?, format: String): DateTime? {
			val formatter = DateTimeFormat.forPattern(format)
			
			if (dateTimeString.isNullOrEmpty()) return null
			
			var parsedDateTime: DateTime? = null
			
			try {
				parsedDateTime = formatter.parseDateTime(dateTimeString)
			} catch (ignored: ParseException) {
				Timber.e(ignored)
			}
			
			return parsedDateTime
		}
		
		fun convertDateTimeToString(dateTime: DateTime?, format: String): String {
			dateTime?.run {
				val formatter = DateTimeFormat.forPattern(format)
				return formatter.print(dateTime)
			}
			
			return ""
		}
	}
}