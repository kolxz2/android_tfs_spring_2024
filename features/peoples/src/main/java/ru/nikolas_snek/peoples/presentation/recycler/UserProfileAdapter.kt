package ru.nikolas_snek.peoples.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.nikolas_snek.peoples.databinding.ProfileItemBinding
import ru.nikolas_snek.peoples.domain.Profile

class UserProfileAdapter : ListAdapter<Profile, UserProfileHolder>(UserProfileDiffUtil()) {
    var onProfileItemClickListener: ((Profile) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileHolder {
        val binding = ProfileItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UserProfileHolder(binding)
    }

    override fun onBindViewHolder(holder: UserProfileHolder, position: Int) {
        val profile = getItem(position)
        holder.bind(profile)
        holder.binding.root.setOnClickListener {
            onProfileItemClickListener?.invoke(profile)
        }
    }
}