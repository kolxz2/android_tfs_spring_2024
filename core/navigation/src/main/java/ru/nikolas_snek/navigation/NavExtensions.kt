package ru.nikolas_snek.navigation

import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router

val Fragment.activityRouter: Router
	get() = (requireActivity() as NavigationHost).router

val Fragment.appScreens: AppScreens
	get() = (requireActivity().application as AppScreensProvider).appScreens
