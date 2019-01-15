package gr.tei.erasmus.pp.eventmate.di

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_MAIL
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_PASSWORD
import gr.tei.erasmus.pp.eventmate.helpers.RestHelper
import gr.tei.erasmus.pp.eventmate.helpers.authetification.BasicAuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule(private val context: Context, private val restApiUrl: String) {
	private val sharedPreferenceHelper by lazy {
		PreferenceManager.getDefaultSharedPreferences(context)
	}
	
	@Provides
	@Singleton
	fun provideOkHttpClient(): OkHttpClient =
		OkHttpClient.Builder().addInterceptor(
			BasicAuthInterceptor(
				sharedPreferenceHelper.getString(USER_MAIL, "")!!, sharedPreferenceHelper.getString(
					USER_PASSWORD, ""
				)!!
			)
		).build()
	
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