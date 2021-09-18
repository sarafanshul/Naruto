package com.projectdelta.naruto.ui.base

import androidx.viewbinding.ViewBinding

abstract class BaseViewBindingActivity<VB : ViewBinding> : BaseActivity() {

	protected var _binding: VB? = null
	val binding: VB
		get() = _binding!!

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}

}