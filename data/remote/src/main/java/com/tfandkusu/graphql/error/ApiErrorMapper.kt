package com.tfandkusu.graphql.error

import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.ApolloNetworkException

class ApiErrorMapper {
    fun mapError(e: ApolloException): Throwable {
        return when (e) {
            is ApolloNetworkException -> {
                NetworkErrorException()
            }
            is ApolloHttpException -> {
                ServerErrorException(e.statusCode, e.message ?: "")
            }
            else -> {
                UnknownErrorException()
            }
        }
    }
}
