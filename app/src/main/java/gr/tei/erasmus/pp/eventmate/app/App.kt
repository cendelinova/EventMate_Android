package gr.tei.erasmus.pp.eventmate.app

import android.app.Application
import android.preference.PreferenceManager
import gr.tei.erasmus.pp.eventmate.BuildConfig
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_MAIL
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_PASSWORD
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.di.AppComponent
import gr.tei.erasmus.pp.eventmate.di.AppModule
import gr.tei.erasmus.pp.eventmate.di.DaggerAppComponent
import gr.tei.erasmus.pp.eventmate.di.NetworkModule
import timber.log.Timber

class App : Application() {
	
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
		val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
		val email: String = sharedPreferences.getString(USER_MAIL, "")
		val password: String = sharedPreferences.getString(USER_PASSWORD, "")
		val user = if (email.isNotEmpty() && password.isNotEmpty()) {
			User(email, password)
		} else {
			null
		}
		COMPONENTS = DaggerAppComponent.builder()
			.appModule(AppModule(this))
			.networkModule(NetworkModule(this, user, BuildConfig.SERVER_URL))
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