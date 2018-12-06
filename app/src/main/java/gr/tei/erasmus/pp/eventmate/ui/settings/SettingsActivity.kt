package gr.tei.erasmus.pp.eventmate.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.*
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		setContentView(R.layout.activity_settings)
		setupToolbar(toolbar, true)
		supportFragmentManager.beginTransaction().replace(R.id.settings_root, EventMatePreferenceFragment()).commit()
	}
	
	class EventMatePreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
		private var sharedPreferenceHelper = App.COMPONENTS.provideSharedPreferencesHelper()
		
		override fun onResume() {
			super.onResume()
			preferenceScreen.sharedPreferences
				.registerOnSharedPreferenceChangeListener(this)
		}
		
		override fun onPause() {
			super.onPause()
			preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
		}
		
		override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
			setPreferencesFromResource(R.xml.preferences, rootKey)
		}
		
		override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
			val preference = findPreference(key)
			if (preference is SwitchPreferenceCompat) preference.isChecked =
					sharedPreferenceHelper.loadBoolean(preference.key)
		}
		
		override fun onCreateAdapter(preferenceScreen: PreferenceScreen?): RecyclerView.Adapter<*> {
			return object : PreferenceGroupAdapter(preferenceScreen) {
				override fun onBindViewHolder(holder: PreferenceViewHolder, position: Int) {
					super.onBindViewHolder(holder, position)
					val preference = getItem(position)
					
					if (preference is PreferenceCategory)
						setZeroPaddingToLayoutChildren(holder.itemView)
					else {
						holder.itemView.findViewById<View?>(R.id.icon_frame)?.visibility =
								if (preference.icon == null) View.GONE else View.VISIBLE
						
					}
				}
			}
		}
		
		
		private fun setZeroPaddingToLayoutChildren(view: View) {
			if (view !is ViewGroup)
				return
			val childCount = view.childCount
			for (i in 0 until childCount) {
				setZeroPaddingToLayoutChildren(view.getChildAt(i))
				val padding = (resources.getDimension(R.dimen.spacing_small) / resources.displayMetrics.density).toInt()
				view.setPadding(0, 0, 0, 0)
			}
		}
		
		override fun onPreferenceTreeClick(preference: Preference?): Boolean {
			preference?.run {
				if (preference is SwitchPreferenceCompat) sharedPreferenceHelper.saveBoolean(
					preference.key,
					preference.isChecked
				)
				
			}
			
			return super.onPreferenceTreeClick(preference)
		}
	}
	
}
