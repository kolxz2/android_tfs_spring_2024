package ru.nikolas_snek.tfsspring2024

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.nikolas_snek.navigation.NavigationHost

class MainActivity : AppCompatActivity(R.layout.activity_main), NavigationHost {
	private val navigator = AppNavigator(this, R.id.fragmentContainer)
	override lateinit var router: Router

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		router = (application as ZulipApplication).routerMainActivity
		if (savedInstanceState == null) {
			router.newRootScreen((application as ZulipApplication).appScreens.tabsMenuScreen())
		}
	}

	override fun onResumeFragments() {
		super.onResumeFragments()
		(application as ZulipApplication).navigatorHolderMainActivity.setNavigator(navigator)
	}

	override fun onPause() {
		(application as ZulipApplication).navigatorHolderMainActivity.removeNavigator()
		super.onPause()
	}
}