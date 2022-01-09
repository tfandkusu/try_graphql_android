package com.tfandkusu.template.viewmodel

import androidx.lifecycle.MutableLiveData

/**
 * For update state
 */
fun <T> MutableLiveData<T>.update(action: T.() -> T) {
    value = requireNotNull(value).action()
}

/**
 * For update state with suspend method
 */
suspend fun <T> MutableLiveData<T>.coUpdate(action: suspend T.() -> T) {
    value = requireNotNull(value).action()
}
