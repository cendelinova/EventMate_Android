package gr.tei.erasmus.pp.eventmate.ui.base

/**
 * Abstract State
 */
open class State

/**
 * Generic Loading State
 */
object LoadingState : State()


/**
 * Generic Finished State
 */
object FinishedState : State()


object DeletedState : State()


/**
 * Generic Error state
 * @param error - caught error
 */
data class ErrorState(val error: Throwable) : State()