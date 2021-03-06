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
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.RestHelper
import gr.tei.erasmus.pp.eventmate.helpers.authetification.BasicAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
class NetworkModule(private val context: Context, private val user: User?, private val restApiUrl: String) {
	

	@Provides
	@Singleton
	fun provideOkHttpClient(): OkHttpClient {
		Timber.d(" XXXA vola se")
		val builder = OkHttpClient.Builder()
		with(builder) {
			HttpLoggingInterceptor().apply {
				level = HttpLoggingInterceptor.Level.HEADERS
			addInterceptor(this)
			}
			user?.run {
				Timber.d("XXXA pridame basic")
				addInterceptor(BasicAuthInterceptor(email, password))
				Timber.d("XXXA pridano basic")
				
			}
		}

		return builder.build()
	}
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