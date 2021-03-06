package gr.tei.erasmus.pp.eventmate.di

import android.content.Context
import com.google.gson.Gson
import dagger.Component
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.data.repository.*
import gr.tei.erasmus.pp.eventmate.data.source.local.room.AppDatabase
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.EventDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.InvitationDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.TaskDao
import gr.tei.erasmus.pp.eventmate.helpers.RestHelper
import gr.tei.erasmus.pp.eventmate.helpers.SharedPreferenceHelper
import gr.tei.erasmus.pp.eventmate.helpers.UserRoleHelper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
	fun provideContext(): Context
	
	fun provideOkHttpClient(): OkHttpClient
	
	fun provideRetrofit(): Retrofit
	
	fun provideGson(): Gson
	
	fun provideDatabase(): AppDatabase
	
	fun provideEventDao(): EventDao
	
	fun provideTaskDao(): TaskDao
	
	fun provideEventRepository(): EventRepository
	
	fun provideTaskRepository(): TaskRepository
	
	fun provideSubmissionRepository(): SubmissionRepository
	
	fun provideSharedPreferencesHelper(): SharedPreferenceHelper
	
	fun provideRestHelper(): RestHelper
	
	fun provideInvitationDao(): InvitationDao
	
	fun provideInvitationRepository(): InvitationRepository
	
	fun provideUserRepository(): UserRepository
	
	fun provideReportRepository(): ReportRepository
	
	fun provideUserRoleHelper(): UserRoleHelper

	fun provideChatRepository(): ChatRepository
}