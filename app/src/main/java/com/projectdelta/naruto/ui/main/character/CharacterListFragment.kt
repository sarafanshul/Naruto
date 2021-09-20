package com.projectdelta.naruto.ui.main.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectdelta.naruto.databinding.FragmentCharacterListBinding
import com.projectdelta.naruto.ui.base.BaseViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class CharacterListFragment : BaseViewBindingFragment<FragmentCharacterListBinding>() {

	companion object {
		@JvmStatic
		fun newInstance() = CharacterListFragment()
	}

	private val viewModel : CharacterViewModel by activityViewModels()

	private lateinit var adapter: CharacterListAdapter

	private var job : Job? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Access viewModel so that it gets initialized on the main thread
		viewModel
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		_binding = FragmentCharacterListBinding.inflate(layoutInflater)
		return binding.root
	}

	@InternalCoroutinesApi
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initUI()

	}

	private fun initUI() {
		adapter = CharacterListAdapter()
		binding.characterRv.layoutManager = LinearLayoutManager(requireActivity())

		binding.characterRv.adapter = adapter

		job = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
			viewModel.characterDataByPowerPaged().collectLatest { characters ->
				adapter.submitData( characters )
			}
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		job?.cancel()
	}
}