package gr.tei.erasmus.pp.eventmate.ui.settings

import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroupAdapter
import androidx.preference.PreferenceScreen
import androidx.preference.PreferenceViewHolder
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : BaseActivity() {
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		setContentView(R.layout.activity_settings)
		setupToolbar(toolbar, true)
		supportFragmentManager.beginTransaction().replace(R.id.settings_root, EventMatePreferenceFragment()).commit()
	}
	
	class EventMatePreferenceFragment : PreferenceFragmentCompat() {
		override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
			setPreferencesFromResource(R.xml.preferences, rootKey)
		}
		
		
		override fun onCreateAdapter(preferenceScreen: PreferenceScreen?): RecyclerView.Adapter<*> {
			return object : PreferenceGroupAdapter(preferenceScreen) {
				override fun onBindViewHolder(holder: PreferenceViewHolder, position: Int) {
					super.onBindViewHolder(holder, position)
					val preference = getItem(position)
					
					holder.itemView.findViewById<View?>(R.id.icon_frame)?.visibility = View.GONE
					
				}
			}
		}
		
		
	}
	
}
