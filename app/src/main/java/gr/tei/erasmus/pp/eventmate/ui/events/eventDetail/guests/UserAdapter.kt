package gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import kotlinx.android.synthetic.main.guest_item.view.*

class UserAdapter(
	private val context: Context,
	private val guestListener: GuestListener,
	private var guests: MutableList<User>
) : RecyclerView.Adapter<UserAdapter.GuestViewHolder>() {
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
			user_photo.setImageBitmap(user.photo?.let { FileHelper.decodeImage(it) })
			user_name.text = user.userName
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