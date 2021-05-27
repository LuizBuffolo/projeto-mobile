package com.example.newsappt2.presentation.common

class Event<T>(private val content: T) {
    private var hasBeenHandled: Boolean = false

    fun handleEvent(handleFunction: (T) -> Unit) {
        if(!hasBeenHandled){
            handleFunction(content)
            hasBeenHandled = true
        }
    }
}