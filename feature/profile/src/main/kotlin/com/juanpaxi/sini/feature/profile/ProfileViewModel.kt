package com.juanpaxi.sini.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpaxi.sini.core.data.UserDataRepository
import com.juanpaxi.sini.core.model.data.DarkThemeConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val userDataRepository: UserDataRepository,
    ) : ViewModel() {
        val darkThemeConfig =
            userDataRepository.userData
                .map { userData ->
                    userData.darkThemeConfig
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = DarkThemeConfig.FOLLOW_SYSTEM,
                )

        fun setDarkTheme(darkThemeConfig: DarkThemeConfig) {
            viewModelScope.launch {
                userDataRepository.setDarkThemeConfig(darkThemeConfig)
            }
        }
    }
