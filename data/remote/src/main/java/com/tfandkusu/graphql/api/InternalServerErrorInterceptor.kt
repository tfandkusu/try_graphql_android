package com.tfandkusu.graphql.api

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class InternalServerErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return Response.Builder()
            .request(request)
            .message("Internal Server Error")
            .protocol(Protocol.HTTP_1_1)
            .code(500)
            .body("Internal Server Error".toResponseBody("text/plain".toMediaType()))
            .build()
    }
}
