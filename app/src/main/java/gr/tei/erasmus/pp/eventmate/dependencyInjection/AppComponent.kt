package gr.tei.erasmus.pp.eventmate.dependencyInjection

import android.content.Context
import dagger.Component
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
}