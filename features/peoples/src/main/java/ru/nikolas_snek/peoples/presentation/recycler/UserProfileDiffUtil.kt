package ru.nikolas_snek.peoples.presentation.recycler

import androidx.recyclerview.widget.DiffUtil
import ru.nikolas_snek.peoples.domain.Profile

class UserProfileDiffUtil : DiffUtil.ItemCallback<Profile>() {
    override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem == newItem
    }
}