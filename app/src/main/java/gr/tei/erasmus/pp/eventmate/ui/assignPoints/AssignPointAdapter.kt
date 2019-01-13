package gr.tei.erasmus.pp.eventmate.ui.assignPoints

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.User
import kotlinx.android.synthetic.main.assign_point_item.view.*
import timber.log.Timber


class AssignPointAdapter(
	val context: Context,
	private val maximumPoints: Int,
	private val assignPointListener: AssignPointListener,
	private var assignees: MutableList<User>
) :
	RecyclerView.Adapter<AssignPointAdapter.AssignPointViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignPointViewHolder {
		Timber.d("onCreateViewHolder() called with parent = [$parent], viewType = [$viewType]")
		
		return AssignPointViewHolder(LayoutInflater.from(context).inflate(R.layout.assign_point_item, parent, false))
	}
	
	override fun getItemCount() = assignees.size
	
	override fun onBindViewHolder(holder: AssignPointViewHolder, position: Int) {
		displayAssigneeEntry(holder, assignees[holder.adapterPosition])
	}
	
	private fun displayAssigneeEntry(holder: AssignPointViewHolder, user: User) {
		with(holder.itemView) {
			user_name.text = user.userName
			seek_bar.max = maximumPoints.toFloat()
			seek_bar.onSeekChangeListener = object : OnSeekChangeListener {
				override fun onSeeking(seekParams: SeekParams?) {
				}
				
				override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {
				}
				
				override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {
					user.id?.let { assignPointListener.onSetPoint(it, seekBar.progress) }
				}
			}
		}
	}
	
	fun updateList(newList: MutableList<User>) {
		assignees = newList
		notifyDataSetChanged()
	}
	
	inner class AssignPointViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	interface AssignPointListener {
		fun onSetPoint(userId: Long, points: Int)
	}
}

