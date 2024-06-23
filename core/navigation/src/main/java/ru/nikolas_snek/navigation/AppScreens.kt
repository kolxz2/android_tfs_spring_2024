package ru.nikolas_snek.navigation

import com.github.terrakok.cicerone.Screen

interface AppScreens {
    fun chatScreen(streamTitle: String, topicTitle: String): Screen
    fun peoplesScreen(): Screen
    fun channelsScreen(): Screen
    fun profileScreen(): Screen
    fun tabsMenuScreen(): Screen
    fun anotherPeopleScreen(userId: Int): Screen
}