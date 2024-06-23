package ru.nikolas_snek.another_people.presentation

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mvi.BaseFragmentMvi
import com.example.mvi.MviStore
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import ru.nikolas_snek.another_people.R
import ru.nikolas_snek.another_people.databinding.FragmentAnotherPeopleBinding
import ru.nikolas_snek.another_people.di.DaggerAnotherPeopleFragmentComponent
import ru.nikolas_snek.another_people.domain.UserProfile
import ru.nikolas_snek.another_people.presentation.mvi.AnotherPeopleEffect
import ru.nikolas_snek.another_people.presentation.mvi.AnotherPeopleIntent
import ru.nikolas_snek.another_people.presentation.mvi.AnotherPeoplePartialState
import ru.nikolas_snek.another_people.presentation.mvi.AnotherPeopleState
import ru.nikolas_snek.data.utils.UserStatus
import ru.nikolas_snek.data_di.AppDepsStore
import ru.nikolas_snek.ui_kit.R.*
import javax.inject.Inject
import ru.nikolas_snek.ui_kit.R.color.secondBackground as secondBackgroundColor

class AnotherPeopleFragment :
	BaseFragmentMvi<AnotherPeoplePartialState, AnotherPeopleState<UserProfile>, AnotherPeopleEffect, AnotherPeopleIntent>(
		R.layout.fragment_another_people
	) {
	private val binding by viewBinding(FragmentAnotherPeopleBinding::bind)

	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory
	override val store: MviStore<
			AnotherPeoplePartialState,
			AnotherPeopleIntent,
			AnotherPeopleState<UserProfile>,
			AnotherPeopleEffect> by viewModels {
		viewModelFactory
	}
	private val userId: Int by lazy { requireArguments().getInt(USER_ID, -1) }
	private val transformation = object : Transformation {
		override fun transform(source: Bitmap): Bitmap {
			val width = source.width
			val height = source.height
			val size = minOf(width, height)
			val result =
				Bitmap.createBitmap(source, (width - size) / 2, (height - size) / 2, size, size)
			if (result != source) {
				source.recycle()
			}
			return result
		}

		override fun key(): String {
			return "square()"
		}
	}

	override fun onAttach(context: Context) {
		DaggerAnotherPeopleFragmentComponent.builder().deps(AppDepsStore.appDependencies).build().inject(this)
		super.onAttach(context)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (savedInstanceState == null) {
			store.postIntent(AnotherPeopleIntent.Init(userId))
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.btGoBack.setOnClickListener {
			store.postIntent(AnotherPeopleIntent.OnBackClicked)
		}
		binding.errorMessage.btReloadData.setOnClickListener {
			store.postIntent(AnotherPeopleIntent.Reload(userId))
		}
	}

	override fun resolveEffect(effect: AnotherPeopleEffect) {
		when (effect) {
			is AnotherPeopleEffect.NavigateGoBack -> requireActivity().onBackPressedDispatcher.onBackPressed()
		}
	}

	override fun render(state: AnotherPeopleState<UserProfile>) {
		when (state) {
			is AnotherPeopleState.DataLoaded -> showUserProfile(state.userProfileData)
			AnotherPeopleState.Error -> failureLoadingDataState()
			AnotherPeopleState.Loading -> startLoadingDataState()
		}
	}

	private fun showUserProfile(userProfile: UserProfile) {
		binding.shimmerView.isVisible = false
		binding.errorMessage.root.isVisible = false
		binding.profileInfoLayout.root.isVisible = true
		binding.profileInfoLayout.tvUserName.text = userProfile.userName
		binding.profileInfoLayout.tvOnlineStatus.text = userProfile.userStatus.textStatus
		val statusColor = when (userProfile.userStatus) {
			UserStatus.OFFLINE -> ContextCompat.getColor(
				requireContext(),
				color.offline
			)

			UserStatus.ACTIVE -> ContextCompat.getColor(
				requireContext(),
				color.online
			)

			UserStatus.IDLE -> ContextCompat.getColor(
				requireContext(),
				color.idle
			)
		}
		binding.profileInfoLayout.tvOnlineStatus.setTextColor(statusColor)
		if (userProfile.userPhoto.isNotBlank()) {
			Picasso.get().load(userProfile.userPhoto).transform(transformation)
				.into(binding.profileInfoLayout.ivProfilePhoto)
		}
	}

	private fun startLoadingDataState() {
		binding.shimmerView.isVisible = true
		binding.errorMessage.root.isVisible = false
		binding.profileInfoLayout.root.isVisible = false
	}

	private fun failureLoadingDataState() {
		binding.shimmerView.isVisible = false
		binding.errorMessage.root.isVisible = true
		binding.profileInfoLayout.root.isVisible = false
	}

	companion object {
		private const val USER_ID = "USER_ID"
		fun newInstance(userId: Int) = AnotherPeopleFragment().apply {
			arguments = Bundle().apply {
				putInt(USER_ID, userId)
			}
		}
	}
}