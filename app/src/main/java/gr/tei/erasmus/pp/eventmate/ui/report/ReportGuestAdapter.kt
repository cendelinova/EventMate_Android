package gr.tei.erasmus.pp.eventmate.ui.report

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.ImageHelper
import kotlinx.android.synthetic.main.report_guest_item.view.*
import timber.log.Timber

class ReportGuestAdapter(
	private val context: Context,
	private val reportListener: ReportListener?,
	private var users: MutableList<User>
) : RecyclerView.Adapter<ReportGuestAdapter.ReportGuestViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		ReportGuestViewHolder(LayoutInflater.from(context).inflate(R.layout.report_guest_item, parent, false))
	
	override fun getItemCount() = users.size
	
	override fun onBindViewHolder(holder: ReportGuestViewHolder, position: Int) {
		Timber.v("onBindViewHolder() called with: holder = [$holder], position = [$position]")
		displayReportEntry(holder, users[holder.adapterPosition])
	}
	
	private fun displayReportEntry(viewHolder: ReportGuestViewHolder, user: User) {
		with(viewHolder.itemView) {
			user_name.text = user.userName
			user.photo?.let {
				user_photo.setImageBitmap(ImageHelper.getImageFromString(it))
			}
			setOnClickListener {
				checkbox.isChecked = !checkbox.isChecked
			}
			checkbox.isChecked = user.checked
			checkbox.setOnCheckedChangeListener { _, isChecked ->
				user.checked = isChecked
				reportListener?.onReportGuestPick(user.id, isChecked)
			}
		}
		
	}
	
	inner class ReportGuestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	interface ReportListener {
		fun onReportGuestPick(userId: Long?, isChecked: Boolean)
	}
}