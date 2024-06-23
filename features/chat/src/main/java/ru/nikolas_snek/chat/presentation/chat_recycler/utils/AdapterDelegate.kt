package ru.nikolas_snek.chat.presentation.chat_recycler.utils

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nikolas_snek.chat.domain.models.Message

interface AdapterDelegate {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Message)
}