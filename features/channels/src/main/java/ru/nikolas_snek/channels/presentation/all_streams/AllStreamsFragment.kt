package ru.nikolas_snek.channels.presentation.all_streams

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
import ru.nikolas_snek.channels.R
import ru.nikolas_snek.ui_kit.R as RUiKit
import ru.nikolas_snek.channels.databinding.FragmentAllStreamsBinding
import ru.nikolas_snek.channels.di.DaggerAllStreamsFragmentComponent
import ru.nikolas_snek.channels.domain.models.Stream
import ru.nikolas_snek.channels.presentation.SharedViewModel
import ru.nikolas_snek.channels.presentation.all_streams.recycler.AllStreamsAdapter
import ru.nikolas_snek.channels.presentation.mvi.AllStreamsEffect
import ru.nikolas_snek.channels.presentation.mvi.AllStreamsIntent
import ru.nikolas_snek.channels.presentation.mvi.AllStreamsPartialState
import ru.nikolas_snek.channels.presentation.mvi.AllStreamsState
import ru.nikolas_snek.channels.presentation.mvi.AllStreamsStore
import ru.nikolas_snek.data.utils.LoadingAttemptStatus
import ru.nikolas_snek.data_di.AppDepsStore
import ru.nikolas_snek.ui_kit.utils.showSnackbarWithAction
import javax.inject.Inject

class AllStreamsFragment :
	BaseFragmentMvi<AllStreamsPartialState, AllStreamsState, AllStreamsEffect, AllStreamsIntent>(R.layout.fragment_all_streams) {
	private val binding by viewBinding(FragmentAllStreamsBinding::bind)
	private lateinit var adapter: AllStreamsAdapter
	private var onStreamClickListener: ((Stream) -> Unit) = { stream ->
		lifecycleScope.launch {
			store.postIntent(AllStreamsIntent.ChangeSubscriptionStatus(stream))
			val position = adapter.currentList.indexOfFirst { it.streamId == stream.streamId }
			adapter.notifyItemChanged(position)
		}
	}
	private val sharedViewModel by activityViewModels<SharedViewModel>()

	@Inject
	lateinit var storeFactory: ViewModelProvider.Factory
	override val store: MviStore<
			AllStreamsPartialState,
			AllStreamsIntent,
			AllStreamsState,
			AllStreamsEffect> by viewModels(
		ownerProducer = { this },
		factoryProducer = { storeFactory }
	)

	override fun onAttach(context: Context) {
		DaggerAllStreamsFragmentComponent.builder().deps(AppDepsStore.appDependencies).build()
			.inject(this)
		super.onAttach(context)
		adapter = AllStreamsAdapter(onStreamClickListener)
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.rvAllStreams.adapter = adapter
		binding.errorMessage.btReloadData.setOnClickListener {
			store.postIntent(AllStreamsIntent.ReloadAllStreams)
		}

		sharedViewModel.allStreamsSearchQuery
			.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
			.onEach { query ->
				query?.let {
					(store as AllStreamsStore).searchQueryPublisher.emit(query)
				}
			}
			.launchIn(viewLifecycleOwner.lifecycleScope)
	}


	override fun resolveEffect(effect: AllStreamsEffect) {
		when (effect) {
			AllStreamsEffect.NetworkError -> showSnackbarWithAction(
				binding.root,
				getString(RUiKit.string.error_snackbar_message),
				getString(RUiKit.string.error_snackbar_action),
			) {
				store.postIntent(AllStreamsIntent.ReloadAllStreams)
			}
		}

	}

	override fun render(state: AllStreamsState) {
		when (state.loadingStatus) {
			LoadingAttemptStatus.FIRST_ATTEMPT -> store.postIntent(AllStreamsIntent.ReloadAllStreams)
			LoadingAttemptStatus.SUCCESS -> showStreams(state.listProfiles)
			LoadingAttemptStatus.LOADING -> startLoadingStreamsState()
			LoadingAttemptStatus.FAILURE -> failureLoadingDataState()
		}
	}

	override fun onPause() {
		super.onPause()
		store.postIntent(AllStreamsIntent.StopListeningSubscribeChanges)
	}

	override fun onResume() {
		super.onResume()
		store.postIntent(AllStreamsIntent.StartListeningSubscribeChanges)

	}

	private fun showStreams(stream: List<Stream>) {
		binding.shimmerList.root.isVisible = false
		binding.errorMessage.root.isVisible = false
		binding.rvAllStreams.isVisible = true
		adapter.submitList(stream)
	}

	private fun startLoadingStreamsState() {
		binding.shimmerList.root.isVisible = true
		binding.errorMessage.root.isVisible = false
		binding.rvAllStreams.isVisible = false
	}

	private fun failureLoadingDataState() {
		binding.shimmerList.root.isVisible = false
		binding.errorMessage.root.isVisible = true
		binding.rvAllStreams.isVisible = false
	}


	companion object {
		fun newInstance() = AllStreamsFragment()
	}
}