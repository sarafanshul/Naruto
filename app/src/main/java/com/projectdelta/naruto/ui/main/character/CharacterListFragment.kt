package com.projectdelta.naruto.ui.main.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import com.projectdelta.naruto.data.model.entity.BaseModel
import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.databinding.FragmentCharacterListBinding
import com.projectdelta.naruto.ui.base.BaseViewBindingFragment
import com.projectdelta.naruto.ui.main.MainActivity
import com.projectdelta.naruto.util.callback.BaseModelItemClickCallback
import com.projectdelta.naruto.util.system.lang.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class CharacterListFragment : BaseViewBindingFragment<FragmentCharacterListBinding>() {

	companion object {
		@JvmStatic
		fun newInstance() = CharacterListFragment()
	}

	private val viewModel : CharacterViewModel by activityViewModels()

	private var adapter: CharacterListAdapter? = null

	private var job : Job? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Access viewModel so that it gets initialized on the main thread
		viewModel
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentCharacterListBinding.inflate(layoutInflater)
		return binding.root
	}

	@InternalCoroutinesApi
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initUI()

	}

	private fun initUI() {
		adapter = CharacterListAdapter( object : BaseModelItemClickCallback{
			override fun onItemClick(item: BaseModel, itemCard: CardView) {
				navigateCharacterDetail(item as Character, itemCard as MaterialCardView)
			}
		} )
		binding.characterRv.layoutManager = LinearLayoutManager(requireActivity())

		binding.characterRv.adapter = adapter

		job = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
			viewModel.characterDataByPowerPaged().collectLatest { characters ->
				adapter?.submitData( characters )
			}
		}

		(requireActivity() as MainActivity).connectivityManager.isNetworkAvailable.observe(viewLifecycleOwner , connectionWatcher@{ x ->
			when(x){
				true -> onNetworkReconnect()
				false -> { }
			}
		})
	}

	private fun onNetworkReconnect() {
		adapter?.retry()
	}

	private fun navigateCharacterDetail(character: Character, card: MaterialCardView) {
		requireActivity().toast("${character.name?.english} clicked")
	}

	override fun onDestroy() {
		job?.cancel()
		adapter = null
		if(_binding != null)
			binding.characterRv.adapter = null
		super.onDestroy()
	}
}