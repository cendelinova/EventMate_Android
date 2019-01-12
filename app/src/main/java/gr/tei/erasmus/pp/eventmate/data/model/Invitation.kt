package gr.tei.erasmus.pp.eventmate.data.model

data class Invitation(
	val id: Long?,
	val user: User?,
	val email: String,
	val invitationType: String,
	val invitationState: String
) {
	
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
