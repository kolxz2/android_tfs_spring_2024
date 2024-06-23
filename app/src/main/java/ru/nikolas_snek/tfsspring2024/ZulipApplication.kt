package ru.nikolas_snek.tfsspring2024

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import ru.nikolas_snek.navigation.AppScreensProvider

interface ZulipApplication: AppScreensProvider {
    var ciceroneMainActivity: Cicerone<Router>


    val routerMainActivity: Router
    val navigatorHolderMainActivity: NavigatorHolder

    var ciceroneTabsMenuFragment: Cicerone<Router>
    val routerTabsMenuFragment: Router
    val navigatorHolderTabsMenuFragment: NavigatorHolder
}