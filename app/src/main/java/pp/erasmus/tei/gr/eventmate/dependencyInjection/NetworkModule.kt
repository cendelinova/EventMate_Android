package pp.erasmus.tei.gr.eventmate.dependencyInjection

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class NetworkModule(private val restApiUrl: String) {
	@Provides
	@Singleton
	fun provideHttpClient() = OkHttpClient()
	
	@Provides
	@Singleton
	fun provideGson(): Gson = Gson()
}