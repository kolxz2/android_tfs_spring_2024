package ru.nikolas_snek.channels.presentation

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import ru.nikolas_snek.channels.R
import ru.nikolas_snek.channels.databinding.FragmentChannelsBinding
import ru.nikolas_snek.channels.presentation.all_streams.AllStreamsFragment
import ru.nikolas_snek.channels.presentation.subscribed.SubscribedFragment
import ru.nikolas_snek.channels.presentation.utils.ChannelsAdapter
import ru.nikolas_snek.channels.presentation.utils.ViewPageScreens
import ru.nikolas_snek.navigation.NavigationBackPressedCallback
import ru.nikolas_snek.ui_kit.R.color.secondBackground as secondBackgroundColor

class ChannelsFragment : Fragment(R.layout.fragment_channels) {
	private val binding by viewBinding(FragmentChannelsBinding::bind)

	private val sharedViewModel by activityViewModels<SharedViewModel>()
	private lateinit var callback: OnBackPressedCallback
	private var viewPager: ViewPager2? = null

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initViewPager()
		onBackPressedAction()
		val window: Window? = activity?.window
		window?.statusBarColor =
			ContextCompat.getColor(requireContext(), secondBackgroundColor)
		viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
			override fun onPageSelected(position: Int) {
				when (ViewPageScreens.entries[position]) {
					ViewPageScreens.SUBSCRIBED -> binding.searchBar.etSearch.setText(
						sharedViewModel.subscribedStreamsSearchQuery.value.orEmpty()
					)

					ViewPageScreens.ALL_STREAMS -> binding.searchBar.etSearch.setText(
						sharedViewModel.allStreamsSearchQuery.value.orEmpty()
					)
				}
			}
		})

		binding.searchBar.etSearch.addTextChangedListener { searchQuery ->
			searchQuery?.let {
				viewLifecycleOwner.lifecycleScope.launch {
					viewPager?.currentItem?.let {
						when (ViewPageScreens.entries[it]) {
							ViewPageScreens.SUBSCRIBED -> sharedViewModel.subscribedStreamsSearchQuery.emit(
								searchQuery.toString()
							)

							ViewPageScreens.ALL_STREAMS -> sharedViewModel.allStreamsSearchQuery.emit(
								searchQuery.toString()
							)
						}
					}
				}
			}
		}
	}


	private fun initViewPager() {
		viewPager = binding.viewPager
		val fragmentsList = listOf(SubscribedFragment.newInstance(), AllStreamsFragment.newInstance())
		val adapter = ChannelsAdapter(childFragmentManager, lifecycle, fragmentsList)
		viewPager?.adapter = adapter
		viewPager?.let {
			TabLayoutMediator(binding.tabLayout, it) { tab, position ->
				tab.text = getString(ViewPageScreens.entries[position].pageTitleResId)
			}
		}?.attach()
	}


	private fun onBackPressedAction() {
		callback = object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				if (binding.viewPager.currentItem == 0) {
					(parentFragment as NavigationBackPressedCallback).callback.handleOnBackPressed()
				} else {
					binding.viewPager.currentItem = 0
				}
			}
		}
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		callback.isEnabled = false
	}

	companion object {
		fun newInstance() = ChannelsFragment()
	}
}