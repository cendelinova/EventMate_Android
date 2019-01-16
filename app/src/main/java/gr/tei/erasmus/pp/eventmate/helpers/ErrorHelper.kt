package gr.tei.erasmus.pp.eventmate.helpers

import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.helpers.ErrorHelper.ErrorType.*
import okhttp3.Headers

object ErrorHelper {
	
	private const val ERROR_HEADER = "c-error-code"
	
	fun getErrorMessageFromHeader(headers: Headers): String {
		return headers.get(ERROR_HEADER)?.toInt()?.let { getErrorMessage(it) }.toString()
	}
	
	fun getErrorMessage(errorCode: Int): String {
		val context = App.COMPONENTS.provideContext()
		
		val message = when (errorCode) {
			USER_NOT_REPORT_CREATOR.errorCode -> USER_NOT_REPORT_CREATOR.errorMessage
			ENTITY_NOT_FOUND.errorCode -> ENTITY_NOT_FOUND.errorMessage
			NO_REPORTS_IN_EVENT.errorCode -> NO_REPORTS_IN_EVENT.errorMessage
			NO_SUBMISSION.errorCode -> NO_SUBMISSION.errorMessage
			USER_NOT_EVENT_OWNER.errorCode -> USER_NOT_EVENT_OWNER.errorMessage
			USER_NOT_TASK_OWNER.errorCode -> USER_NOT_TASK_OWNER.errorMessage
			USER_NOT_TASK_ASSIGNEE.errorCode -> USER_NOT_TASK_ASSIGNEE.errorMessage
			NO_PERMISSION.errorCode -> NO_PERMISSION.errorMessage
			NO_PERMISSION_FOR_EVENT.errorCode -> NO_PERMISSION_FOR_EVENT.errorMessage
			NO_PERMISSION_FOR_TASK.errorCode -> NO_PERMISSION_FOR_TASK.errorMessage
			NO_PERMISSION_FOR_DELETE_REPORT.errorCode -> NO_PERMISSION_FOR_DELETE_REPORT.errorMessage
			NO_PERMISSION_FOR_SEND_SUBMISSION.errorCode -> NO_PERMISSION_FOR_SEND_SUBMISSION.errorMessage
			NO_PERMISSION_FOR_SUBMISSION_FILE.errorCode -> NO_PERMISSION_FOR_SUBMISSION_FILE.errorMessage
			NO_PERMISSION_FOR_SUBMISSION_FILE.errorCode -> NO_PERMISSION_FOR_SUBMISSION_FILE.errorMessage
			NO_PERMISSION_FOR_EDIT_TASK.errorCode -> NO_PERMISSION_FOR_EDIT_TASK.errorMessage
			NO_PERMISSION_FOR_ASSIGN_POINTS.errorCode -> NO_PERMISSION_FOR_ASSIGN_POINTS.errorMessage
			EMAIL_ALREADY_USED.errorCode -> EMAIL_ALREADY_USED.errorMessage
			EVENT_NAME_USED.errorCode -> EVENT_NAME_USED.errorMessage
			TASK_IS_NOT_IN_PLAYABLE_STATE.errorCode -> TASK_IS_NOT_IN_PLAYABLE_STATE.errorMessage
			TASK_IS_NOT_IN_REVIEW_STATE.errorCode -> TASK_IS_NOT_IN_REVIEW_STATE.errorMessage
			EVENT_NOT_IN_EDITABLE_STATE.errorCode -> EVENT_NOT_IN_EDITABLE_STATE.errorMessage
			EVENT_NOT_IN_FINISHED_STATE.errorCode -> EVENT_NOT_IN_FINISHED_STATE.errorMessage
			MORE_POINTS_THAN_ALLOWED.errorCode -> MORE_POINTS_THAN_ALLOWED.errorMessage
			
			else -> {
				NO_PERMISSION.errorMessage
			}
		}
		
		return context.getString(message)
	}
	
	enum class ErrorType(val errorCode: Int, val errorMessage: Int) {
		USER_NOT_REPORT_CREATOR(4021, R.string.error_user_not_report_creator),
		ENTITY_NOT_FOUND(4000, R.string.error_entity_not_found),
		NO_REPORTS_IN_EVENT(4017, R.string.error_no_reports_in_event),
		NO_SUBMISSION(4014, R.string.error_no_submission),
		
		USER_NOT_EVENT_OWNER(4000, R.string.error_user_not_event_owner),
		USER_NOT_TASK_OWNER(4020, R.string.error_user_not_task_owner),
		USER_NOT_TASK_ASSIGNEE(4012, R.string.error_user_not_task_assignee),
		
		NO_PERMISSION(4002, R.string.error_no_permission),
		NO_PERMISSION_FOR_EVENT(4003, R.string.error_no_permission_event),
		NO_PERMISSION_FOR_TASK(4004, R.string.error_no_permission_task),
		NO_PERMISSION_FOR_DELETE_REPORT(4005, R.string.error_no_permission_delete_report),
		NO_PERMISSION_FOR_SEND_SUBMISSION(4006, R.string.error_no_permission_send_submission),
		NO_PERMISSION_FOR_SUBMISSION_FILE(4007, R.string.error_no_permission_submission_file),
		NO_PERMISSION_FOR_EDIT_TASK(4008, R.string.error_no_permission_edit_task),
		NO_PERMISSION_FOR_ASSIGN_POINTS(4013, R.string.error_no_permission_assign_points),
		
		
		EMAIL_ALREADY_USED(4015, R.string.error_email_already_used),
		EVENT_NAME_USED(4009, R.string.error_event_name_used),
		
		TASK_IS_NOT_IN_PLAYABLE_STATE(4018, R.string.error_task_not_playble_state),
		TASK_IS_NOT_IN_REVIEW_STATE(4019, R.string.error_task_not_review_state),
		EVENT_NOT_IN_EDITABLE_STATE(4010, R.string.error_event_not_editable_state),
		EVENT_NOT_IN_FINISHED_STATE(4011, R.string.error_event_not_finished_state),
		
		MORE_POINTS_THAN_ALLOWED(4016, R.string.error_more_points_than_max)
	}
	
}