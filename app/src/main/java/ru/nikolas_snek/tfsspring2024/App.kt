package ru.nikolas_snek.tfsspring2024

import android.app.Application
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import ru.nikolas_snek.data_di.AppDepsStore
import ru.nikolas_snek.navigation.AppScreens
import ru.nikolas_snek.tfsspring2024.di.AppComponent
import ru.nikolas_snek.tfsspring2024.di.DaggerAppComponent
import javax.inject.Inject

class App : Application(),  ZulipApplication {
    @Inject
    override lateinit var ciceroneMainActivity: Cicerone<Router>

    @Inject
    override lateinit var appScreens: AppScreens
    override val routerMainActivity get() = ciceroneMainActivity.router
    override val navigatorHolderMainActivity get() = ciceroneMainActivity.getNavigatorHolder()

    @Inject
    override lateinit var ciceroneTabsMenuFragment: Cicerone<Router>
    override val routerTabsMenuFragment get() = ciceroneTabsMenuFragment.router
    override val navigatorHolderTabsMenuFragment get() = ciceroneTabsMenuFragment.getNavigatorHolder()

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        AppDepsStore.appDependencies = appComponent
        INSTANCE = this
    }

    companion object {
        internal lateinit var INSTANCE: App
            private set
    }
}