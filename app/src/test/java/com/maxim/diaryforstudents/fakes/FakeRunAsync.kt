package com.maxim.diaryforstudents.fakes

import com.maxim.diaryforstudents.core.presentation.RunAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

class FakeRunAsync : RunAsync {
    private lateinit var cached: (Any) -> Unit
    private lateinit var cachedArgument: Any
    fun returnResult() {
        cached.invoke(cachedArgument)
    }

    override fun <T : Any> handleAsync(
        coroutineScope: CoroutineScope,
        backgroundBlock: suspend () -> T,
        uiBlock: (T) -> Unit
    ) = runBlocking {
        cachedArgument = backgroundBlock.invoke()
        cached = uiBlock as (Any) -> Unit
    }
}