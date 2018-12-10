package gr.tei.erasmus.pp.eventmate.di

import android.content.Context
import dagger.Component
import gr.tei.erasmus.pp.eventmate.data.repository.EventRepository
import gr.tei.erasmus.pp.eventmate.data.repository.TaskRepository
import gr.tei.erasmus.pp.eventmate.data.source.local.room.AppDatabase
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.EventDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.TaskDao
import gr.tei.erasmus.pp.eventmate.helpers.SharedPreferenceHelper
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
	fun provideContext(): Context
//
//	fun provideOkHttpClient(): OkHttpClient
//
//	fun provideRetrofit(): Retrofit
//
//	fun provideGson(): Gson
	
	fun provideDatabase(): AppDatabase
	
	fun provideEventDao(): EventDao
	
	fun provideTaskDao(): TaskDao
	
	fun provideEventRepository(): EventRepository
	
	fun provideTaskRepository(): TaskRepository
	
	fun provideSharedPreferencesHelper(): SharedPreferenceHelper
	
}