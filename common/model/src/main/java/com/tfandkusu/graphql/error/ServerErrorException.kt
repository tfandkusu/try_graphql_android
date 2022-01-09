package com.tfandkusu.graphql.error

/**
 * Server error
 */
class ServerErrorException(val code: Int, val httpMessage: String) : Exception()
