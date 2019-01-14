package gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests


import android.content.Intent
import android.os.Bundle
import android.text.util.Rfc822Tokenizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.android.ex.chips.BaseRecipientAdapter
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants
import gr.tei.erasmus.pp.eventmate.data.model.Invitation
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.helpers.DialogHelper
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.showError
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper.toggleProgress
import gr.tei.erasmus.pp.eventmate.helpers.TextHelper
import gr.tei.erasmus.pp.eventmate.ui.base.BaseFragment
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.EventDetailActivity
import gr.tei.erasmus.pp.eventmate.ui.report.ReportGuestAdapter
import gr.tei.erasmus.pp.eventmate.ui.userProfile.UserProfileActivity
import kotlinx.android.synthetic.main.fragment_guests.*
import kotlinx.android.synthetic.main.guest_invitation_dialog.view.*
import timber.log.Timber

class GuestsFragment : BaseFragment() {
	
	private lateinit var guestAdapter: UserAdapter
	
	private var eventId: Long? = null
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(UserViewModel::class.java) }
	
	private var invitedExistingUsers = mutableListOf<User>()
	private var emails = mutableListOf<String>()
	private var users: MutableList<User> = mutableListOf()
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		eventId = arguments?.getLong(Constants.EVENT_ID)
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_guests, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeRecyclerView()
		observeViewModel()
		
		eventId?.let {
			viewModel.getGuests(it)
		}
		
		viewModel.getAppUsers()
		handleAddGuest()
	}
	
	private fun handleAddGuest() {
		btn_add_guests.setOnClickListener {
			setupGuestsInvitationDialog()
		}
		
		btn_add_guests.visibility =
				if ((activity as EventDetailActivity).isEditableEvent()) View.VISIBLE else View.GONE
	}
	
	/**
	 * We have blacklist items obtained, initialize recyclerView and display them.
	 */
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		guestAdapter = UserAdapter(
			context!!,
			onUserClick,
			mutableListOf()
		)
		
		with(guest_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(guest_empty_view)
			layoutManager = GridLayoutManager(context!!, 3)
			adapter = guestAdapter
		}
	}
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeUserProgressState)
		}
	}
	
	
	private val onUserClick = object :
		UserAdapter.GuestListener {
		override fun onUserClick(user: User) {
			startActivity(Intent(this@GuestsFragment.activity, UserProfileActivity::class.java))
		}
	}
	
	private val observeUserProgressState = Observer<State> { state ->
		when (state) {
			is LoadingState -> toggleProgress(progress, true)
			is ErrorState -> showError(state.error, progress, guests_fragment)
			is UserViewModel.UserListState -> {
				toggleProgress(progress, false)
				guestAdapter.updateUserList(state.users)
			}
			is UserViewModel.AppUserState -> {
				StateHelper.toggleProgress(progress, false)
				users = state.appUsers
			}
		}
	}
	
	private fun setupGuestsInvitationDialog() {
		
		val layout = layoutInflater.inflate(R.layout.guest_invitation_dialog, null)
		
		layout.input_emails.setTokenizer(Rfc822Tokenizer())
		layout.input_emails.setAdapter(BaseRecipientAdapter(this@GuestsFragment.context))
		
		val reportListener = object : ReportGuestAdapter.ReportListener {
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
			
			emails = layout.input_emails.sortedRecipients.map { d -> d.value.toString() }.toMutableList()
			
			emails.forEach { e ->
				Invitation.buildInvitation(
					null,
					e,
					Invitation.InvitationType.EMAIL
				).also { invitationList.add(it) }
			}
			
			
			if (invitedExistingUsers.isNotEmpty()) {
				invitedExistingUsers.forEach { u ->
					Invitation.buildInvitation(
						u,
						u.email,
						Invitation.InvitationType.EMAIL_AND_NOTIFICATION
					).also { invitationList.add(it) }
				}
			}
			
			eventId?.let {
				viewModel.inviteGuests(it, invitationList)
			}
		}
		
		val userAdapter = ReportGuestAdapter(this@GuestsFragment.context!!, reportListener, users)
		DialogHelper.showDialogWithAdapter(
			this@GuestsFragment.context!!, userAdapter,
			layout,
			"", confirmListener, TextHelper.getQueryTextListener(userAdapter)
		)
	}
	
	
}
