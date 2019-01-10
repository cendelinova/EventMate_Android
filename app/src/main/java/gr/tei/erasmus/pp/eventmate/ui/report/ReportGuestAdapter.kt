package gr.tei.erasmus.pp.eventmate.ui.report

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import gr.tei.erasmus.pp.eventmate.ui.base.AbstractFilterAdapter
import kotlinx.android.synthetic.main.report_guest_item.view.*
import timber.log.Timber

class ReportGuestAdapter(
	private val context: Context,
	private val reportListener: ReportListener?,
	private var users: MutableList<User>
) : AbstractFilterAdapter() {
	
	private var filteredUsers = users
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		ReportGuestViewHolder(LayoutInflater.from(context).inflate(R.layout.report_guest_item, parent, false))
	
	override fun getItemCount() = filteredUsers.size
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		Timber.v("onBindViewHolder() called with: holder = [$holder], position = [$position]")
		displayReportEntry(holder, filteredUsers[holder.adapterPosition])
	}
	
	override fun getFilter() = GuestFilter()
	
	private fun displayReportEntry(viewHolder: RecyclerView.ViewHolder, user: User) {
		with(viewHolder.itemView) {
			user_name.text = user.userName
			user.photo?.let {
				user_photo.setImageBitmap(FileHelper.decodeImage(it))
			}
			setOnClickListener {
				checkbox.isChecked = !checkbox.isChecked
			}
			checkbox.isChecked = user.checked
			checkbox.setOnCheckedChangeListener { _, isChecked ->
				user.checked = isChecked
				reportListener?.onReportGuestPick(user, isChecked)
			}
		}
		
	}
	
	inner class GuestFilter : Filter() {
		override fun performFiltering(constraint: CharSequence): Filter.FilterResults {
			filteredUsers = if (constraint.isEmpty()) {
				users
			} else {
				users.filter { user -> user.userName.contains(constraint.toString().toLowerCase()) }
					.toMutableList()
			}
			
			return Filter.FilterResults().apply {
				values = filteredUsers
			}
		}
		
		override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
			filteredUsers = results.values as MutableList<User>
			notifyDataSetChanged()
		}
		
	}
	
	inner class ReportGuestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	interface ReportListener {
		fun onReportGuestPick(user: User, isChecked: Boolean)
	}
}