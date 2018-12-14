package gr.tei.erasmus.pp.eventmate.data.repository

import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.InvitationDao

class InvitationRepository(private val invitationDao: InvitationDao) {
	
	fun getAllInvitation(eventId: Long) = invitationDao.getAllInvitation(eventId)
	
}