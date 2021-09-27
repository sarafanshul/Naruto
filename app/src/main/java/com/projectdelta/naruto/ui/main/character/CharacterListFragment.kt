package com.projectdelta.naruto.ui.main.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import com.projectdelta.naruto.R
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
import timber.log.Timber


@AndroidEntryPoint
class CharacterListFragment : BaseViewBindingFragment<FragmentCharacterListBinding>() {

	companion object {
		@JvmStatic
		fun newInstance() = CharacterListFragment()
	}

	private val viewModel : CharacterViewModel by activityViewModels()

	private var adapter: CharacterListAdapter? = null

	private var job : Job? = null

	private var settingsSheet : CharacterSettingSheet? = null

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

		setMenu()
		setSearchBar()
		initMenu()

		adapter = CharacterListAdapter( object : BaseModelItemClickCallback{
			override fun onItemClick(item: BaseModel, itemCard: CardView) {
				navigateCharacterDetail(item as Character, itemCard as MaterialCardView)
			}
		} )
		binding.characterRv.layoutManager = LinearLayoutManager(requireActivity())

		binding.characterRv.adapter = adapter

		viewModel.data.observe(viewLifecycleOwner) { characters ->
			adapter?.submitData(viewLifecycleOwner.lifecycle, characters)
		}

		(requireActivity() as MainActivity).connectivityManager.isNetworkAvailable.observe(viewLifecycleOwner , connectionWatcher@{ x ->
			when(x){
				true -> onNetworkReconnect()
				false -> { }
			}
		})


	}

	private fun initMenu() {
		settingsSheet = CharacterSettingSheet(
			requireActivity() ,
			(requireActivity() as MainActivity).preferenceManager
		){ group ->
			when (group) {
				is CharacterSettingSheet.Filter.FilterGroup -> onFilterChanged()
				is CharacterSettingSheet.Sort.SortGroup -> onSortChanged()

			}
		}
	}

	private fun onSortChanged() {
//		binding.characterRv.scrollToPosition(0)
		viewModel.getUpdatePrefDataSort()
	}

	private fun onFilterChanged() {
		Timber.i("====filter changed=====")
	}

	private fun setSearchBar() {
		val searchView : SearchView? = binding.toolbar.menu.findItem(R.id.action_search).actionView as SearchView?
		searchView?.queryHint = "Hinata Hyuga"

		searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
			override fun onQueryTextSubmit(query: String?): Boolean {
				requireActivity().toast(query.orEmpty())

				searchView.clearFocus()
				return false
			}

			override fun onQueryTextChange(newText: String?): Boolean {
				return false
			}
		})
	}

	private fun setMenu() {
		binding.toolbar.setOnMenuItemClickListener {
			when(it.itemId){
				R.id.action_filter -> {
					settingsSheet?.show()
				}
				else -> {}
			}
			super.onOptionsItemSelected(it)
		}
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
		if(viewModel.data.hasActiveObservers())
			viewModel.data.removeObservers(viewLifecycleOwner)
		if(_binding != null)
			binding.characterRv.adapter = null
		super.onDestroy()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		settingsSheet = null
	}
}