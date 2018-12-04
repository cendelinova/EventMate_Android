package gr.tei.erasmus.pp.eventmate.annotations

import com.mobsandgeeks.saripaar.annotation.ValidateUsing


@ValidateUsing(FutureDateRule::class)
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class FutureDate(
	val messageResId: Int = -1,
	val message: String = "Date should be in the future",
	val sequence: Int = -1
)
