package gr.tei.erasmus.pp.eventmate.data.source.local.room

import android.arch.persistence.room.*
import gr.tei.erasmus.pp.eventmate.data.source.local.room.converters.DateTimeConverter
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.EventDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.EventEntity

@Database(entities = [(EventEntity::class)], version = 1, exportSchema = false)
@TypeConverters(DateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
	abstract fun eventDao(): EventDao
}