package gr.tei.erasmus.pp.eventmate.ui.base

import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractFilterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
	abstract override fun getFilter(): Filter?
	
}