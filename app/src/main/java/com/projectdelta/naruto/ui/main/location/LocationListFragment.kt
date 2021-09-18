package com.projectdelta.naruto.ui.main.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projectdelta.naruto.databinding.FragmentLocationListBinding
import com.projectdelta.naruto.ui.base.BaseViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationListFragment : BaseViewBindingFragment<FragmentLocationListBinding>() {
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentLocationListBinding.inflate(layoutInflater)
		return binding.root
	}

	companion object {
		@JvmStatic
		fun newInstance(param1: String, param2: String) =
			LocationListFragment()
	}
}