package com.tfandkusu.template.viewmodel

import androidx.lifecycle.LiveData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

/**
 * Reference: https://github.com/DroidKaigi/conference-app-2021/blob/main/uicomponent-compose/core/src/main/java/io/github/droidkaigi/feeder/core/UnidirectionalViewModel.kt
 */
interface UnidirectionalViewModel<EVENT, EFFECT, STATE> {

    fun createDefaultState(): STATE

    val state: LiveData<STATE>
    val effect: Flow<EFFECT>

    fun event(event: EVENT)

    fun createEffectChannel(): Channel<EFFECT> {
        return Channel(Channel.UNLIMITED)
    }
}
