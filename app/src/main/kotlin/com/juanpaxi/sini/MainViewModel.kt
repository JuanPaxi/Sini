package com.juanpaxi.sini

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpaxi.sini.core.data.UserDataRepository
import com.juanpaxi.sini.core.model.data.DarkThemeConfig
import com.juanpaxi.sini.core.model.data.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val userDataRepository: UserDataRepository,
    ) : ViewModel() {
        val uiState: StateFlow<MainUiState> =
            userDataRepository
                .userData
                .map {
                    MainUiState.Success(it)
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = MainUiState.Loading,
                )
    }

sealed interface MainUiState {
    data object Loading : MainUiState

    data class Success(
        val userData: UserData,
    ) : MainUiState {
        override fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) =
            when (userData.darkThemeConfig) {
                DarkThemeConfig.FOLLOW_SYSTEM -> isSystemDarkTheme
                DarkThemeConfig.LIGHT -> false
                DarkThemeConfig.DARK -> true
            }
    }

    fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) = isSystemDarkTheme
}
