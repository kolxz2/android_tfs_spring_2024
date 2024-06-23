package ru.nikolas_snek.channels.presentation.subscribed.recycler.utils

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface ListStreamsItemDelegate {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: SubscribedRecyclerItems)
}