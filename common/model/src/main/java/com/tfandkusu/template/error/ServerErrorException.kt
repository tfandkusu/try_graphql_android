package com.tfandkusu.template.error

/**
 * Server error
 */
class ServerErrorException(val code: Int, val httpMessage: String) : Exception()
