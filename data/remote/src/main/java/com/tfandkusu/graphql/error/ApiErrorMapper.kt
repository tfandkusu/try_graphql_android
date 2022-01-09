package com.tfandkusu.graphql.error

import java.io.IOException
import retrofit2.HttpException

fun mapApiError(e: Throwable): Throwable {
    return when (e) {
        is IOException -> {
            NetworkErrorException()
        }
        is HttpException -> {
            ServerErrorException(e.code(), e.message())
        }
        else -> {
            UnknownErrorException()
        }
    }
}
