package ru.nikolas_snek.tfsspring2024

import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.nikolas_snek.another_people.presentation.AnotherPeopleFragment
import ru.nikolas_snek.channels.presentation.ChannelsFragment
import ru.nikolas_snek.chat.presentation.ChatFragment
import ru.nikolas_snek.navigation.AppScreens
import ru.nikolas_snek.peoples.presentation.PeoplesFragment
import ru.nikolas_snek.profile.presentation.ProfileFragment
import javax.inject.Inject

class AppScreensImpl @Inject constructor() : AppScreens {
    override fun chatScreen(streamTitle: String, topicTitle: String): Screen {
        return FragmentScreen { ChatFragment.newInstance(streamTitle, topicTitle) }
    }

    override fun peoplesScreen(): Screen {
        return FragmentScreen { PeoplesFragment.newInstance() }
    }

    override fun channelsScreen(): Screen {
        return FragmentScreen { ChannelsFragment.newInstance() }
    }

    override fun profileScreen(): Screen {
        return FragmentScreen { ProfileFragment.newInstance() }
    }

    override fun tabsMenuScreen(): Screen {
        return FragmentScreen { TabsMenuFragment.newInstance() }
    }

    override fun anotherPeopleScreen(userId: Int): Screen {
        return FragmentScreen { AnotherPeopleFragment.newInstance(userId) }
    }
}