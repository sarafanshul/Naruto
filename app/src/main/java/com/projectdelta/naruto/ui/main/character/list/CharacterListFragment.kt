package com.projectdelta.naruto.ui.main.character.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import com.google.android.material.transition.MaterialElevationScale
import com.projectdelta.naruto.R
import com.projectdelta.naruto.data.model.entity.BaseModel
import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.databinding.FragmentCharacterListBinding
import com.projectdelta.naruto.ui.base.BaseViewBindingFragment
import com.projectdelta.naruto.ui.main.MainActivity
import com.projectdelta.naruto.util.Constants.TRANSITION_CHARACTER
import com.projectdelta.naruto.util.callback.BaseModelItemClickCallback
import com.projectdelta.naruto.util.system.lang.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CharacterListFragment : BaseViewBindingFragment<FragmentCharacterListBinding>() {

	companion object {
		@JvmStatic
		fun newInstance() = CharacterListFragment()
	}

	private val viewModel : CharacterViewModel by activityViewModels()

	private var adapter: CharacterListAdapter? = null

	private var settingsSheet : CharacterSettingSheet? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Access viewModel so that it gets initialized on the main thread
		viewModel

		adapter = CharacterListAdapter( object : BaseModelItemClickCallback{
			override fun onItemClick(item: BaseModel, itemCard: CardView) {
				navigateCharacterDetail(item as Character, itemCard as MaterialCardView)
			}
		} )
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentCharacterListBinding.inflate(layoutInflater)
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

		binding.characterRv.layoutManager = LinearLayoutManager(requireActivity())

		binding.characterRv.adapter = adapter

		viewModel.data.observe(viewLifecycleOwner ,{
			adapter?.submitData( viewLifecycleOwner.lifecycle , it )
		})

		(requireActivity() as MainActivity).connectivityManager.isNetworkAvailable.observe(viewLifecycleOwner , connectionWatcher@{ x ->
			when(x){
				true -> onNetworkReconnect()
				false -> { }
			}
		})

		adapter?.addLoadStateListener { state ->
			binding.progressBar.isVisible = state.source.refresh is LoadState.Loading
			if( state.source.refresh is LoadState.NotLoading &&
				state.append.endOfPaginationReached && adapter?.itemCount!! < 1) {
				binding.emptyView.visibility = View.VISIBLE
				binding.characterRv.isVisible = false
			}
			else {
				binding.emptyView.visibility = View.GONE
				binding.characterRv.isVisible = true
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
				else -> {}
			}
			super.onOptionsItemSelected(it)
		}
	}

	private fun initMenu() {
		setSearchBar()
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
		viewModel.getUpdatePrefDataSort()
	}

	private fun onFilterChanged() {
		viewModel.triggerFilters()
	}

	/*
	For now Search disables sort and filter
	 */
	private fun setSearchBar() {
		val searchView : SearchView? = binding.toolbar.menu.findItem(R.id.action_search).actionView as SearchView?
		searchView?.queryHint = "Hinata Hyuga"

		searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
			override fun onQueryTextSubmit(query: String?): Boolean {
				searchView.clearFocus()
				return false
			}

			override fun onQueryTextChange(newText: String?): Boolean {
				viewModel.updateQuery(newText.orEmpty().trim())
				return false
			}
		})

		// refer : https://stackoverflow.com/a/48989340/11718077
		binding.toolbar.menu.findItem(R.id.action_search).setOnActionExpandListener(
			object : MenuItem.OnActionExpandListener{
				override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
					binding.toolbar.menu.findItem(R.id.action_filter).isEnabled = false
					binding.toolbar.menu.findItem(R.id.action_filter).icon.alpha = 130
					return true
				}

				override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
					viewModel.updateQuery("")
					binding.toolbar.menu.findItem(R.id.action_filter).isEnabled = true
					binding.toolbar.menu.findItem(R.id.action_filter).icon.alpha = 255
					return true
				}
			}
		)
	}

	private fun onNetworkReconnect() {
		adapter?.retry()
	}

	private fun navigateCharacterDetail(character: Character, card: MaterialCardView) {
		val extras = FragmentNavigatorExtras(
			card to TRANSITION_CHARACTER.plus(character.id)
		)
		val action =
			CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment(
				character
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
		binding.characterRv.adapter = null
		settingsSheet = null
		super.onDestroyView()
	}

	override fun onDestroy() {
		adapter = null
		super.onDestroy()
	}
}