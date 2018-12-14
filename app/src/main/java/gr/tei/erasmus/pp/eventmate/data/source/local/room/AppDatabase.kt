package gr.tei.erasmus.pp.eventmate.data.source.local.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.EventDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.InvitationDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.TaskDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.EventEntity
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.InvitationEntity
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.TaskEntity

@Database(entities = [(EventEntity::class), TaskEntity::class, InvitationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
	abstract fun eventDao(): EventDao
	abstract fun taskDao(): TaskDao
	abstract fun invitationDao(): InvitationDao
}