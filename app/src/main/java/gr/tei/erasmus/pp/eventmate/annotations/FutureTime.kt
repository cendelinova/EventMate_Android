package gr.tei.erasmus.pp.eventmate.annotations

import com.mobsandgeeks.saripaar.annotation.ValidateUsing

@ValidateUsing(FutureTimeRule::class)
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class FutureTime(
	val messageResId: Int = -1,
	val message: String = "Time should be in the future",
	val sequence: Int = -1
)
