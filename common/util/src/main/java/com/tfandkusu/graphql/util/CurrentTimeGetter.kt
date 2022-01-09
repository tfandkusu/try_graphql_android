package com.tfandkusu.graphql.util

import javax.inject.Inject

interface CurrentTimeGetter {
    fun get(): Long
}

class CurrentTimeGetterImpl @Inject constructor() : CurrentTimeGetter {
    override fun get() = System.currentTimeMillis()
}
