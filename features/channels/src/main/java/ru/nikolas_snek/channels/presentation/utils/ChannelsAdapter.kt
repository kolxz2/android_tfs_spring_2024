package ru.nikolas_snek.channels.presentation.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ChannelsAdapter(
    fragmentActivity: FragmentManager,
    lifecycle: Lifecycle,
    private val fragmentsList: List<Fragment>
) : FragmentStateAdapter(fragmentActivity, lifecycle) {
    override fun getItemCount(): Int {
        return fragmentsList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentsList[position]
    }
}