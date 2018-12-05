package gr.tei.erasmus.pp.eventmate.database

import android.arch.persistence.db.SupportSQLiteOpenHelper
import android.arch.persistence.room.*
import gr.tei.erasmus.pp.eventmate.database.converters.DateTimeConverter
import gr.tei.erasmus.pp.eventmate.database.dao.EventDao
import gr.tei.erasmus.pp.eventmate.database.entities.EventEntity

@Database(entities = [(EventEntity::class)], version = 1, exportSchema = false)
@TypeConverters(DateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
	abstract fun eventDao(): EventDao
}