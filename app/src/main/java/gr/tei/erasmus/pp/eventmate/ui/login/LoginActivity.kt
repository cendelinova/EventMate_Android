package gr.tei.erasmus.pp.eventmate.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_ID
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.data.model.UserRequest
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper
import gr.tei.erasmus.pp.eventmate.helpers.TextHelper
import gr.tei.erasmus.pp.eventmate.ui.base.*
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserViewModel
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivity
import gr.tei.erasmus.pp.eventmate.ui.signup.SignupActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(UserViewModel::class.java) }
	private val sharedPreferenceHelper = App.COMPONENTS.provideSharedPreferencesHelper()
	
	private val userRepository = App.COMPONENTS.provideUserRepository()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)
		
		if (sharedPreferenceHelper.hasKey(USER_ID)) {
			startActivity(Intent(this, MainActivity::class.java))
		}
		
		observeViewModel()
		
		handleButtons()
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeUserProgressState)
		}
	}
	
	private val observeUserProgressState = Observer<State> { state ->
		when (state) {
			is LoadingState -> StateHelper.toggleProgress(progress, true)
			is ErrorState -> StateHelper.showError(state.error, progress, login)
			is FinishedState -> {
				StateHelper.toggleProgress(progress, false)
				Toast.makeText(this, getString(R.string.success_login), Toast.LENGTH_LONG).show()
				startActivity(Intent(this, MainActivity::class.java))
			}
		}
	}
	
	
	private fun handleButtons() {
		handleLoginButton()
		handleSignUp()
		handleFacebookLogin()
		handleGoogleLogin()
	}
	
	private fun handleLoginButton() {
		btn_login.setOnClickListener {
			val userEmail = TextHelper.collectValueFromInput(input_email)
			val password = TextHelper.collectValueFromInput(input_password)
			val user = UserRequest(password, userEmail)
			
			viewModel.login(user)
		}
	}
	
	private fun handleSignUp() {
		tv_no_account.setOnClickListener {
			startActivity(Intent(this, SignupActivity::class.java))
		}
	}
	
	private fun handleFacebookLogin() {
		btn_facebook.setOnClickListener {
			// todo api call
		}
	}
	
	private fun handleGoogleLogin() {
		btn_google.setOnClickListener {
			// todo api call
		}
	}
}
