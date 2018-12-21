package gr.tei.erasmus.pp.eventmate.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import kotlinx.android.synthetic.main.activity_login.*
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.ui.assignPoints.AssignPointViewModel
import gr.tei.erasmus.pp.eventmate.ui.base.NoToolbarActivity
import gr.tei.erasmus.pp.eventmate.ui.eventDetail.guests.UserViewModel
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivity
import gr.tei.erasmus.pp.eventmate.ui.signup.SignupActivity

class LoginActivity : NoToolbarActivity() {
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(UserViewModel::class.java) }
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)
		
		handleButtons()
	}
	
	private fun handleButtons() {
		handleLoginButton()
		handleSignUp()
		handleFacebookLogin()
		handleForgottenPassword()
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
	
	private fun handleForgottenPassword() {
		tv_forgotten_password.setOnClickListener {
			// todo api call
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
