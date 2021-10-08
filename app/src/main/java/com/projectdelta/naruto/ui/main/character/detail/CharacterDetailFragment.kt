package com.projectdelta.naruto.ui.main.character.detail

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import com.projectdelta.naruto.R
import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.databinding.FragmentCharacterDetailBinding
import com.projectdelta.naruto.ui.base.BaseViewBindingFragment
import com.projectdelta.naruto.ui.main.MainActivity
import com.projectdelta.naruto.util.CollapsingToolbarState
import com.projectdelta.naruto.util.Constants.COLLAPSING_TOOLBAR_VISIBILITY_THRESHOLD
import com.projectdelta.naruto.util.Constants.TRANSITION_CHARACTER
import com.projectdelta.naruto.util.NotFound
import com.projectdelta.naruto.util.networking.ApiConstants.FANDOM_URL
import com.projectdelta.naruto.util.system.lang.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * (for collapsing toolbar)[https://stackoverflow.com/questions/40472680/add-collapsing-toolbar-with-image]
 */
@AndroidEntryPoint
class CharacterDetailFragment : BaseViewBindingFragment<FragmentCharacterDetailBinding>() {

	companion object {
		@JvmStatic
		fun newInstance() = CharacterDetailFragment()
	}

	private val viewModel : CharacterDetailViewModel by viewModels()

	lateinit var character : Character

	private var jutsuListAdapter : JutsuListAdapter? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		val args: CharacterDetailFragmentArgs by navArgs()
		character = args.characterData

		jutsuListAdapter = JutsuListAdapter()

		jobs += viewLifecycleOwner.lifecycleScope.launch {
			viewModel.setJutsus(character.id)
		}

		_binding = FragmentCharacterDetailBinding.inflate(layoutInflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		view.doOnPreDraw {
			startPostponedEnterTransition()
		}

		prepareTransitions()

		initUI()

		subscribeObservers()

		viewModel.jutsuList.observe(viewLifecycleOwner ,{ data ->
			jutsuListAdapter?.submitList(data)
		})

	}

	private fun initUI() {

		binding.apply {
			layoutCharacterDetail.transitionName = TRANSITION_CHARACTER.plus(character.id)

			appBar.addOnOffsetChangedListener(
				AppBarLayout.OnOffsetChangedListener{ _ ,offset ->
					if (offset < COLLAPSING_TOOLBAR_VISIBILITY_THRESHOLD) {
						viewModel.setCollapsingToolbarState(CollapsingToolbarState.Collapsed())
					} else {
						viewModel.setCollapsingToolbarState(CollapsingToolbarState.Expanded())
					}
				}
			)

			// jutsus
			characterJutsusRv.layoutManager = LinearLayoutManager(requireActivity())
			characterJutsusRv.adapter = jutsuListAdapter

			Glide
				.with(this@CharacterDetailFragment)
				.load(character.images?.first())
				.apply(
					RequestOptions()
						.placeholder(R.drawable.placeholder_white_leaf)
						.diskCacheStrategy(DiskCacheStrategy.DATA)
				)
				.thumbnail(0.25f)
				.into(characterImage)

			characterName.text = "${character.name?.english}\n${character.name?.kanji}"

			toolbarPrimaryIcon.setOnClickListener {
				findNavController().navigateUp()
			}

			toolbarSecondaryIcon.setOnClickListener {
				launchWebView()
			}

			// status
			characterStatus.text = character.personal?.status
			if( character.personal?.status != Character.Companion.CharacterStatus.ALIVE.value ){
				characterStatus.compoundDrawables.first().setTint(Color.RED)
				characterStatus.setTextColor(Color.RED)
			}

			// birth
			characterBirth.text = character.personal?.birthDate
			characterBirth.isSelected = true

			// sex
			characterSex.text = character.personal?.sex
			when(character.personal?.sex){
				"Male" -> characterSex.leftDrawable(R.drawable.ic_male_black_24dp)
				"Female" -> characterSex.leftDrawable(R.drawable.ic_female_black_24dp)
				else -> characterSex.leftDrawable(R.drawable.ic_transgender_black_24dp)
			}

			// description
			characterDescription.text = character.description

			// debut
			characterDebut.text = "Manga : ${character.debut?.manga?.name} #${character.debut?.manga?.chapter}" + "\n" +
					"Anime : ${character.debut?.anime?.name} #${character.debut?.anime?.episode}"

			// clan
			characterClan.text = if(character.personal?.clan?.first().isOk()) character.personal?.clan?.first() else "Rogue Ninja"

			// village
			characterVillage.text = if(character.personal?.affiliation?.first().isOk()) character.personal?.affiliation?.first() else "Traveller"

		}
	}

	/**
	 *  ref [1](https://www.youtube.com/watch?v=KuV8Y9-T-oA) ,
	 *  [2](https://github.com/AlexSheva-mason/Rick-Morty-Database/blob/b94ffac84abf765dbc6136d7c6303d44fdd8f642/app/src/main/java/com/shevaalex/android/rickmortydatabase/ui/base/BaseDetailFragment.kt#L232)
	 */
	private fun prepareTransitions() {
		if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
			postponeEnterTransition()
			val animation = MaterialContainerTransform().apply {
				drawingViewId = R.id.nav_host_fragment
				duration = resources.getInteger(R.integer.rm_motion_default_large).toLong()
				scrimColor = Color.TRANSPARENT
			}
			sharedElementEnterTransition = animation
			sharedElementReturnTransition = animation
		} else {
			enterTransition = MaterialFadeThrough().apply {
				duration = resources.getInteger(R.integer.rm_motion_default_large).toLong()
			}
		}
	}

	private fun subscribeObservers() {
		viewModel.collapsingToolbarState.observe(viewLifecycleOwner, { state ->
			when (state) {
				is CollapsingToolbarState.Expanded -> {
					transitionToExpandedMode()
				}
				is CollapsingToolbarState.Collapsed -> {
					transitionToCollapsedMode()
				}
			}
		})
	}

	private fun launchWebView() {
		(requireActivity() as MainActivity).launchWebView( FANDOM_URL + character.id)
	}

	private fun transitionToExpandedMode() {
		binding.toolBarTitle.fadeIn()
		displayToolbarTitle(binding.toolBarTitle, null, true)
	}

	private fun transitionToCollapsedMode() {
		binding.toolBarTitle.fadeOut()
		displayToolbarTitle(binding.toolBarTitle, getToolbarTitle(), true)
	}

	private fun getToolbarTitle(): String =
		if( character.name?.english!!.isOk())
			character.name?.english?.chop(45)!!
		else
			NotFound.surpriseMe()

	override fun onDestroy() {
		jutsuListAdapter = null
		if (_binding != null)
			binding.characterJutsusRv.adapter = null
		super.onDestroy()
	}

}