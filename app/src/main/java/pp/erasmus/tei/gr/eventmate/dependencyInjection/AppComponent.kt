package pp.erasmus.tei.gr.eventmate.dependencyInjection

import android.content.Context
import com.google.gson.Gson
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
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