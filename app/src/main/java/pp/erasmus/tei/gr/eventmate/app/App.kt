package pp.erasmus.tei.gr.eventmate.app

import android.app.Application
import pp.erasmus.tei.gr.eventmate.BuildConfig
import pp.erasmus.tei.gr.eventmate.dependencyInjection.AppComponent
import pp.erasmus.tei.gr.eventmate.dependencyInjection.AppModule
import pp.erasmus.tei.gr.eventmate.dependencyInjection.DaggerAppComponent
import pp.erasmus.tei.gr.eventmate.dependencyInjection.NetworkModule
import timber.log.Timber

class App: Application() {
	
	companion object {
		lateinit var COMPONENTS: AppComponent
	}
	
	override fun onCreate() {
		super.onCreate()
		
		initializeTimber()
		initializeDagger()
	}
	
	/**
	 * Build dependency injection app component used to inject various helpers.
	 */
	private fun initializeDagger() {
		Timber.v("initializeDagger() called")
		
		COMPONENTS = DaggerAppComponent.builder()
			.appModule(AppModule(this))
			.build()
	}
	
	/**
	 * Initialize Timber logging library.
	 * We use debug tree for debug logging.
	 *
	 * @see Timber
	 */
	private fun initializeTimber() {
		if (BuildConfig.DEBUG) {
			Timber.plant(Timber.DebugTree())
		}
	}
	
}