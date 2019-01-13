package gr.tei.erasmus.pp.eventmate.helpers

import gr.tei.erasmus.pp.eventmate.data.model.*
import gr.tei.erasmus.pp.eventmate.data.source.remote.services.*
import retrofit2.Retrofit

class RestHelper(retrofit: Retrofit) {
	
	// Retrofit services
	private val eventService: EventService = retrofit.create(EventService::class.java)
	private val userService: UserService = retrofit.create(UserService::class.java)
	private val taskService = retrofit.create(TaskService::class.java)
	private val submissionService = retrofit.create(SubmissionService::class.java)
	private val reportService = retrofit.create(ReportService::class.java)
	
	// events
	fun getEvents() = eventService.getEvents()
	fun getEvent(eventId: Long) = eventService.getEvent(eventId)
	fun insertEvent(event: EventRequest) = eventService.insertEvent(event)
	fun updateEvent(event: EventRequest) = event.id?.let { eventService.updateEvent(it, event) }
	fun deleteEvent(eventId: Long) = eventService.deleteEvent(eventId)
	fun inviteGuests(eventId: Long, invitations: MutableList<Invitation>) = eventService.inviteGuests(eventId, invitations)
	
	// tasks
	fun insertTask(task: TaskRequest) = taskService.insertTask(task.eventId, task)
	fun getTask(taskId: Long) = taskService.getTask(taskId)
	fun getEventTasks(eventId: Long) = taskService.getEventTasks(eventId)
	fun deleteTask(taskId: Long) = taskService.deleteTask(taskId)
	fun assignPoints(taskId: Long, userSubmissionPointList: MutableList<UserSubmissionPoints>) = taskService.assignPoints(taskId, userSubmissionPointList)
	
	// submissions
	fun getUserTaskSubmissions(userId: Long, taskId: Long) = submissionService.getSubmissions(taskId, userId)
	fun saveSubmission(taskId: Long, submissionFile: SubmissionFile) = submissionService.saveSubmissionFile(taskId, submissionFile)
	
	// users
	fun getUser(userId: Long) = userService.getUser(userId)
	fun getAppUsers() = userService.getAllUsers()
	fun registerUser(user: UserRequest) = userService.registerUser(user)
	fun getMyProfile() = userService.getMyProfile()
	fun getEventGuests(eventId: Long) = userService.getEventGuests(eventId)
	
	// reports
	fun getEventReports(eventId: Long) = reportService.getEventReports(eventId)
	fun getEventReport(reportId: Long) = reportService.getReport(reportId)
	fun saveEventReport(eventId: Long, report: ReportRequest) = reportService.saveReport(eventId, report)
	fun deleteEventReport(reportId: Long) = reportService.deleteReport(reportId)
	fun shareReport(reportId: Long, email: Email) = reportService.shareReport(reportId, email)
	
}