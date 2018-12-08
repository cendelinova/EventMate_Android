package gr.tei.erasmus.pp.eventmate.ui.events

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import gr.tei.erasmus.pp.eventmate.ui.base.SwipeItemHandler
import kotlinx.android.synthetic.main.event_item.view.*

class EventSwipeCallback(dragDirs: Int, private val swipeDirs: Int, listener: SwipeItemHandler.RecyclerItemTouchHelperListener) : SwipeItemHandler(dragDirs, swipeDirs, listener) {
	
	override fun onChildDrawOver(canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
		ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(canvas,
			recyclerView,
			getForegroundView(viewHolder),
			dX, dY,
			actionState,
			isCurrentlyActive)
	}
	
	override fun onChildDraw(canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
		ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(canvas, recyclerView, getForegroundView(viewHolder),
			dX, dY,
			actionState, isCurrentlyActive)
	}
	
	override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
		ItemTouchHelper.Callback.getDefaultUIUtil().clearView(getForegroundView(viewHolder))
	}
	
	override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
		return swipeDirs
	}
	
	override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?,
	                               actionState: Int) {
		if (viewHolder != null) {
			ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(getForegroundView(viewHolder))
		}
	}
	
	/* Private methods *****************************************************************************/
	
	private fun getForegroundView(viewHolder: RecyclerView.ViewHolder?) =
		(viewHolder as EventAdapter.EventViewHolder).itemView.view_foreground
}