package gr.tei.erasmus.pp.eventmate.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_ID
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper
import gr.tei.erasmus.pp.eventmate.ui.base.BaseFragment
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserAdapter
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserViewModel
import gr.tei.erasmus.pp.eventmate.ui.gameRank.GameRankActivity
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivity
import gr.tei.erasmus.pp.eventmate.ui.settings.SettingsActivity
import gr.tei.erasmus.pp.eventmate.ui.userProfile.UserProfileActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.user_profile.*
import timber.log.Timber


class ProfileFragment : BaseFragment() {
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(UserViewModel::class.java) }
	private val sharedPreferenceHelper =  App.COMPONENTS.provideSharedPreferencesHelper()
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		Timber.v("onCreateView() called with: inflater = [$inflater], container = [$container], savedInstanceState = [$savedInstanceState]")
		setHasOptionsMenu(true)
		(activity as MainActivity).setCustomTitle("")
		
		return inflater.inflate(R.layout.fragment_profile, null)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		observeViewModel()
		profile_fragment.visibility = View.INVISIBLE
		viewModel.getMyProfile()
	}
	
	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		activity?.menuInflater?.run {
			inflate(R.menu.menu_fragment_profile, menu)
		}
		super.onCreateOptionsMenu(menu, inflater)
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.settings -> openActivity(SettingsActivity::class.java)
			R.id.game_rank -> openActivity(GameRankActivity::class.java)
			R.id.logout -> logOut()
		}
		
		return true
	}
	
	private fun <T : Any> openActivity(uri: Class<T>) = startActivity(Intent(this.activity, uri))
	
	private fun logOut() {
		sharedPreferenceHelper.remove(USER_ID)
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeUserProgressState)
		}
	}
	
	
	private val onUserClick = object :
		UserAdapter.GuestListener {
		override fun onUserClick(user: User) {
			startActivity(Intent(this@ProfileFragment.activity, UserProfileActivity::class.java))
		}
	}
	
	private val observeUserProgressState = Observer<State> { state ->
		when (state) {
			is LoadingState -> StateHelper.toggleProgress(progress, true)
			is ErrorState -> StateHelper.showError(state.error, progress, profile_fragment)
			is UserViewModel.UserListState -> {
				StateHelper.toggleProgress(progress, false)
				setupLayout(state.users[0])
			}
		}
	}
	
	private fun setupLayout(user: User) {
		profile_fragment.visibility = View.VISIBLE
		with(user) {
			tv_user_name.text = userName
			tv_user_email.text = email
			tv_count_earned_points.text = score.toString()
			tv_count_attended_events.text = attendedEvents.toString()
			tv_count_organized_events.text = organizedEvents.toString()
			photo?.let {
				profile_image.setImageBitmap(FileHelper.decodeImage(it))
			}
		}
	}
}