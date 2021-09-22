package com.projectdelta.naruto.ui.main.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectdelta.naruto.databinding.FragmentLocationListBinding
import com.projectdelta.naruto.ui.base.BaseViewBindingFragment
import com.projectdelta.naruto.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationListFragment : BaseViewBindingFragment<FragmentLocationListBinding>() {

	companion object {
		@JvmStatic
		fun newInstance() = LocationListFragment()
	}

	private val viewModel : LocationViewModel by activityViewModels()

	private var adapter : LocationListAdapter? = null

	private var job : Job? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Access viewModel so that it gets initialized on the main thread
		viewModel
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentLocationListBinding.inflate(layoutInflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initUI()
	}

	private fun initUI() {
		adapter = LocationListAdapter()
		binding.locationRv.layoutManager = LinearLayoutManager(requireActivity())

		binding.locationRv.adapter = adapter
		job = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
			viewModel.getLocationPaged().collectLatest { villages ->
				adapter?.submitData(villages)
			}
		}

		(requireActivity() as MainActivity).connectivityManager.isNetworkAvailable.observe(viewLifecycleOwner , connectionWatcher@{ x ->
			when(x){
				true -> {
					adapter?.retry()
				}
				false -> { }
			}
		})
	}

	override fun onDestroy() {
		job?.cancel()
		adapter = null
		if(_binding != null)
			binding.locationRv.adapter = null
		super.onDestroy()
	}

}