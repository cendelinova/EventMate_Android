package gr.tei.erasmus.pp.eventmate.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.ui.common.RecyclerViewEmptyViewSupport
import gr.tei.erasmus.pp.eventmate.ui.report.ReportGuestAdapter


class CustomDialog : DialogFragment() {
	
	private lateinit var adapter: ReportGuestAdapter
	
	override fun setArguments(args: Bundle?) {
		super.setArguments(args)
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		adapter =  ReportGuestAdapter(
			activity as Context, null, mutableListOf(User("pepa"), User("jenda"), User("evik"))
		)
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		//inflate layout with recycler view
		val v = inflater.inflate(R.layout.report_guest_dialog, container, false)
		val emptyView = v.findViewById(R.id.guest_empty_view) as LinearLayout
		val recyclerView = v.findViewById(R.id.guest_recycler_view) as RecyclerViewEmptyViewSupport
		recyclerView.layoutManager = LinearLayoutManager(activity).apply { orientation = RecyclerView.VERTICAL }
		//setadapter
		recyclerView.adapter = adapter
		recyclerView.setEmptyView(emptyView)
		adapter.notifyDataSetChanged()
		//get your recycler view and populate it.
		return v
	}

	

//	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//
//		val customView = activity?.layoutInflater?.inflate(R.layout.report_guest_dialog, null)
//		val myAdapter = ReportGuestAdapter(
//			activity as Context, null, mutableListOf(User("pepa"), User("jenda"), User("evik"))
//		)
//		with(customView?.guest_recycler_view!!) {
//			setHasFixedSize(true)
//			setEmptyView(customView.guest_empty_view)
//			layoutManager = LinearLayoutManager(context).apply { orientation = RecyclerView.VERTICAL }
//			this.adapter = myAdapter
//		}
//
//		myAdapter.notifyDataSetChanged()
//
//		return AlertDialog.Builder(activity as Context).apply {
//			setView(customView)
//			setPositiveButton(R.string.btn_confirm, null)
//			setNegativeButton(
//				R.string.btn_cancel
//			) { dialog, _ -> dialog.dismiss() }
//		}.also {
//			it.create()
//		}.show()
//	}
}