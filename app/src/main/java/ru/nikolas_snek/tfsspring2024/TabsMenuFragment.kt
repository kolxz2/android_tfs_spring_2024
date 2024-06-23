package ru.nikolas_snek.tfsspring2024

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.nikolas_snek.ui_kit.R.color.secondBackground as secondBackgroundColor
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.nikolas_snek.navigation.NavigationBackPressedCallback
import ru.nikolas_snek.navigation.appScreens
import ru.nikolas_snek.tfsspring2024.databinding.FragmentTabsMenuBinding

class TabsMenuFragment : Fragment(R.layout.fragment_tabs_menu), NavigationBackPressedCallback {
	private lateinit var router: Router
	private val navigator by lazy {
		AppNavigator(
			activity = requireActivity(),
			fragmentManager = childFragmentManager,
			containerId = R.id.tabFragmentContainer
		)
	}
	private val binding by viewBinding(FragmentTabsMenuBinding::bind)
	override val callback = object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			if (binding.tabBottomNavigationView.selectedItemId == R.id.channels) {
				requireActivity().finish()
			} else {
				binding.tabBottomNavigationView.selectedItemId = R.id.channels
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		router = (requireActivity().application as ZulipApplication).routerTabsMenuFragment
		if (savedInstanceState == null) {
			router.newRootScreen(appScreens.channelsScreen())
		}

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		appNavigation(savedInstanceState)
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
		val window: Window? = activity?.window
		window?.statusBarColor =
			ContextCompat.getColor(requireContext(), secondBackgroundColor)
	}

	private fun appNavigation(savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			binding.tabBottomNavigationView.selectedItemId = R.id.channels
		}

		binding.tabBottomNavigationView.setOnItemSelectedListener { item ->
			when (item.itemId) {
				R.id.profile -> {
					router.navigateTo(appScreens.profileScreen())
					true
				}

				R.id.people -> {
					router.navigateTo(appScreens.peoplesScreen())
					true
				}

				R.id.channels -> {
					router.navigateTo(appScreens.channelsScreen())
					true
				}

				else -> false
			}
		}
	}

	override fun onResume() {
		super.onResume()
		callback.isEnabled = true
		(requireActivity().application as ZulipApplication).navigatorHolderTabsMenuFragment.setNavigator(
			navigator
		)
	}

	override fun onPause() {
		(requireActivity().application as ZulipApplication).navigatorHolderTabsMenuFragment.removeNavigator()
		callback.isEnabled = false
		super.onPause()
	}

	companion object {
		fun newInstance() = TabsMenuFragment()
	}
}