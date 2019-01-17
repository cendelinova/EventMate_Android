package gr.tei.erasmus.pp.eventmate.ui.submission

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import kotlinx.android.synthetic.main.assignee_item.view.*

class AssigneeAdapter(
	private val context: Context,
	private val guestListener: AssigneeListener,
	private var guests: MutableList<User>
) : RecyclerView.Adapter<AssigneeAdapter.AssigneeViewHolder>() {
	override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AssigneeViewHolder {
		return AssigneeViewHolder(LayoutInflater.from(context).inflate(R.layout.assignee_item, viewGroup, false))
	}
	
	override fun getItemCount() = guests.size
	
	override fun onBindViewHolder(viewHolder: AssigneeViewHolder, position: Int) {
		displayGuestEntry(viewHolder, guests[viewHolder.adapterPosition])
	}
	
	private fun displayGuestEntry(viewHolder: AssigneeViewHolder, user: User) {
		with(viewHolder.itemView) {
			assignee_item.setOnClickListener { guestListener.onUserClick(user) }
			user_photo.setImageBitmap(user.photo?.let { FileHelper.decodeImage(it) })
			user_name.text = user.userName
			file_indicator.visibility = if (user.hasSent) View.VISIBLE else View.GONE
			points_layout.visibility = if (user.achievedPoints == null) View.GONE else View.VISIBLE
			tv_points.text = user.achievedPoints.toString()
		}
	}
	
	fun updateUserList(guests: MutableList<User>) {
		this.guests = guests
		notifyDataSetChanged()
	}
	
	
	inner class AssigneeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	interface AssigneeListener {
		fun onUserClick(user: User)
	}
	
}