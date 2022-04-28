package com.projectdelta.naruto.ui.base

import androidx.viewbinding.ViewBinding

abstract class BaseViewBindingFragment<VB : ViewBinding> : BaseFragment() {

	protected var _binding: VB? = null
	val binding: VB
		get() = _binding!!

	override fun onDestroyView() {
		_binding = null
		super.onDestroyView()
	}

}
