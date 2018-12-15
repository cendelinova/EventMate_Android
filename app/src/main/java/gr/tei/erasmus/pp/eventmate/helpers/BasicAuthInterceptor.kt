package gr.tei.erasmus.pp.eventmate.helpers

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response


class BasicAuthInterceptor(val user: String, password: String) : Interceptor {
	private var credentials: String = Credentials.basic(user, password)
	
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		val authenticatedRequest = request.newBuilder()
			.header("Authorization", credentials).build()
		return chain.proceed(authenticatedRequest)
	}
}