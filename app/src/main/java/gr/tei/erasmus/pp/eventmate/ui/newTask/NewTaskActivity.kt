package gr.tei.erasmus.pp.eventmate.ui.newTask

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.google.android.material.textfield.TextInputLayout
import android.view.View
import android.widget.Toast
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.squareup.picasso.Picasso
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.EVENT_ID
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.helpers.TextInputLayoutHelper
import gr.tei.erasmus.pp.eventmate.ui.base.*
import gr.tei.erasmus.pp.eventmate.ui.eventDetail.EventDetailActivity
import gr.tei.erasmus.pp.eventmate.ui.signup.TextInputLayoutAdapter
import kotlinx.android.synthetic.main.activity_new_task.*
import timber.log.Timber

class NewTaskActivity : BaseActivity(), Validator.ValidationListener, IPickResult {
	
	@NotEmpty(messageResId = R.string.error_required_field)
	private lateinit var taskName: TextInputLayout
	
	@NotEmpty(messageResId = R.string.error_required_field)
	private lateinit var points: TextInputLayout
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(NewTaskViewModel::class.java) }
	
	private var listOfInputs = mutableListOf<TextInputLayout>()
	
	private var eventId: Long? = null
	
	private val validator: Validator by lazy {
		Validator(this).also {
			it.setValidationListener(this@NewTaskActivity)
			it.registerAdapter(TextInputLayout::class.java, TextInputLayoutAdapter())
		}
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_new_task)
		setupToolbar(toolbar)
		observeViewModel()
		setupChoosingPhotoDialog()
		initInputs()
		handleSaveBtn()
		eventId = intent.getLongExtra(EVENT_ID, 0)
	}
	
	override fun onPickResult(pickResult: PickResult?) {
		pickResult?.let {
			Picasso.get().load(pickResult.uri).into(task_photo)
		}
	}
	
	private fun setupChoosingPhotoDialog() {
		task_photo.setOnClickListener {
			PickImageDialog.build(PickSetup().apply {
				setTitle(R.string.choose_photo)
			}).show(this)
		}
	}
	
	private fun handleSaveBtn() {
		btn_save_task.setOnClickListener {
			validator.validate()
		}
	}
	
	override fun onValidationFailed(errors: MutableList<ValidationError>?) {
		TextInputLayoutHelper.clearInputs(listOfInputs)
		errors?.run {
			for (error in errors) {
				val view = error.view
				val message = error.getCollatedErrorMessage(this@NewTaskActivity)
				
				if (view is TextInputLayout) {
					view.error = message
					view.isErrorEnabled = true
				}
			}
		}
	}
	
	override fun onValidationSucceeded() {
		TextInputLayoutHelper.clearInputs(listOfInputs)
		val name = TextInputLayoutHelper.collectValueFromInput(input_task_name)
		val points = TextInputLayoutHelper.collectValueFromInput(input_points).toInt()
		val description = TextInputLayoutHelper.collectValueFromInput(input_description)
		val place = TextInputLayoutHelper.collectValueFromInput(input_place)
		
		eventId?.let {
			viewModel.createTask(
				Task(
					null,
					eventId!!,
					name,
					points,
					description,
					place, null
				)
			)
		}
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeEventProgressState)
		}
	}
	
	private fun initInputs() {
		taskName = input_task_name
		points = input_points
		
		// for clearing purposes
		listOfInputs = mutableListOf(taskName, points)
		TextInputLayoutHelper.setRequiredMark(listOfInputs)
	}
	
	private fun toggleProgress(visibility: Boolean) {
		progress.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
	}
	
	private fun showError(error: Throwable) {
		Timber.e("Error $error while fetching tasks")
		toggleProgress(false)
//		Snackbar.make(
//			new_event,
//			getString(R.string.error_create_event),
//			Snackbar.LENGTH_INDEFINITE
//		).show()
//			.setAction(R.string.retry) {
//				startActivity(EventsFragment>().clearTop().clearTask().newTask())
//			}
//			.show()
	}
	
	
	private val observeEventProgressState = Observer<State> { state ->
		when (state) {
			is LoadingState -> toggleProgress(true)
			is ErrorState -> showError(state.error)
			is FinishedState -> {
				toggleProgress(false)
				Toast.makeText(this, R.string.success_task_created, Toast.LENGTH_LONG).show()
				startActivity(Intent(this, EventDetailActivity::class.java).apply {
					putExtra(EVENT_ID, eventId)
				})
			}
		}
	}
	
	
}
