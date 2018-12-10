package gr.tei.erasmus.pp.eventmate.data.source.local.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import gr.tei.erasmus.pp.eventmate.data.source.local.room.converters.DateTimeConverter
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.EventDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.TaskDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.EventEntity
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.TaskEntity

@Database(entities = [(EventEntity::class), TaskEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
	abstract fun eventDao(): EventDao
	abstract fun taskDao(): TaskDao
}