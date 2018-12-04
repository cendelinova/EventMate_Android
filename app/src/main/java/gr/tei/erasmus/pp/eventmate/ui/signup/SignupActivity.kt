package gr.tei.erasmus.pp.eventmate.ui.signup

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.widget.Toast
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.mobsandgeeks.saripaar.annotation.Password
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.helpers.TextInputLayoutHelper
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_signup.*


class SignupActivity : BaseActivity(), Validator.ValidationListener {
	
	@NotEmpty(messageResId = R.string.error_required_field)
	@Email(messageResId = R.string.error_invalid_email)
	private lateinit var email: TextInputLayout
	
	@NotEmpty(messageResId = R.string.error_required_field)
	private lateinit var name: TextInputLayout
	
	@NotEmpty(messageResId = R.string.error_required_field)
	@Password(min = 6, scheme = Password.Scheme.ANY, messageResId = R.string.error_invalid_password)
	private lateinit var password: TextInputLayout
	
	@NotEmpty(messageResId = R.string.error_required_field)
	@ConfirmPassword(messageResId = R.string.error_password_not_equal)
	private lateinit var repeatPassword: TextInputLayout
	
	private val validator: Validator by lazy {
		Validator(this@SignupActivity).also {
			it.setValidationListener(this@SignupActivity)
			it.registerAdapter(TextInputLayout::class.java, TextInputLayoutAdapter())
		}
	}
	
	private var listOfInputs: MutableList<TextInputLayout> = mutableListOf()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_signup)
		
		setupToolbar(toolbar, true)
		initInputs()
		handleSignUpButton()
	}
	
	override fun onValidationFailed(errors: MutableList<ValidationError>?) {
		TextInputLayoutHelper.clearInputs(listOfInputs)
		errors?.run {
			for (error in errors) {
				val view = error.view
				val message = error.getCollatedErrorMessage(this@SignupActivity)
				
				// Display error messages ;)
				if (view is TextInputLayout) {
					view.error = message
				}
			}
		}
	}
	
	override fun onValidationSucceeded() {
		Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();
		
	}
	
	private fun initInputs() {
		email = input_email
		name = input_name
		password = input_password
		repeatPassword = input_repeat_password
		
		listOfInputs = mutableListOf(email, name, password, repeatPassword)
	}
	
	
	private fun handleSignUpButton() {
		btn_signup.setOnClickListener {
			validator.validate()
		}
	}
}
