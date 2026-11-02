package com.juanpaxi.sini.feature.profile.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.juanpaxi.sini.core.navigation.Navigator
import com.juanpaxi.sini.feature.profile.ProfileScreen

fun EntryProviderScope<NavKey>.profileEntry(navigator: Navigator) {
    entry<ProfileNavKey> {
        ProfileScreen()
    }
}
