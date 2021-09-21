package com.projectdelta.naruto.ui.main.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectdelta.naruto.databinding.FragmentEpisodeListBinding
import com.projectdelta.naruto.ui.base.BaseViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class EpisodeListFragment : BaseViewBindingFragment<FragmentEpisodeListBinding>() {

	companion object {
		@JvmStatic
		fun newInstance() = EpisodeListFragment()
	}

	private val viewModel : EpisodeViewModel by activityViewModels()

	private lateinit var adapter : EpisodeListAdapter

	private var job : Job? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Access viewModel so that it gets initialized on the main thread
		viewModel
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentEpisodeListBinding.inflate(layoutInflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initUI()

	}

	private fun initUI() {
		adapter = EpisodeListAdapter()
		binding.episodeRv.layoutManager = LinearLayoutManager(requireActivity())

		binding.episodeRv.adapter = adapter
		job = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
			viewModel.getChapterSortedPaged().collectLatest { episodes ->
				adapter.submitData(episodes)
			}
		}
	}

	override fun onDestroy() {
		job?.cancel()
		if(_binding != null)
			binding.episodeRv.adapter = null
		super.onDestroy()
	}

}