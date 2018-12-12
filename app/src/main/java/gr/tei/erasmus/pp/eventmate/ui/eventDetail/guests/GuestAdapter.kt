package gr.tei.erasmus.pp.eventmate.ui.eventDetail.guests

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.User
import kotlinx.android.synthetic.main.guest_item.view.*

class GuestAdapter(
	private val context: Context,
	private val guestListener: GuestListener,
	private var guests: MutableList<User>
) : RecyclerView.Adapter<GuestAdapter.GuestViewHolder>() {
	override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GuestViewHolder {
		return GuestViewHolder(LayoutInflater.from(context).inflate(R.layout.guest_item, viewGroup, false))
	}
	
	override fun getItemCount() = guests.size
	
	override fun onBindViewHolder(viewHolder: GuestViewHolder, position: Int) {
		displayGuestEntry(viewHolder, guests[viewHolder.adapterPosition])
	}
	
	private fun displayGuestEntry(viewHolder: GuestViewHolder, user: User) {
		with(viewHolder.itemView) {
			guest_item.setOnClickListener { guestListener.onUserClick(user) }
			user_name.text = user.name
		}
	}
	
	fun updateUserList(guests: MutableList<User>) {
		this.guests = guests
		notifyDataSetChanged()
	}
	
	
	inner class GuestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	interface GuestListener {
		fun onUserClick(user: User)
	}
	
}