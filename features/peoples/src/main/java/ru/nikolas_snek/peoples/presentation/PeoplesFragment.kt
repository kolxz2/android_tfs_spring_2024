package ru.nikolas_snek.peoples.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mvi.BaseFragmentMvi
import com.example.mvi.MviStore
import ru.nikolas_snek.chat.utils.NetworkChangeReceiver
import ru.nikolas_snek.data.utils.LoadingAttemptStatus
import ru.nikolas_snek.data_di.AppDepsStore
import ru.nikolas_snek.navigation.activityRouter
import ru.nikolas_snek.navigation.appScreens
import ru.nikolas_snek.peoples.R
import ru.nikolas_snek.ui_kit.R as RUiKit
import ru.nikolas_snek.peoples.databinding.FragmentPeoplesBinding
import ru.nikolas_snek.peoples.di.DaggerPeoplesFragmentComponent
import ru.nikolas_snek.peoples.domain.Profile
import ru.nikolas_snek.peoples.presentation.mvi.PeoplesEffect
import ru.nikolas_snek.peoples.presentation.mvi.PeoplesIntent
import ru.nikolas_snek.peoples.presentation.mvi.PeoplesPartialState
import ru.nikolas_snek.peoples.presentation.mvi.PeoplesState
import ru.nikolas_snek.peoples.presentation.recycler.UserProfileAdapter
import ru.nikolas_snek.ui_kit.utils.showSnackbarWithAction
import javax.inject.Inject

class PeoplesFragment :
	BaseFragmentMvi<PeoplesPartialState, PeoplesState, PeoplesEffect, PeoplesIntent>(R.layout.fragment_peoples) {
	private val binding by viewBinding(FragmentPeoplesBinding::bind)

	private lateinit var networkChangeReceiver: NetworkChangeReceiver

	@Inject
	lateinit var storeFactory: ViewModelProvider.Factory
	override val store: MviStore<
			PeoplesPartialState,
			PeoplesIntent,
			PeoplesState,
			PeoplesEffect> by viewModels {
		storeFactory
	}
	private val userProfileAdapter: UserProfileAdapter by lazy { UserProfileAdapter() }

	override fun onAttach(context: Context) {
		DaggerPeoplesFragmentComponent.builder().deps(AppDepsStore.appDependencies).build().inject(this)
		super.onAttach(context)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)


		userProfileAdapter.onProfileItemClickListener = {
			store.postIntent(PeoplesIntent.NavigateToUserPeoplesDetails(it.id))
		}

		binding.searchBar.etSearch.addTextChangedListener {
			it?.let {
				store.postIntent(PeoplesIntent.SearchQuery(it.toString()))
			}
		}

		binding.searchBar.etSearch.hint = getString(R.string.search_bar_text)

		binding.errorMessage.btReloadData.setOnClickListener {
			store.postIntent(PeoplesIntent.Reload)
		}


	}

	override fun onPause() {
		super.onPause()
		store.postIntent(PeoplesIntent.StopListeningPeoplesListChanges)
		networkChangeReceiver.unregister()
	}

	override fun onResume() {
		super.onResume()
		store.postIntent(PeoplesIntent.StartListeningPeoplesListChanges)
		networkChangeReceiver = NetworkChangeReceiver(requireContext()) { isConnected ->
			handleNetworkChange(isConnected)
		}
		networkChangeReceiver.register()
	}

	private fun handleNetworkChange(isConnected: Boolean) {
		if (isConnected) {
			store.postIntent(PeoplesIntent.Reload)
		}
	}

	override fun render(state: PeoplesState) {
		when (state.loadingStatus) {
			LoadingAttemptStatus.FIRST_ATTEMPT -> store.postIntent(PeoplesIntent.Reload)
			LoadingAttemptStatus.SUCCESS -> showProfilesList(state.listProfiles, state.searchQuery)
			LoadingAttemptStatus.LOADING -> startLoadingDataState()
			LoadingAttemptStatus.FAILURE -> failureLoadingDataState()
		}
	}

	override fun resolveEffect(effect: PeoplesEffect) {
		when (effect) {
			is PeoplesEffect.NavigateToUserPeoplesDetails -> activityRouter.navigateTo(
				appScreens.anotherPeopleScreen(effect.userId)
			)

			PeoplesEffect.NetworkError -> showSnackbarWithAction(
				binding.root,
				getString(RUiKit.string.error_snackbar_message),
				getString(RUiKit.string.error_snackbar_action),
			) {
				store.postIntent(PeoplesIntent.Reload)
			}
		}
	}


	private fun showProfilesList(profiles: List<Profile>, searchQuery: String?) {
		binding.rvPeoples.adapter = userProfileAdapter
		userProfileAdapter.submitList(profiles)
		if (binding.searchBar.etSearch.text.toString() != searchQuery.orEmpty()) {
			binding.searchBar.etSearch.setText(searchQuery.orEmpty())
		}

		binding.searchBar.etSearch.clearFocus()
		binding.rvPeoplesShimmer.isVisible = false
		binding.errorMessage.root.isVisible = false
		binding.rvPeoples.isVisible = true
	}

	private fun startLoadingDataState() {
		binding.rvPeoplesShimmer.isVisible = true
		binding.errorMessage.root.isVisible = false
		binding.rvPeoples.isVisible = false
	}

	private fun failureLoadingDataState() {
		binding.rvPeoplesShimmer.isVisible = false
		binding.errorMessage.root.isVisible = true
		binding.rvPeoples.isVisible = false
	}

	companion object {
		fun newInstance() = PeoplesFragment()
	}
}
