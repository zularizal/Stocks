package com.ap.mobile.stocks.dagger

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Suppress("UNCHECKED_CAST")
@Singleton
class VMFactory @Inject constructor(
        private val viewModelMap: Map<Class<out ViewModel>,
                @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var viewModel = viewModelMap[modelClass]

        if (viewModel == null) {
            for (entry in viewModelMap) {
                if (modelClass.isAssignableFrom(entry.key)) {
                    viewModel = entry.value
                    break
                }
            }
        }
        if (viewModel == null) throw IllegalArgumentException("Unknown model class $modelClass")
        return viewModel.get() as T
    }
}