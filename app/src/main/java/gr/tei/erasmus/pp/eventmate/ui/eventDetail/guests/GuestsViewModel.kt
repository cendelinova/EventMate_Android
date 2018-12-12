package gr.tei.erasmus.pp.eventmate.ui.eventDetail.guests

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.ui.base.BaseViewModel
import gr.tei.erasmus.pp.eventmate.ui.base.State

class GuestsViewModel: BaseViewModel() {
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	private var allUsers = mutableListOf<Task>()
	
	fun getGuests() {
	
	}
	
	data class UserListState(val users: MutableList<User>) : State() {
		companion object {
			fun from(list: MutableList<User>): UserListState {
				return if (list.isEmpty()) error("event list should not be empty")
				else {
					UserListState(list)
				}
			}
		}
	}
	
}
