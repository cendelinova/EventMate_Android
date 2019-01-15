package gr.tei.erasmus.pp.eventmate.ui.userProfile

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_ID
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import gr.tei.erasmus.pp.eventmate.helpers.GameRankHelper
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.showError
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.toggleProgress
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserViewModel
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.user_profile.*

class UserProfileActivity : BaseActivity() {
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(UserViewModel::class.java) }
	private var userId: Long? = null
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_user_profile)
		
		setupToolbar(toolbar)
		userId = intent.getLongExtra(USER_ID, 0)
		
		observeViewModel()
		
		userId?.let {
			viewModel.getUser(userId!!)
		}
		
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeUserProgressState)
		}
	}
	
	private fun setupLayout(user: User) {
		with(user) {
			tv_user_name.text = userName
			tv_user_email.text = email
			tv_count_earned_points.text = score.toString()
			tv_count_attended_events.text = attendedEvents.toString()
			tv_count_organized_events.text = organizedEvents.toString()
			
			photo?.let {
				profile_image.setImageBitmap(FileHelper.decodeImage(it))
			}
			GameRankHelper.showCorrectGameRankPhoto(ic_rank, score)
		}
	}
	
	private val observeUserProgressState = Observer<State> { state ->
		when (state) {
			is LoadingState -> toggleProgress(progress, true)
			is ErrorState -> showError(state.error, progress, user_profile)
			is UserViewModel.UserListState -> {
				toggleProgress(progress, false)
				setupLayout(state.users[0])
			}
		}
	}
	
	
}
