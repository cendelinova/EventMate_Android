package gr.tei.erasmus.pp.eventmate.di

import android.arch.persistence.room.Room
import android.content.Context
import android.content.ContextWrapper
import dagger.Module
import dagger.Provides
import gr.tei.erasmus.pp.eventmate.data.repository.EventRepository
import gr.tei.erasmus.pp.eventmate.data.repository.TaskRepository
import gr.tei.erasmus.pp.eventmate.data.source.local.room.AppDatabase
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.EventDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.TaskDao
import gr.tei.erasmus.pp.eventmate.helpers.RestHelper
import gr.tei.erasmus.pp.eventmate.helpers.SharedPreferenceHelper
import javax.inject.Singleton

@Module
class AppModule(context: Context) : ContextWrapper(context) {
	
	companion object {
		private const val DATABASE_NAME = "eventmate-db"
	}
	
	private lateinit var roomDatabase: AppDatabase
	
	@Provides
	@Singleton
	fun provideContext(): Context = applicationContext
	
	@Provides
	@Singleton
	fun provideDatabase(context: Context): AppDatabase {
		roomDatabase =
				Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).allowMainThreadQueries().build()
		return roomDatabase
	}
	
	
	@Provides
	@Singleton
	fun provideEventDao(roomDatabase: AppDatabase) = roomDatabase.eventDao()
	
	@Provides
	@Singleton
	fun provideEventRepository(eventDao: EventDao) = EventRepository(eventDao)
	
	@Provides
	@Singleton
	fun provideTaskDao(roomDatabase: AppDatabase) = roomDatabase.taskDao()
	
	@Provides
	@Singleton
	fun provideTaskRepository(taskDao: TaskDao) = TaskRepository(taskDao)
	
	@Provides
	@Singleton
	fun provideSharedPreferenceHelper(context: Context) = SharedPreferenceHelper(context)
	
	
}