package gr.tei.erasmus.pp.eventmate.ui.eventDetail.guests


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.showError
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.toggleProgress
import gr.tei.erasmus.pp.eventmate.ui.base.BaseFragment
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import gr.tei.erasmus.pp.eventmate.ui.userProfile.UserProfileActivity
import kotlinx.android.synthetic.main.fragment_guests.*
import timber.log.Timber

class GuestsFragment : BaseFragment() {
	
	private lateinit var guestAdapter: GuestAdapter
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(UserViewModel::class.java) }
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_guests, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeRecyclerView()
		observeViewModel()
//		viewModel.getGuests()
	}
	
	/**
	 * We have blacklist items obtained, initialize recyclerView and display them.
	 */
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		guestAdapter = GuestAdapter(
			context!!,
			onUserClick,
			mutableListOf()
		)
		
		with(guest_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(guest_empty_view)
			layoutManager = GridLayoutManager(context!!, 3)
			adapter = guestAdapter
		}
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeUserProgressState)
		}
	}
	
	
	private val onUserClick = object :
		GuestAdapter.GuestListener {
		override fun onUserClick(user: User) {
			startActivity(Intent(this@GuestsFragment.activity, UserProfileActivity::class.java))
		}
	}
	
	private val observeUserProgressState = Observer<State> { state ->
		when (state) {
			is LoadingState -> toggleProgress(progress, true)
			is ErrorState -> showError(state.error, progress, guests_fragment)
			is UserViewModel.UserListState -> {
				toggleProgress(progress, false)
				guestAdapter.updateUserList(state.users)
			}
		}
	}
	
}
