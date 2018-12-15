package gr.tei.erasmus.pp.eventmate.helpers

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.preference.PreferenceManager
import timber.log.Timber

class SharedPreferenceHelper(context: Context) : ContextWrapper(context.applicationContext) {
	
	private val sharedPreferences: SharedPreferences
	
	init {
		Timber.v("SharedPreferencesHelper() called with: context = [$context]")
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
	}
	
	fun loadBoolean(key: String) = sharedPreferences.getBoolean(key, false)
	
	fun loadLong(key: String) = sharedPreferences.getLong(key, 0)
	
	fun loadString(key: String) = sharedPreferences.getString(key, "")
	
	fun saveBoolean(key: String, state: Boolean) = sharedPreferences.edit().putBoolean(key, state).apply()
	
	fun saveLong(key: String, state: Long) = sharedPreferences.edit().putLong(key, state).apply()
	
	fun saveString(key: String, value: String) = sharedPreferences.edit().putString(key, value).apply()
	
}