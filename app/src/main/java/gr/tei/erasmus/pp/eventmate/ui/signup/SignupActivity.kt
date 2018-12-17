package gr.tei.erasmus.pp.eventmate.ui.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.mobsandgeeks.saripaar.annotation.Password
import com.squareup.picasso.Picasso
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.ImageHelper
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper
import gr.tei.erasmus.pp.eventmate.helpers.TextInputLayoutHelper
import gr.tei.erasmus.pp.eventmate.ui.base.*
import gr.tei.erasmus.pp.eventmate.ui.eventDetail.guests.UserViewModel
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivity
import kotlinx.android.synthetic.main.activity_signup.*


class SignupActivity : BaseActivity(), Validator.ValidationListener, IPickResult {
	
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
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(UserViewModel::class.java) }
	
	private var listOfInputs: MutableList<TextInputLayout> = mutableListOf()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_signup)
		observeViewModel()
		setupToolbar(toolbar)
		initInputs()
		handleSignUpButton()
		setupChoosingPhotoDialog()
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
		val userName = TextInputLayoutHelper.collectValueFromInput(input_name)
		val email = TextInputLayoutHelper.collectValueFromInput(input_email)
		val password = TextInputLayoutHelper.collectValueFromInput(input_password)
		
//		val photo = ImageHelper.getStringImage(ImageHelper.convertImageViewToBitmap(profile_photo))
		viewModel.register(User(userName, password, email, null))
	}
	
	override fun onPickResult(pickResult: PickResult?) {
		pickResult?.let {
			Picasso.get().load(pickResult.uri).into(profile_photo)
		}
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeUserProgressState)
		}
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
	
	private fun setupChoosingPhotoDialog() {
//		profile_photo.setOnClickListener {
//			PickImageDialog.build(PickSetup().apply {
//				setTitle(R.string.choose_photo)
//			}).show(this)
//		}
	}
	
	private val observeUserProgressState = Observer<State> { state ->
		when (state) {
			is LoadingState -> StateHelper.toggleProgress(progress, true)
			is ErrorState -> StateHelper.showError(state.error, progress, sign_up_activity)
			is FinishedState -> {
				StateHelper.toggleProgress(progress, false)
				Toast.makeText(this, getString(R.string.success_sign_up), Toast.LENGTH_LONG).show()
				startActivity(Intent(this, MainActivity::class.java))
			}
		}
	}
}

