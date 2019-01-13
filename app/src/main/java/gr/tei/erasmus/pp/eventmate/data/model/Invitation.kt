package gr.tei.erasmus.pp.eventmate.data.model

data class Invitation(
	val id: Long?,
	val user: User?,
	val email: String,
	val invitationType: String,
	val invitationState: String
) {
	
	companion object {
		fun buildInvitation(user: User?, email: String, type: Invitation.InvitationType) = Invitation(
			null,
			user,
			email,
			type.name,
			Invitation.InvitationState.PENDING.name
		)
	}
	
	enum class InvitationType {
		EMAIL,
		NOTIFICATION,
		EMAIL_AND_NOTIFICATION
	}
	
	internal enum class InvitationState {
		PENDING,
		ACCEPTED,
		REJECTED
	}
}
