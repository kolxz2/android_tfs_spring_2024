package ru.nikolas_snek.chat.presentation

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mvi.BaseFragmentMvi
import com.example.mvi.MviStore
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.nikolas_snek.chat.R
import ru.nikolas_snek.ui_kit.R.color.colorPrimary as colorPrimary
import ru.nikolas_snek.ui_kit.R as RUiKit
import ru.nikolas_snek.chat.databinding.FragmentChatBinding
import ru.nikolas_snek.chat.di.DaggerChatFragmentComponent
import ru.nikolas_snek.chat.domain.models.Message
import ru.nikolas_snek.chat.domain.models.Reaction
import ru.nikolas_snek.chat.presentation.mvi.ChatEffect
import ru.nikolas_snek.chat.presentation.mvi.ChatIntent
import ru.nikolas_snek.chat.presentation.mvi.ChatPartialState
import ru.nikolas_snek.chat.presentation.mvi.ChatState
import ru.nikolas_snek.chat.presentation.chat_recycler.MessageAdapter
import ru.nikolas_snek.chat.presentation.chat_recycler.utils.DateHeaderDecoration
import ru.nikolas_snek.chat.presentation.chat_recycler.utils.EmojiCNCS
import ru.nikolas_snek.chat.utils.NetworkChangeReceiver
import ru.nikolas_snek.data.utils.LoadingAttemptStatus
import ru.nikolas_snek.data_di.AppDepsStore
import ru.nikolas_snek.ui_kit.utils.showSnackbarWithAction
import javax.inject.Inject

class ChatFragment :
	BaseFragmentMvi<ChatPartialState, ChatState, ChatEffect, ChatIntent>(R.layout.fragment_chat),
	EmojiPickerListener {
	private val binding by viewBinding(FragmentChatBinding::bind)

	private lateinit var networkChangeReceiver: NetworkChangeReceiver

	@Inject
	lateinit var storeFactory: ViewModelProvider.Factory
	override val store: MviStore<
			ChatPartialState,
			ChatIntent,
			ChatState,
			ChatEffect> by viewModels { storeFactory }
	private val streamTitle by lazy { requireArguments().getString(STREAM_TITLE) }
	private val topicTitle by lazy { requireArguments().getString(TOPIC_TITLE) }
	private var selectedMessageForSetEmoji: Message? = null
	private val adapter: MessageAdapter by lazy(LazyThreadSafetyMode.NONE) {
		MessageAdapter(
			onMessageLongClickListener,
			onEmojiClickListener
		)
	}
	private var onMessageLongClickListener: ((Message) -> Unit) = {
		if (selectedMessageForSetEmoji == null) {
			selectedMessageForSetEmoji = it
			val bottomSheetDialog = EmojiSheetDialog()
			bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
		}
	}
	private var onEmojiClickListener: ((messageId: Long, emoji: Reaction) -> Unit) =
		{ messageId, emoji ->
			val position = adapter.currentList.indexOfFirst { it.id == messageId }
			if (position != -1) {
				if (emoji.isUserSelect) {
					store.postIntent(ChatIntent.RemoveReaction(emoji.emojiName, messageId))
				} else {
					store.postIntent(ChatIntent.SetReaction(emoji.emojiName, messageId))
				}
			}
		}

	override fun onAttach(context: Context) {
		DaggerChatFragmentComponent.builder().deps(AppDepsStore.appDependencies).build()
			.inject(this)
		super.onAttach(context)
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		logicToShowSentMessageButton()
		setupMessageSending()
		initRecyclerView()
		setupUI()
		subscribeToStore()

	}

	private fun setupUI() {
		binding.tvStreamTitle.text = streamTitle
		binding.tvTopicTitle.text = getString(R.string.topic, topicTitle)
		binding.btGoBack.setOnClickListener {
			store.postIntent(ChatIntent.NavigateToGoBack)
		}

		val window: Window? = activity?.window
		window?.statusBarColor =
			ContextCompat.getColor(requireContext(), colorPrimary)
		binding.errorMessage.btReloadData.setOnClickListener {
			store.postIntent(ChatIntent.Reload(topicTitle!!, streamTitle!!))
		}
	}

	private fun subscribeToStore() {
		viewLifecycleOwner.lifecycleScope.launch {
			store.uiState.collect(::render)
		}
		viewLifecycleOwner.lifecycleScope.launch {
			store.effect.onEach { resolveEffect(it) }.launchIn(lifecycleScope)
		}
	}


	override fun resolveEffect(effect: ChatEffect) {
		when (effect) {
			is ChatEffect.NavigateToGoBack -> requireActivity().onBackPressedDispatcher.onBackPressed()
			ChatEffect.ErrorLoadMessages -> showSnackbarWithAction(
				binding.root,
				getString(RUiKit.string.error_snackbar_message),
				getString(RUiKit.string.error_snackbar_action),
			) {
				store.postIntent(ChatIntent.Reload(topicTitle!!, streamTitle!!))
			}

		}
	}

	override fun render(state: ChatState) {
		when (state.loadingStatus) {
			LoadingAttemptStatus.FIRST_ATTEMPT -> store.postIntent(
				ChatIntent.Reload(
					topicTitle!!,
					streamTitle!!
				)
			)

			LoadingAttemptStatus.SUCCESS -> showChat(state.listProfiles)
			LoadingAttemptStatus.LOADING -> startLoadingDataState()
			LoadingAttemptStatus.FAILURE -> failureLoadingDataState()
		}
	}

	private fun setupMessageSending() {
		binding.btSentMessage.setOnClickListener {
			val messageText = binding.etMessageContent.text.toString()
			if (messageText.isNotBlank()) {
				store.postIntent(
					ChatIntent.SendMessage(
						stream = streamTitle!!,
						topic = topicTitle!!,
						message = messageText
					)
				)
				binding.etMessageContent.text.clear()
				val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.8f)
				val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.8f)
				val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0.6f)
				val animator = ObjectAnimator.ofPropertyValuesHolder(it, scaleX, scaleY, alpha)
				animator.duration = 150
				animator.interpolator = AccelerateDecelerateInterpolator()
				animator.start()
			}
		}
	}

	override fun onPause() {
		super.onPause()
		store.postIntent(ChatIntent.StopListeningProfileListChanges)
		networkChangeReceiver.unregister()
	}

	override fun onResume() {
		super.onResume()
		store.postIntent(ChatIntent.StartListeningProfileListChanges(topicTitle!!, streamTitle!!))
		networkChangeReceiver = NetworkChangeReceiver(requireContext()) { isConnected ->
			handleNetworkChange(isConnected)
		}
		networkChangeReceiver.register()
	}

	private fun handleNetworkChange(isConnected: Boolean) {
		if (isConnected) {
			store.postIntent(ChatIntent.Reload(topicTitle!!, streamTitle!!))
		}
	}

	private fun initRecyclerView() {
		binding.rvMessages.adapter = adapter
		val layoutManager = LinearLayoutManager(requireContext())
		layoutManager.stackFromEnd = true
		binding.rvMessages.layoutManager = layoutManager
		binding.rvMessages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				super.onScrolled(recyclerView, dx, dy)
				val layoutManager = recyclerView.layoutManager as LinearLayoutManager
				val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
				if (firstVisibleItemPosition == 0 || firstVisibleItemPosition == 5) {
					store.postIntent(
						ChatIntent.GetOldTopicMessages(topicTitle!!, streamTitle!!)
					)
				}
			}
		})
		val dividerItemDecoration = DateHeaderDecoration(adapter, requireContext(), 120)
		binding.rvMessages.addItemDecoration(dividerItemDecoration)
	}

	private fun showChat(profiles: List<Message>) {
		binding.pdChat.isVisible = false
		binding.errorMessage.root.isVisible = false
		binding.rvMessages.isVisible = true
		val lastAdapterMessage = adapter.currentList.lastOrNull()
		val lastMessageFromNewTopicMessages = profiles.lastOrNull()
		adapter.submitList(profiles) {
			lastAdapterMessage?.let { adapterMessage ->
				lastMessageFromNewTopicMessages?.let { repositoryMessage ->
					if (repositoryMessage.isMeMessage && lastAdapterMessage.id != repositoryMessage.id) {
						binding.rvMessages.scrollToPosition(adapter.currentList.size - 1)
					}
				}
			}
		}

	}

	private fun startLoadingDataState() {
		binding.pdChat.isVisible = true
		binding.errorMessage.root.isVisible = false
		binding.rvMessages.isVisible = false
	}

	private fun failureLoadingDataState() {
		binding.pdChat.isVisible = false
		binding.errorMessage.root.isVisible = true
		binding.rvMessages.isVisible = false
	}

	override fun onSelectEmoji(emoji: EmojiCNCS) {
		val position = adapter.currentList.indexOfFirst { it.id == selectedMessageForSetEmoji?.id }
		if (position != -1) {
			selectedMessageForSetEmoji?.let {
				store.postIntent(
					ChatIntent.SetReaction(
						emoji.name,
						it.id
					)
				)
			}
		}
	}

	override fun onDestroy() {
		super.onDestroy()
	}

	override fun onEmojiPickerDismissed() {
		selectedMessageForSetEmoji = null
	}

	private fun logicToShowSentMessageButton() {
		binding.etMessageContent.doOnTextChanged { s, _, _, _ ->
			val isTextEmpty = s.isNullOrBlank()
			binding.btSentFile.isVisible = isTextEmpty
			binding.btSentMessage.isVisible = !isTextEmpty
		}
	}

	companion object {
		private const val STREAM_TITLE = "STREAM_TITLE"
		private const val TOPIC_TITLE = "TOPIC_TITLE"

		fun newInstance(streamTitle: String, topicTitle: String) = ChatFragment().apply {
			arguments = Bundle().apply {
				putString(STREAM_TITLE, streamTitle)
				putString(TOPIC_TITLE, topicTitle)
			}
		}
	}
}
