package dev.kirstenbaker.gallery.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
A simple class that helps with debugging API calls.
 */
object RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d(this.javaClass.toString(), "Outgoing message url: ${request.url}")
        return chain.proceed(request)
    }
}