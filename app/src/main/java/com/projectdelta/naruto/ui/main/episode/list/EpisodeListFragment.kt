package com.projectdelta.naruto.ui.main.episode.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.slider.RangeSlider
import com.projectdelta.naruto.R
import com.projectdelta.naruto.databinding.FragmentEpisodeListBinding
import com.projectdelta.naruto.ui.base.BaseViewBindingFragment
import com.projectdelta.naruto.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EpisodeListFragment : BaseViewBindingFragment<FragmentEpisodeListBinding>() {

	companion object {
		@JvmStatic
		fun newInstance() = EpisodeListFragment()
	}

	private val viewModel : EpisodeViewModel by activityViewModels()

	private var adapter : EpisodeListAdapter? = null

	private var job : Job? = null

	private var searchSheet : MaterialDialog? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Access viewModel so that it gets initialized on the main thread
		viewModel

		adapter = EpisodeListAdapter()
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

		setMenu()

		binding.episodeRv.layoutManager = LinearLayoutManager(requireActivity())

		binding.episodeRv.adapter = adapter
		job = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
			viewModel.getChapterSortedPaged().collectLatest { episodes ->
				adapter?.submitData(episodes)
			}
		}

		(requireActivity() as MainActivity).connectivityManager.isNetworkAvailable.observe(viewLifecycleOwner , connectionWatcher@{x ->
			when(x){
				true -> {
					onNetworkReconnect()
				}
				false -> { }
			}
		})

		adapter?.addLoadStateListener { state ->
			binding.progressBar.isVisible = state.source.refresh is LoadState.Loading
			if( state.source.refresh is LoadState.NotLoading &&
				state.append.endOfPaginationReached && adapter?.itemCount!! < 1) {
				binding.emptyView.visibility = View.VISIBLE
				binding.episodeRv.isVisible = false
			}
			else {
				binding.emptyView.visibility = View.GONE
				binding.episodeRv.isVisible = true
			}
		}
	}

	private fun setMenu() {
		initMenu()
		binding.toolbar.setOnMenuItemClickListener {
			when(it.itemId){
				R.id.action_filter -> {
//					settingsSheet?.show()
				}
				R.id.action_search_range -> {
					// show range slide enu here
					searchSheet?.show()
				}
			}
			super.onOptionsItemSelected(it)
		}
	}

	private fun initMenu() {

		searchSheet = MaterialDialog(requireActivity(), BottomSheet(LayoutMode.WRAP_CONTENT)).apply {
			title(text = "Search")
			message(text = "Select range to filter episodes.")
			customView(R.layout.layout_search_range)
			cornerRadius(res = R.dimen.dialog_default_corner_radius)
		}

		setupRangeSheet(searchSheet?.getCustomView() as RangeSlider)

//		settingsSheet = EpisodeSettingSheet(
//			requireActivity() ,
//			(requireActivity() as MainActivity).preferenceManager
//		){ group ->
//			when (group) {
//				is CharacterSettingSheet.Filter.FilterGroup -> { }
//				is CharacterSettingSheet.Sort.SortGroup -> { }
//
//			}
//		}

	}

	private fun setupRangeSheet(rangeSlider: RangeSlider?) {
		rangeSlider?.setValues(0f ,100f)
	}

	private fun onNetworkReconnect() {
		adapter?.retry()
	}

	override fun onDestroyView() {
		binding.episodeRv.adapter = null
		super.onDestroyView()
	}

	override fun onDestroy() {
		job?.cancel()
		adapter = null
		super.onDestroy()
	}

}