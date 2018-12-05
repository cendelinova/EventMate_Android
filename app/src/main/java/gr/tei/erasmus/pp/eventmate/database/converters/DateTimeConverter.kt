package gr.tei.erasmus.pp.eventmate.database.converters

import android.arch.persistence.room.TypeConverter
import org.joda.time.DateTime

class DateTimeConverter {
	
	@TypeConverter
	fun toDate(timestamp: Long?): DateTime? = if (timestamp == null) null else DateTime(timestamp)
	
	@TypeConverter
	fun toTimestamp(date: DateTime?): Long? = date?.millis
}
