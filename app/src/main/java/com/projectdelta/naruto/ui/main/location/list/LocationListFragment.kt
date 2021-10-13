package com.projectdelta.naruto.ui.main.location.list

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
import com.google.android.material.transition.MaterialElevationScale
import com.projectdelta.naruto.R
import com.projectdelta.naruto.data.model.entity.BaseModel
import com.projectdelta.naruto.data.model.entity.location.Village
import com.projectdelta.naruto.databinding.FragmentLocationListBinding
import com.projectdelta.naruto.ui.base.BaseViewBindingFragment
import com.projectdelta.naruto.ui.main.MainActivity
import com.projectdelta.naruto.util.Constants.TRANSITION_LOCATION
import com.projectdelta.naruto.util.callback.BaseModelItemClickCallback
import com.projectdelta.naruto.util.system.lang.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationListFragment : BaseViewBindingFragment<FragmentLocationListBinding>() {

	companion object {
		@JvmStatic
		fun newInstance() = LocationListFragment()
	}

	private val viewModel : LocationViewModel by activityViewModels()

	private var adapter : LocationListAdapter? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Access viewModel so that it gets initialized on the main thread
		viewModel

		adapter = LocationListAdapter( object : BaseModelItemClickCallback{
			override fun onItemClick(item: BaseModel, itemCard: CardView) {
				navigateVillageDetail(item as Village ,itemCard)
			}
		})
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentLocationListBinding.inflate(layoutInflater)
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
		binding.locationRv.layoutManager = LinearLayoutManager(requireActivity())

		binding.locationRv.adapter = adapter

		viewModel.getLocationPaged().observe(viewLifecycleOwner ,{
			adapter?.submitData(viewLifecycleOwner.lifecycle ,it)
		})

		adapter?.addLoadStateListener { state ->
			binding.progressBar.isVisible = state.source.refresh is LoadState.Loading
			if( state.source.refresh is LoadState.NotLoading &&
				state.append.endOfPaginationReached && adapter?.itemCount!! < 1) {
				binding.emptyView.visibility = View.VISIBLE
				binding.locationRv.isVisible = false
			}
			else {
				binding.emptyView.visibility = View.GONE
				binding.locationRv.isVisible = true
			}
		}

		(requireActivity() as MainActivity).connectivityManager.isNetworkAvailable.observe(viewLifecycleOwner , connectionWatcher@{ x ->
			when(x){
				true -> {
					onNetworkReconnect()
				}
				false -> { }
			}
		})
	}

	private fun onNetworkReconnect() {
		adapter?.retry()
	}

	private fun navigateVillageDetail(village: Village, card: CardView) {
		val extras = FragmentNavigatorExtras(
			card to TRANSITION_LOCATION.plus(village.id)
		)
		val action =
			LocationListFragmentDirections.actionLocationListFragmentToLocationDetailFragment(
			village
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

	override fun onDestroyView() {
		binding.locationRv.adapter = null
		super.onDestroyView()
	}

	override fun onDestroy() {
		adapter = null
		super.onDestroy()
	}

}