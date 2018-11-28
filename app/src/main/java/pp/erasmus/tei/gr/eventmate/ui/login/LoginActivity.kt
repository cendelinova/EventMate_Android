package pp.erasmus.tei.gr.eventmate.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import pp.erasmus.tei.gr.eventmate.R
import pp.erasmus.tei.gr.eventmate.ui.base.BaseActivity
import pp.erasmus.tei.gr.eventmate.ui.base.NoToolbarActivity
import pp.erasmus.tei.gr.eventmate.ui.mainActivity.MainActivity
import pp.erasmus.tei.gr.eventmate.ui.signup.SignupActivity

class LoginActivity : NoToolbarActivity() {
	
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
			startActivity(Intent(this@LoginActivity, MainActivity::class.java))
		}
	}
	
	private fun handleSignUp() {
		tv_no_account.setOnClickListener {
			startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
			
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
