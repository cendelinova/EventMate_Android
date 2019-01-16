package gr.tei.erasmus.pp.eventmate.ui.events.newEvent

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.util.Rfc822Tokenizer
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.ex.chips.BaseRecipientAdapter
import com.android.ex.chips.recipientchip.DrawableRecipientChip
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
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.EVENT_ID
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.EventRequest
import gr.tei.erasmus.pp.eventmate.data.model.Invitation
import gr.tei.erasmus.pp.eventmate.data.model.Invitation.Companion.buildInvitation
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.DateTimeHelper
import gr.tei.erasmus.pp.eventmate.helpers.DialogHelper
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.toggleProgress
import gr.tei.erasmus.pp.eventmate.helpers.TextHelper
import gr.tei.erasmus.pp.eventmate.helpers.validation.FutureDate
import gr.tei.erasmus.pp.eventmate.helpers.validation.FutureTimeRule
import gr.tei.erasmus.pp.eventmate.ui.base.*
import gr.tei.erasmus.pp.eventmate.ui.events.EventsViewModel
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserViewModel
import gr.tei.erasmus.pp.eventmate.ui.mainActivity.MainActivity
import gr.tei.erasmus.pp.eventmate.ui.report.ReportGuestAdapter
import gr.tei.erasmus.pp.eventmate.ui.report.ReportGuestAdapter.ReportListener
import gr.tei.erasmus.pp.eventmate.ui.signup.TextInputLayoutAdapter
import kotlinx.android.synthetic.main.activity_new_event.*
import kotlinx.android.synthetic.main.guest_invitation_dialog.view.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class NewEventActivity : BaseActivity(), IPickResult, Validator.ValidationListener {
	
	@NotEmpty(messageResId = R.string.error_required_field)
	private lateinit var eventName: TextInputLayout
	
	@FutureDate
	private lateinit var date: TextInputLayout
	
	private lateinit var time: TextInputLayout
	
	private var isEdit: Boolean = false
	
	private var eventId: Long? = null
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(EventsViewModel::class.java) }
	
	private val calendar by lazy { Calendar.getInstance() }
	
	private var listOfInputs = mutableListOf<TextInputLayout>()
	
	private var users: MutableList<User> = mutableListOf()
	
	private var emails: MutableList<String> = mutableListOf()
	
	private var invitedExistingUsers: MutableList<User> = mutableListOf()
	
	private val eventRequest by lazy { EventRequest() }
	
	private var savedRecipients: Array<out DrawableRecipientChip>? = null
	
	private val userRoleHelper = App.COMPONENTS.provideUserRoleHelper()
	
	private val validator: Validator by lazy {
		Validator(this@NewEventActivity).also {
			it.setValidationListener(this@NewEventActivity)
			it.registerAdapter(TextInputLayout::class.java, TextInputLayoutAdapter())
		}
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_new_event)
		
		if (intent.hasExtra(EVENT_ID)) {
			eventId = intent.getLongExtra(EVENT_ID, 0)
			viewModel.getEvent(eventId!!)
			isEdit = true
			eventRequest.id = eventId
			btn_create_event.text = getString(R.string.btn_update_event)
		}
		
		setupToolbar(toolbar, true)
		setupPickers()
		initInputs()
		handleCreateNewEvent()
		Validator.registerAnnotation(FutureDate::class.java)
		observeViewModel()
		
		viewModel.getAppUsers()
	}
	
	private var photoPicked: Boolean = false
	
	override fun onPickResult(pickResult: PickResult?) {
		pickResult?.let {
			photoPicked = true
			Picasso.get().load(pickResult.uri).into(event_photo)
		}
	}
	
	override fun onValidationFailed(errors: MutableList<ValidationError>?) {
		TextHelper.clearInputs(listOfInputs)
		errors?.run {
			for (error in errors) {
				val view = error.view
				val message = error.getCollatedErrorMessage(this@NewEventActivity)
				
				if (view is TextInputLayout) {
					view.error = message
					view.isErrorEnabled = true
				}
			}
		}
		
	}
	
	override fun onValidationSucceeded() {
		TextHelper.clearInputs(listOfInputs)
		val name = TextHelper.collectValueFromInput(input_event_name)
		val date =
			DateTimeHelper.parseDateTimeFromString(
				TextHelper.collectValueFromInput(input_date),
				DateTimeHelper.DATE_FORMAT
			)
		val time =
			DateTimeHelper.parseDateTimeFromString(
				TextHelper.collectValueFromInput(input_time),
				DateTimeHelper.TIME_FORMAT
			)
		
		val place = TextHelper.collectValueFromInput(input_place)
		
		var photo: String? = null
		if (photoPicked) {
			photo = FileHelper.encodeImage(FileHelper.convertImageViewToBitmap(event_photo))
		}
		
		
		val dateWithTime = date?.withTime(time?.toLocalTime())
		with(eventRequest) {
			this.name = name
			this.date = DateTimeHelper.convertDateTimeToString(dateWithTime, DateTimeHelper.FULL_DATE_TIME_FORMAT)
			this.place = place
			this.photo = photo
		}
		
		if (!isEdit) viewModel.createEvent(eventRequest) else viewModel.updateEvent(eventRequest)
		
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeEventProgressState)
		}
	}
	
	private fun setupPickers() {
		setupTimePicker()
		setupDatePicker()
		setupChoosingPhotoDialog()
		setupGuestsInvitationDialog()
	}
	
	
	private fun setupGuestsInvitationDialog() {
		tv_pick_guests.setOnClickListener {
			
			val layout = layoutInflater.inflate(R.layout.guest_invitation_dialog, null)
			
			layout.input_emails.setTokenizer(Rfc822Tokenizer())
			layout.input_emails.setAdapter(BaseRecipientAdapter(this))
			
			val reportListener = object : ReportListener {
				override fun onReportGuestPick(user: User, isChecked: Boolean) {
					if (isChecked) {
						invitedExistingUsers.add(user)
					} else {
						invitedExistingUsers.remove(user)
					}
				}
			}
			
			val confirmListener = View.OnClickListener {
				if (layout.input_emails.sortedRecipients.isNullOrEmpty() && invitedExistingUsers.isNullOrEmpty()) return@OnClickListener
				val invitationList = mutableListOf<Invitation>()
				
				savedRecipients = layout.input_emails.sortedRecipients
				emails = layout.input_emails.sortedRecipients.map { d -> d.value.toString() }.toMutableList()
				
				emails.forEach { e ->
					buildInvitation(
						null,
						e,
						Invitation.InvitationType.EMAIL
					).also { invitationList.add(it) }
				}
				
				
				if (invitedExistingUsers.isNotEmpty()) {
					invitedExistingUsers.forEach { u ->
						buildInvitation(
							u,
							u.email,
							Invitation.InvitationType.NOTIFICATION
						).also { invitationList.add(it) }
					}
				}
				
				eventRequest.invitations = invitationList
				
				chip_group.removeAllViews()
				
				TextHelper.createContactChips(invitedExistingUsers, chip_group, closeUserChipListener)
				
				TextHelper.createContactChips(emails, chip_group, closeEmailChipListener)
				
			}
			
			
			val userAdapter = ReportGuestAdapter(this, reportListener, users)
			DialogHelper.showDialogWithAdapter(
				this, userAdapter,
				layout,
				getString(R.string.mgs_invite_guests), confirmListener, TextHelper.getQueryTextListener(userAdapter)
			)
		}
	}
	
	private val observeEventProgressState = Observer<State> { state ->
		when (state) {
			is LoadingState -> toggleProgress(progress, true)
			is ErrorState -> showError(state.error)
			is FinishedState -> {
				val message = if (isEdit) R.string.success_event_updated else R.string.success_event_created
				toggleProgress(progress, false)
				Toast.makeText(this@NewEventActivity, message, Toast.LENGTH_LONG).show()
				finish()
				startActivity(Intent(this@NewEventActivity, MainActivity::class.java))
			}
			is EventsViewModel.EventListState -> {
				toggleProgress(progress, false)
				setupInputs(state.events[0])
			}
			is UserViewModel.AppUserState -> {
				toggleProgress(progress, false)
				users = state.appUsers.filter { u -> !userRoleHelper.isSameUser(u) }.toMutableList()
			}
		}
	}
	
	private val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
		with(calendar) {
			set(Calendar.YEAR, year)
			set(Calendar.MONTH, month)
			set(Calendar.DAY_OF_MONTH, dayOfMonth)
		}
		
		updateDate()
	}
	
	private val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
		with(calendar) {
			set(Calendar.HOUR_OF_DAY, hourOfDay)
			set(Calendar.MINUTE, minute)
		}
		
		updateTime()
	}
	
	private fun setupDatePicker() {
		edit_text_date.setOnClickListener {
			DatePickerDialog(
				this, dateSetListener, calendar
					.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH)
			).show()
		}
	}
	
	private fun showError(error: Throwable) {
		Timber.e("Error $error while fetching events")
		toggleProgress(progress, false)
	}
	
	private fun initInputs() {
		eventName = input_event_name
		date = input_date
		time = input_time
		
		// for clearing purposes
		listOfInputs = mutableListOf(eventName, date)
		TextHelper.setRequiredMark(listOfInputs)
		// because of different validation
		TextHelper.setRequiredMark(mutableListOf(time))
	}
	
	private fun setupTimePicker() {
		edit_text_time.setOnClickListener {
			TimePickerDialog(
				this,
				timeSetListener,
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE),
				true
			).show()
		}
	}
	
	private fun updateDate() {
		val sdf = SimpleDateFormat(DateTimeHelper.DATE_FORMAT, Locale.US)
		edit_text_date.setText(sdf.format(calendar.time))
	}
	
	private fun updateTime() {
		val sdf = SimpleDateFormat(DateTimeHelper.TIME_FORMAT, Locale.US)
		edit_text_time.setText(sdf.format(calendar.time))
	}
	
	private fun setupChoosingPhotoDialog() {
		event_photo.setOnClickListener {
			PickImageDialog.build(PickSetup().apply {
				setTitle(R.string.choose_photo)
			}).show(supportFragmentManager)
		}
	}
	
	private fun handleCreateNewEvent() {
		btn_create_event.setOnClickListener {
//			validateTime()
			validator.validate()
		}
	}
	
	private fun validateTime() {
		val chosenDate =
			DateTimeHelper.parseDateTimeFromString(input_date.editText?.text.toString(), DateTimeHelper.DATE_FORMAT)
		if (!FutureTimeRule(chosenDate).isValid(time.editText)) {
			time.error = getString(R.string.error_future_time)
			time.isErrorEnabled = true
		} else {
			TextHelper.clearInputs(mutableListOf(time))
		}
	}
	
	private fun setupInputs(event: Event) {
		with(event) {
			input_event_name.editText?.setText(name)
			input_date.editText?.setText(DateTimeHelper.formatDateTimeString(date, DateTimeHelper.DATE_FORMAT))
			input_time.editText?.setText(DateTimeHelper.formatDateTimeString(date, DateTimeHelper.TIME_FORMAT))
			input_place.editText?.setText(place)
			
			photo?.let {
				event_photo.setImageBitmap(FileHelper.decodeImage(it))
			}
			
			if (!invitations.isNullOrEmpty()) {
				invitedExistingUsers = invitations.filter { i -> i.user != null }.map { i -> i.user!! }.toMutableList()
				emails = invitations.filter { i -> i.user == null }.map { i -> i.email }.toMutableList()
			}
			
			TextHelper.createContactChips(invitedExistingUsers, chip_group, closeUserChipListener)
			TextHelper.createContactChips(emails, chip_group, closeEmailChipListener)
		}
	}
	
	
	private val closeUserChipListener = View.OnClickListener {
		val userToDeleted = invitedExistingUsers.first { u -> u.userName == (it as Chip).text }
		invitedExistingUsers.remove(userToDeleted)
		users.first { u -> u == userToDeleted }.checked = false
		chip_group.removeView(it)
	}
	
	
	private val closeEmailChipListener = View.OnClickListener { chip ->
		val userToDeleted = emails.first { e -> e == (chip as Chip).text }
		emails.remove(userToDeleted)
		chip_group.removeView(chip)
	}
	
}
