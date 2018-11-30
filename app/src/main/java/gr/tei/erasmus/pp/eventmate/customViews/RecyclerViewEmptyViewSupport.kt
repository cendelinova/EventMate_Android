package gr.tei.erasmus.pp.eventmate.customViews

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView

class RecyclerViewEmptyViewSupport : FastScrollRecyclerView {
	
	/* Public attributes **************************************************************************/
	
	var loadAdOnEmptyViewListener: LoadAdOnEmptyViewListener? = null
	
	/* Private attributes *************************************************************************/
	
	private var emptyView: View? = null
	
	private val emptyObserver = object : RecyclerView.AdapterDataObserver() {
		override fun onChanged() = checkEmpty()
		
		override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = checkEmpty()
		
		override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) = checkEmpty()
		
		override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = checkEmpty()
		
		override fun onItemRangeChanged(positionStart: Int, itemCount: Int) = checkEmpty()
		
	}
	
	/* Constructors *******************************************************************************/
	
	constructor(context: Context) : super(context)
	
	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
	
	constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
	
	/* Public methods *****************************************************************************/
	
	override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
		super.setAdapter(adapter)
		adapter?.registerAdapterDataObserver(emptyObserver)
		emptyObserver.onChanged()
	}
	
	/**
	 * This view will be used as placeholder, when there are no data for recycler view.
	 */
	fun setEmptyView(emptyView: View?) {
		this.emptyView = emptyView
	}
	
	/* Private methods  ***************************************************************************/
	
	private fun checkEmpty() {
		if (adapter?.itemCount == 0) {
			emptyView?.visibility = View.VISIBLE
			visibility = View.GONE
			loadAdOnEmptyViewListener?.loadAd()
		} else {
			emptyView?.visibility = View.GONE
			visibility = View.VISIBLE
		}
	}
	
	/* Callbacks **********************************************************************************/
	
	interface LoadAdOnEmptyViewListener {
		fun loadAd()
	}
	
}