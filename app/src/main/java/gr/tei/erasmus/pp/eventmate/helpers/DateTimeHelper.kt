package gr.tei.erasmus.pp.eventmate.helpers

import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import timber.log.Timber
import java.text.ParseException

class DateTimeHelper {
	companion object {
		const val DATE_FORMAT = "dd/MM/yyyy"
		const val TIME_FORMAT = "HH:mm"
		const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm"
		const val FULL_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
		
		fun isToday(dateTime: DateTime): Boolean = (dateTime.toLocalDate()) == (LocalDate())
		
		fun parseDateTimeFromString(dateTimeString: String?, format: String? = null): DateTime? {
			if (dateTimeString.isNullOrEmpty()) return null
			
			var parsedDateTime: DateTime? = null
			
			try {
				parsedDateTime = if (format == null) DateTime.parse(dateTimeString) else DateTime.parse(
					dateTimeString,
					getFormatter(format)
				)
			} catch (ignored: ParseException) {
				Timber.e(ignored)
			}
			
			return parsedDateTime
		}
		
		fun convertDateTimeToString(dateTime: DateTime?, format: String): String {
			dateTime?.run {
				val formatter = getFormatter(format)
				return formatter.print(dateTime)
			}
			
			return ""
		}
		
		fun formatDateTimeString(dateTimeString: String, format: String): String {
			val dateTime = parseDateTimeFromString(dateTimeString)
			return convertDateTimeToString(dateTime, format)
		}
		
		private fun getFormatter(format: String) = DateTimeFormat.forPattern(format)
		
	}
}