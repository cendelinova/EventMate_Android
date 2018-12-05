package gr.tei.erasmus.pp.eventmate.dependencyInjection

import android.arch.persistence.room.Room
import android.content.Context
import android.content.ContextWrapper
import dagger.Module
import dagger.Provides
import gr.tei.erasmus.pp.eventmate.database.AppDatabase
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) : ContextWrapper(context) {
	
	companion object {
		private const val DATABASE_NAME = "eventmate-db"
	}
	
	@Provides
	@Singleton
	fun provideContext(): Context = applicationContext
	
	@Provides
	@Singleton
	fun provideDatabase(context: Context) =
		Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).allowMainThreadQueries().build()
	
	
}