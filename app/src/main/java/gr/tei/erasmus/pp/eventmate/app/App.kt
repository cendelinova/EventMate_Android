package gr.tei.erasmus.pp.eventmate.app

import android.app.Application
import gr.tei.erasmus.pp.eventmate.BuildConfig
import gr.tei.erasmus.pp.eventmate.dependencyInjection.AppComponent
import gr.tei.erasmus.pp.eventmate.dependencyInjection.AppModule
import gr.tei.erasmus.pp.eventmate.dependencyInjection.DaggerAppComponent
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