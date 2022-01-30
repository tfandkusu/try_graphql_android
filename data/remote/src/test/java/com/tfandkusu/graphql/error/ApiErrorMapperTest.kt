package com.tfandkusu.graphql.error

import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.exception.JsonEncodingException
import io.kotlintest.matchers.types.shouldBeTypeOf
import org.junit.Test

class ApiErrorMapperTest {

    private val errorHelper = ApiErrorMapper()

    @Test
    fun network() {
        errorHelper.mapError(ApolloNetworkException()).shouldBeTypeOf<NetworkErrorException>()
    }

    @Test
    fun unknown() {
        errorHelper.mapError(
            JsonEncodingException("Some message")
        ).shouldBeTypeOf<UnknownErrorException>()
    }
}
