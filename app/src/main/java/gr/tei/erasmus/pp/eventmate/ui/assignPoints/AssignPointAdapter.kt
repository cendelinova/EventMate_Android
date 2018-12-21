package gr.tei.erasmus.pp.eventmate.ui.assignPoints

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.User
import kotlinx.android.synthetic.main.assign_point_item.view.*
import timber.log.Timber

class AssignPointAdapter(val context: Context, val maximumPoints: Int, val assigness: MutableList<User>) :
	RecyclerView.Adapter<AssignPointAdapter.AssignPointViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignPointViewHolder {
		Timber.d("onCreateViewHolder() called with parent = [$parent], viewType = [$viewType]")
		
		return AssignPointViewHolder(LayoutInflater.from(context).inflate(R.layout.assign_point_item, parent, false))
	}
	
	override fun getItemCount() = assigness.size
	
	override fun onBindViewHolder(holder: AssignPointViewHolder, position: Int) {
		displayAssigneeEntry(holder, assigness[holder.adapterPosition])
	}
	
	private fun displayAssigneeEntry(holder: AssignPointViewHolder, user: User) {
		with(holder.itemView) {
			user_name.text = user.userName
			seek_bar.max = maximumPoints.toFloat()
			
		}
	}
	
	inner class AssignPointViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

