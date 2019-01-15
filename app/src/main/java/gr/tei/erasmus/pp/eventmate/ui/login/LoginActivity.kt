package gr.tei.erasmus.pp.eventmate.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.USER_ID
import gr.tei.erasmus.pp.eventmate.ui.base.NoToolbarActivity
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserViewModel
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivity
import gr.tei.erasmus.pp.eventmate.ui.signup.SignupActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : NoToolbarActivity() {
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(UserViewModel::class.java) }
	private val sharedPreferenceHelper = App.COMPONENTS.provideSharedPreferencesHelper()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)
		sharedPreferenceHelper.remove(USER_ID)
		if (sharedPreferenceHelper.hasKey(USER_ID)) {
			startActivity(Intent(this, MainActivity::class.java))
		}
		
		handleButtons()
	}
	
	
	private fun handleButtons() {
		handleLoginButton()
		handleSignUp()
		handleFacebookLogin()
		handleGoogleLogin()
	}
	
	private fun handleLoginButton() {
		btn_login.setOnClickListener {
			//todo validate + api call
			startActivity(Intent(this, MainActivity::class.java))
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
