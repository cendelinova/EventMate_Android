package gr.tei.erasmus.pp.eventmate.di

import android.content.Context
import android.content.ContextWrapper
import androidx.room.Room
import dagger.Module
import dagger.Provides
import gr.tei.erasmus.pp.eventmate.data.repository.EventRepository
import gr.tei.erasmus.pp.eventmate.data.repository.InvitationRepository
import gr.tei.erasmus.pp.eventmate.data.repository.TaskRepository
import gr.tei.erasmus.pp.eventmate.data.repository.UserRepository
import gr.tei.erasmus.pp.eventmate.data.source.local.room.AppDatabase
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.EventDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.InvitationDao
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
	fun provideTaskRepository(restHelper: RestHelper, taskDao: TaskDao) = TaskRepository(restHelper, taskDao)
	
	@Provides
	@Singleton
	fun provideSharedPreferenceHelper(context: Context) = SharedPreferenceHelper(context)
	
	@Provides
	@Singleton
	fun provideInvitationDao(roomDatabase: AppDatabase) = roomDatabase.invitationDao()
	
	@Provides
	@Singleton
	fun provideInvitationRepository(invitationDao: InvitationDao) = InvitationRepository(invitationDao)
	
	@Provides
	@Singleton
	fun provideUserRepository(restHelper: RestHelper) = UserRepository(restHelper)
	
	
}