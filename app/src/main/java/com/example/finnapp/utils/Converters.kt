package com.example.finnapp.utils

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

object Converters {

    fun <T>Flow<T>.launchWhenCreated (lifecycleScope: LifecycleCoroutineScope){
        lifecycleScope.launchWhenCreated {
            this@launchWhenCreated.collect()
        }
    }

    fun replaceRange(string: String, int: Int):String{
        if (string.length < int)
            return string
        return string.replaceRange(
            int until string.length,
            "..."
        )
    }
}