package com.projectdelta.naruto.ui.main.episode.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.slider.RangeSlider
import com.google.android.material.transition.MaterialElevationScale
import com.projectdelta.naruto.R
import com.projectdelta.naruto.data.model.entity.BaseModel
import com.projectdelta.naruto.data.model.entity.chapter.Chapter
import com.projectdelta.naruto.data.model.entity.chapter.Chapter.Companion.MAX_EPISODE_NUMBER
import com.projectdelta.naruto.databinding.FragmentEpisodeListBinding
import com.projectdelta.naruto.ui.base.BaseViewBindingFragment
import com.projectdelta.naruto.ui.main.MainActivity
import com.projectdelta.naruto.util.Constants.TRANSITION_EPISODE
import com.projectdelta.naruto.util.callback.BaseModelItemClickCallback
import com.projectdelta.naruto.util.system.lang.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EpisodeListFragment : BaseViewBindingFragment<FragmentEpisodeListBinding>() {

	companion object {
		@JvmStatic
		fun newInstance() = EpisodeListFragment()
	}

	private val viewModel : EpisodeViewModel by activityViewModels()

	private var adapter : EpisodeListAdapter? = null

	private var searchSheet : MaterialDialog? = null
	private var settingsSheet : EpisodeSettingSheet? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Access viewModel so that it gets initialized on the main thread
		viewModel

		adapter = EpisodeListAdapter( object : BaseModelItemClickCallback{
			override fun onItemClick(item: BaseModel, itemCard: CardView) {
				navigateEpisodeDetail( item as Chapter ,itemCard )
			}
		})
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentEpisodeListBinding.inflate(layoutInflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		view.doOnPreDraw {
			startPostponedEnterTransition()
		}
		prepareTransitions()

		initUI()

	}

	private fun initUI() {

		setMenu()

		binding.episodeRv.layoutManager = LinearLayoutManager(requireActivity())

		binding.episodeRv.adapter = adapter
		viewModel.data.observe(viewLifecycleOwner, {data ->
			adapter?.submitData(viewLifecycleOwner.lifecycle ,data)
		})

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
					settingsSheet?.show()
				}
				R.id.action_search_range -> {
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

		settingsSheet = EpisodeSettingSheet(
			requireActivity() ,
			(requireActivity() as MainActivity).preferenceManager
		){ group ->
			when (group) {
				is EpisodeSettingSheet.Filter.FilterGroup -> { onFilterChanged() }
				is EpisodeSettingSheet.Sort.SortGroup -> { onSortChanged() }
			}
		}

	}

	private fun onSortChanged() {
		viewModel.onSortChanged()
	}

	private fun onFilterChanged() {
		viewModel.onFilterChanged()
	}

	private fun setupRangeSheet(rangeSlider: RangeSlider?) {
		if( rangeSlider == null )
			return
		with(rangeSlider){

			setValues(viewModel.rangeEpSt.toFloat() ,viewModel.rangeEpEd.toFloat())
			valueFrom = 0f
			valueTo = MAX_EPISODE_NUMBER.toFloat()
			stepSize = 20f

			setLabelFormatter { value ->
				when( value.toInt() ){
					MAX_EPISODE_NUMBER -> "End"
					0 -> "Start"
					else -> value.toInt().toString()
				}
			}

			addOnSliderTouchListener( object : RangeSlider.OnSliderTouchListener{
				override fun onStartTrackingTouch(slider: RangeSlider) {}

				override fun onStopTrackingTouch(slider: RangeSlider) {
					viewModel.updateEpisodeRange(slider.values)
				}
			})
		}
	}

	private fun navigateEpisodeDetail(chapter: Chapter, card: CardView) {
		val extras = FragmentNavigatorExtras(
			card to TRANSITION_EPISODE
		)
		val action =
			EpisodeListFragmentDirections.actionEpisodeListFragmentToEpisodeDetailFragment(
				chapter
			)
		safeNavigate(action ,extras)
	}

	private fun prepareTransitions() {
		if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
			postponeEnterTransition()
			exitTransition = MaterialElevationScale(false).apply {
				duration = resources.getInteger(R.integer.rm_motion_default_large).toLong()
			}
			reenterTransition = MaterialElevationScale(true).apply {
				duration = resources.getInteger(R.integer.rm_motion_default_large).toLong()
			}
		}
	}

	private fun onNetworkReconnect() {
		adapter?.retry()
	}

	override fun onDestroyView() {
		binding.episodeRv.adapter = null
		searchSheet = null
		settingsSheet = null
		super.onDestroyView()
	}

	override fun onDestroy() {
		adapter = null
		super.onDestroy()
	}

}