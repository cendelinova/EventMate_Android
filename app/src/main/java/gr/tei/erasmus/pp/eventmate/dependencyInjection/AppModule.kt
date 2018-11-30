package gr.tei.erasmus.pp.eventmate.dependencyInjection

import android.content.Context
import android.content.ContextWrapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) : ContextWrapper(context) {
	@Provides
	@Singleton
	fun provideContext(): Context = applicationContext
	
}