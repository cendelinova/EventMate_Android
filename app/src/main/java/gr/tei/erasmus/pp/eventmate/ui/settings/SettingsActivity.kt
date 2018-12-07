package gr.tei.erasmus.pp.eventmate.ui.settings

import android.os.Bundle
import android.support.v7.preference.*
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
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
					
					if (preference is PreferenceCategory) setZeroPaddingToLayoutChildren(holder.itemView)
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
		
	}
	
}
