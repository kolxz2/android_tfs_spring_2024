package ru.nikolas_snek.profile.presentation

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mvi.BaseFragmentMvi
import com.example.mvi.MviStore
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import ru.nikolas_snek.data.utils.UserStatus
import ru.nikolas_snek.data_di.AppDepsStore
import ru.nikolas_snek.profile.R
import ru.nikolas_snek.profile.databinding.FragmentProfileBinding
import ru.nikolas_snek.profile.di.DaggerProfileComponent
import ru.nikolas_snek.profile.domain.UserProfile
import ru.nikolas_snek.profile.presentation.mvi.ProfileEffect
import ru.nikolas_snek.profile.presentation.mvi.ProfileIntent
import ru.nikolas_snek.profile.presentation.mvi.ProfilePartialState
import ru.nikolas_snek.profile.presentation.mvi.ProfileState
import ru.nikolas_snek.ui_kit.R.*
import javax.inject.Inject

class ProfileFragment :
    BaseFragmentMvi<ProfilePartialState, ProfileState<UserProfile>, ProfileEffect, ProfileIntent>(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override val store: MviStore<
            ProfilePartialState,
            ProfileIntent,
            ProfileState<UserProfile>,
            ProfileEffect> by viewModels {
        viewModelFactory
    }
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

    override fun resolveEffect(effect: ProfileEffect) {

    }

    override fun render(state: ProfileState<UserProfile>) {
        when (state) {
            is ProfileState.DataLoaded -> showUserProfile(state.data)
            ProfileState.Error -> failureLoadingDataState()
            ProfileState.Loading -> startLoadingDataState()
        }
    }

    override fun onAttach(context: Context) {
        DaggerProfileComponent.builder().deps(AppDepsStore.appDependencies).build().inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            store.postIntent(ProfileIntent.Init)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.errorMessage.btReloadData.setOnClickListener {
            store.postIntent(ProfileIntent.Reload)
        }
    }

    private fun showUserProfile(userProfile: UserProfile) {
        binding.shimmerView.isVisible = false
        binding.errorMessage.root.isVisible = false
        binding.profileInfoLayout.root.isVisible = true
        binding.shimmerView.startShimmer()
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
        fun newInstance() = ProfileFragment()
    }
}