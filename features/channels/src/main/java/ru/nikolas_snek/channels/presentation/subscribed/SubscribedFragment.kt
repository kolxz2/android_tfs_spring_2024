package ru.nikolas_snek.channels.presentation.subscribed

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mvi.BaseFragmentMvi
import com.example.mvi.MviStore
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.nikolas_snek.ui_kit.R as RUiKit
import ru.nikolas_snek.channels.R
import ru.nikolas_snek.channels.databinding.FragmentSubscribedBinding
import ru.nikolas_snek.channels.di.DaggerSubscribedFragmentComponent
import ru.nikolas_snek.channels.domain.models.Stream
import ru.nikolas_snek.channels.domain.models.Topic
import ru.nikolas_snek.channels.presentation.SharedViewModel
import ru.nikolas_snek.channels.presentation.mvi.SubscribedEffect
import ru.nikolas_snek.channels.presentation.mvi.SubscribedStore
import ru.nikolas_snek.channels.presentation.subscribed.mvi.SubscribedIntent
import ru.nikolas_snek.channels.presentation.subscribed.mvi.SubscribedPartialState
import ru.nikolas_snek.channels.presentation.subscribed.mvi.SubscribedState
import ru.nikolas_snek.channels.presentation.subscribed.recycler.SubscribedStreamsAdapter
import ru.nikolas_snek.channels.presentation.subscribed.recycler.utils.SubscribedRecyclerItems
import ru.nikolas_snek.chat.utils.NetworkChangeReceiver
import ru.nikolas_snek.data.utils.LoadingAttemptStatus
import ru.nikolas_snek.data_di.AppDepsStore
import ru.nikolas_snek.navigation.activityRouter
import ru.nikolas_snek.navigation.appScreens
import ru.nikolas_snek.ui_kit.utils.showSnackbarWithAction
import javax.inject.Inject

class SubscribedFragment :
	BaseFragmentMvi<SubscribedPartialState, SubscribedState, SubscribedEffect, SubscribedIntent>(R.layout.fragment_subscribed) {
	@Inject
	lateinit var storeFactory: ViewModelProvider.Factory
	override val store: MviStore<
			SubscribedPartialState,
			SubscribedIntent,
			SubscribedState,
			SubscribedEffect> by viewModels(
		ownerProducer = { this },
		factoryProducer = { storeFactory }
	)
	private val sharedViewModel by activityViewModels<SharedViewModel>()
	private val binding by viewBinding(FragmentSubscribedBinding::bind)
	private lateinit var adapter: SubscribedStreamsAdapter
	private lateinit var networkChangeReceiver: NetworkChangeReceiver


	private var onStreamClickListener: ((Stream) -> Unit) = { stream ->
		store.postIntent(SubscribedIntent.LoadStreamTopics(stream))
	}
	private var onTopiClickListener: ((Topic) -> Unit) = { topic ->
		store.postIntent(
			SubscribedIntent.NavigateToTopicChat(
				streamTitle = topic.streamTitle,
				topicTitle = topic.title
			)
		)
	}

	override fun onAttach(context: Context) {
		DaggerSubscribedFragmentComponent.builder().deps(AppDepsStore.appDependencies).build().inject(this)
		adapter = SubscribedStreamsAdapter(onStreamClickListener, onTopiClickListener)
		super.onAttach(context)
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.rvSubscribedStreams.adapter = adapter

		binding.errorMessage.btReloadData.setOnClickListener {
			viewLifecycleOwner.lifecycleScope.launch {
				store.postIntent(SubscribedIntent.ReloadSubscribed)
			}
		}

		sharedViewModel.subscribedStreamsSearchQuery
			.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
			.onEach { query ->
				query?.let {
					(store as SubscribedStore).searchQueryPublisher.emit(query)
				}
			}
			.launchIn(viewLifecycleOwner.lifecycleScope)
	}

	override fun render(state: SubscribedState) {
		when (state.loadingStatus) {
			LoadingAttemptStatus.LOADING -> startLoadingStreamsState()
			LoadingAttemptStatus.FIRST_ATTEMPT -> store.postIntent(SubscribedIntent.ReloadSubscribed)
			LoadingAttemptStatus.SUCCESS -> {
				if (state.isLoadingTopics) startLoadingTopicsState() else showStreams(state.dataLoaded)
			}

			LoadingAttemptStatus.FAILURE -> failureLoadingDataState()
		}
	}

	override fun resolveEffect(effect: SubscribedEffect) {
		when (effect) {
			is SubscribedEffect.NavigateToTopicChat -> activityRouter.navigateTo(
				appScreens.chatScreen(
					effect.streamTitle,
					effect.topicTitle
				)
			)

			SubscribedEffect.NetworkError -> showSnackbarWithAction(
				binding.root,
				getString(RUiKit.string.error_snackbar_message),
				getString(RUiKit.string.error_snackbar_action),
			) {
				store.postIntent(SubscribedIntent.ReloadSubscribed)
			}
		}
	}


	private fun showStreams(stream: List<SubscribedRecyclerItems>) {
		binding.shimmerList.root.isVisible = false
		binding.errorMessage.root.isVisible = false
		binding.rvSubscribedStreams.isVisible = true
		binding.pbSubscribed.visibility = View.INVISIBLE
		adapter.submitList(stream)
	}

	private fun startLoadingStreamsState() {
		binding.shimmerList.root.isVisible = true
		binding.errorMessage.root.isVisible = false
		binding.rvSubscribedStreams.isVisible = false
		binding.pbSubscribed.visibility = View.INVISIBLE
	}

	private fun startLoadingTopicsState() {
		binding.shimmerList.root.isVisible = false
		binding.errorMessage.root.isVisible = false
		binding.rvSubscribedStreams.isVisible = true
		binding.pbSubscribed.visibility = View.VISIBLE
	}

	private fun failureLoadingDataState() {
		binding.shimmerList.root.isVisible = false
		binding.errorMessage.root.isVisible = true
		binding.rvSubscribedStreams.isVisible = false
		binding.pbSubscribed.visibility = View.INVISIBLE
	}

	override fun onPause() {
		super.onPause()
		store.postIntent(SubscribedIntent.StopListeningSubscribeChanges)
		networkChangeReceiver.unregister()
	}

	override fun onResume() {
		super.onResume()
		store.postIntent(SubscribedIntent.StartListeningSubscribeChanges)
		networkChangeReceiver = NetworkChangeReceiver(requireContext()) { isConnected ->
			handleNetworkChange(isConnected)
		}
		networkChangeReceiver.register()
	}

	private fun handleNetworkChange(isConnected: Boolean) {
		if (isConnected) {
			store.postIntent(SubscribedIntent.ReloadSubscribed)
		}
	}

	companion object {
		fun newInstance() = SubscribedFragment()
	}
}