package gr.tei.erasmus.pp.eventmate.ui.newTask

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputLayout
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
import gr.tei.erasmus.pp.eventmate.data.model.TaskRequest
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.DialogHelper
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import gr.tei.erasmus.pp.eventmate.helpers.TextHelper
import gr.tei.erasmus.pp.eventmate.ui.base.*
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.EventDetailActivity
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserViewModel
import gr.tei.erasmus.pp.eventmate.ui.report.ReportGuestAdapter
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
	
	private var users: MutableList<User> = mutableListOf()
	
	private var assignees: MutableList<User> = mutableListOf()
	
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
		eventId = intent.getLongExtra(EVENT_ID, 0)
		
		viewModel.getEventGuests(eventId!!)
		setupChoosingPhotoDialog()
		setupPickAssigneesDialog()
		initInputs()
		handleSaveBtn()
	}
	
	override fun onPickResult(pickResult: PickResult?) {
		pickResult?.let {
			Picasso.get().load(pickResult.uri).into(task_photo)
		}
	}
	
	private fun setupPickAssigneesDialog() {
		btn_choose_assignees.setOnClickListener {
			val pickUserListener = object : ReportGuestAdapter.ReportListener {
				override fun onReportGuestPick(user: User, isChecked: Boolean) {
					if (isChecked) {
						assignees.add(user)
					} else {
						assignees.remove(user)
					}
				}
			}
			
			val userAdapter = ReportGuestAdapter(this, pickUserListener, users)
			DialogHelper.showDialogWithAdapter(
				this, userAdapter,
				layoutInflater.inflate(R.layout.report_pick_dialog, null),
				getString(R.string.mgs_invite_guests), confirmListener, TextHelper.getQueryTextListener(userAdapter)
			)
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
		TextHelper.clearInputs(listOfInputs)
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
		TextHelper.clearInputs(listOfInputs)
		val name = TextHelper.collectValueFromInput(input_task_name)
		val points = TextHelper.collectValueFromInput(input_points).toInt()
		val description = TextHelper.collectValueFromInput(input_description)
		val place = TextHelper.collectValueFromInput(input_place)
//		val timeLimit =
//			if (TextHelper.collectValueFromInput(input_time).isEmpty()) null else TextHelper.collectValueFromInput(
//				input_time
//			).toInt()
		
		val photo = FileHelper.encodeImage(FileHelper.convertImageViewToBitmap(task_photo))
		
		eventId?.let {
			viewModel.createTask(
				TaskRequest(
					eventId!!,
					name,
					place,
					description,
					points,
					null, photo, assignees
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
		TextHelper.setRequiredMark(listOfInputs)
	}
	
	private fun toggleProgress(visibility: Boolean) {
		progress.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
	}
	
	private fun showError(error: Throwable) {
		Timber.e("Error $error while fetching tasks")
		toggleProgress(false)
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
			is UserViewModel.UserListState -> {
				toggleProgress(false)
				users = state.users
			}
		}
	}
	
	private val confirmListener = View.OnClickListener {
		chip_group.removeAllViews()
		TextHelper.createContactChips(assignees, chip_group, closeUserChipListener)
	}
	
	private val closeUserChipListener = View.OnClickListener {
		val userToDeleted = assignees.first { u -> u.userName == (it as Chip).text }
		assignees.remove(userToDeleted)
		users.first { u -> u == userToDeleted }.checked = false
		chip_group.removeView(it)
	}
}
