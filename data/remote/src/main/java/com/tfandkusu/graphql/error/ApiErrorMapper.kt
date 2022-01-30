package com.tfandkusu.graphql.error

import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.exception.ApolloNetworkException

class ApiErrorMapper {
    fun mapError(e: ApolloException): Throwable {
        return if (e is ApolloNetworkException) {
            NetworkErrorException()
        } else {
            UnknownErrorException()
        }
    }
}
