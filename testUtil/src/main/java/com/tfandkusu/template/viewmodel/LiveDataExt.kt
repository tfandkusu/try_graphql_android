package com.tfandkusu.template.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.mockk.mockk

fun <T> LiveData<T>.mockStateObserver(): Observer<T> {
    val mockObserver = mockk<Observer<T>>(relaxed = true, name = "state")
    observeForever(mockObserver)
    return mockObserver
}
