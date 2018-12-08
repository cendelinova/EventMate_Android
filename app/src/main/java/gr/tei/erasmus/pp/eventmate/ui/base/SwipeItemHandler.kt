package gr.tei.erasmus.pp.eventmate.ui.base

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

abstract class SwipeItemHandler(dragDirs: Int, swipeDirs: Int, private val listener: RecyclerItemTouchHelperListener) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
	
	/**
	 * Returns the swipe directions for the provided ViewHolder.
	 * Default implementation returns the swipe directions that was set via constructor or
	 * {@link #setDefaultSwipeDirs(int)}.
	 *
	 * @param recyclerView The RecyclerView to which the ItemTouchHelper is attached to.
	 * @param viewHolder   The RecyclerView for which the swipe direction is queried.
	 * @return A binary OR of direction flags.
	 */
	abstract override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int
	
	/**
	 * Called when the ViewHolder swiped or dragged by the ItemTouchHelper is changed.
	 *
	 * @param viewHolder  The new CallLogPageViewHolder that is being swiped or dragged. Might be null if it is cleared.
	 * @param actionState One of ACTION_STATE_IDLE, ACTION_STATE_SWIPE or ACTION_STATE_DRAG.
	 */
	abstract override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int)
	
	/**
	 * Called by ItemTouchHelper on RecyclerView's onDraw callback.
	 *
	 * @param canvas            The canvas which RecyclerView is drawing its children
	 * @param recyclerView      The RecyclerView to which ItemTouchHelper is attached to
	 * @param viewHolder        The CallLogPageViewHolder which is being interacted by the User or it was interacted and simply animating to its original position
	 * @param dX                The amount of horizontal displacement caused by user's action
	 * @param dY                The amount of vertical displacement caused by user's action
	 * @param actionState       The type of interaction on the View. Is either ACTION_STATE_DRAG or ACTION_STATE_SWIPE.
	 * @param isCurrentlyActive True if this view is currently being controlled by the user or false it is simply animating back to its original state.
	 */
	abstract override fun onChildDraw(canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean)
	
	/**
	 * Called by ItemTouchHelper on RecyclerView's onDraw callback.
	 *
	 * @param canvas            The canvas which RecyclerView is drawing its children
	 * @param recyclerView      The RecyclerView to which ItemTouchHelper is attached to
	 * @param viewHolder        The CallLogPageViewHolder which is being interacted by the User or it was interacted and simply animating to its original position
	 * @param dX                The amount of horizontal displacement caused by user's action
	 * @param dY                The amount of vertical displacement caused by user's action
	 * @param actionState       The type of interaction on the View. Is either ACTION_STATE_DRAG or ACTION_STATE_SWIPE.
	 * @param isCurrentlyActive True if this view is currently being controlled by the user or false it is simply animating back to its original state.
	 */
	abstract override fun onChildDrawOver(canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean)
	
	
	/**
	 * Called by the ItemTouchHelper when the user interaction with an element is over and it also completed its animation.
	 *
	 * @param recyclerView The RecyclerView which is controlled by the ItemTouchHelper.
	 * @param viewHolder   The View that was interacted by the user.
	 */
	abstract override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder)
	
	/**
	 * Called when ItemTouchHelper wants to move the dragged item from its old position to the new position.
	 *
	 * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to.
	 * @param viewHolder   The CallLogPageViewHolder which is being dragged by the user.
	 * @param target       The CallLogPageViewHolder over which the currently active item is being dragged.
	 *
	 * @return True if the viewHolder has been moved to the adapter position of target.
	 */
	override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true
	
	/**
	 * Called when a CallLogPageViewHolder is swiped by the user.
	 *
	 * @param viewHolder The CallLogPageViewHolder which has been swiped by the user.
	 * @param direction  The direction to which the CallLogPageViewHolder is swiped. It is one of UP, DOWN, LEFT or RIGHT.
	 * 					 If your getMovementFlags(RecyclerView, CallLogPageViewHolder) method returned relative flags
	 * 					 instead of LEFT / RIGHT; `direction` will be relative as well. (START or END).
	 */
	override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
		viewHolder.let { listener.onSwiped(it, direction, it.adapterPosition) }
	}
	
	
	/* Interfaces *********************************************************************************/
	interface RecyclerItemTouchHelperListener {
		fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
	}
}