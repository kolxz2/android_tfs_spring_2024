package com.example.mvi

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

abstract class BaseFragmentMvi<
		PartialState : MviPartialState,
		State : MviState,
		Effect : MviEffect,
		Intent : MviIntent>(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {
	protected abstract val store: MviStore<PartialState, Intent, State, Effect>

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewLifecycleOwner.lifecycleScope.launch {
			store.uiState.collect(::render)
		}
		viewLifecycleOwner.lifecycleScope.launch {
			store.effect.collect(::resolveEffect)
		}
	}

	protected abstract fun render(state: State)

	protected abstract fun resolveEffect(effect: Effect)

}