package gr.tei.erasmus.pp.eventmate.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import gr.tei.erasmus.pp.eventmate.helpers.BasicAuthInterceptor
import gr.tei.erasmus.pp.eventmate.helpers.RestHelper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule(private val restApiUrl: String) {
	@Provides
	@Singleton
	fun provideOkHttpClient(): OkHttpClient =
		OkHttpClient.Builder().addInterceptor(BasicAuthInterceptor("user1", "pass")).build()
	
	@Provides
	@Singleton
	fun provideGson(): Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
	
	@Provides
	@Singleton
	fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
		.baseUrl(restApiUrl)
		.client(okHttpClient)
		.addConverterFactory(GsonConverterFactory.create())
		.addCallAdapterFactory(CoroutineCallAdapterFactory())
		.build()
	
	@Provides
	@Singleton
	fun provideRestHelper(retrofit: Retrofit) = RestHelper(retrofit)
}