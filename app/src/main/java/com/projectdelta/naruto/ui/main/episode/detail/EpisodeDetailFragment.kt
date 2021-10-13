package com.projectdelta.naruto.ui.main.episode.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projectdelta.naruto.databinding.FragmentEpisodeDetailBinding
import com.projectdelta.naruto.ui.base.BaseViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodeDetailFragment : BaseViewBindingFragment<FragmentEpisodeDetailBinding>() {

	companion object {

		@JvmStatic
		fun newInstance() = EpisodeDetailFragment()
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentEpisodeDetailBinding.inflate(inflater)
		return binding.root
	}

}