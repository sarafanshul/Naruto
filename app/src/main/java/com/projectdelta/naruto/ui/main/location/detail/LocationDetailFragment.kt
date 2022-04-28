package com.projectdelta.naruto.ui.main.location.detail

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import com.projectdelta.naruto.R
import com.projectdelta.naruto.data.model.entity.location.Village
import com.projectdelta.naruto.databinding.FragmentLocationDetailBinding
import com.projectdelta.naruto.ui.base.BaseViewBindingFragment
import com.projectdelta.naruto.ui.main.MainActivity
import com.projectdelta.naruto.util.CollapsingToolbarState
import com.projectdelta.naruto.util.Constants
import com.projectdelta.naruto.util.Constants.TRANSITION_LOCATION
import com.projectdelta.naruto.util.NotFound
import com.projectdelta.naruto.util.networking.ApiConstants
import com.projectdelta.naruto.util.system.lang.chop
import com.projectdelta.naruto.util.system.lang.fadeIn
import com.projectdelta.naruto.util.system.lang.fadeOut
import com.projectdelta.naruto.util.system.lang.isOk
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationDetailFragment : BaseViewBindingFragment<FragmentLocationDetailBinding>() {

	companion object {
		@JvmStatic
		fun newInstance() = LocationDetailFragment()
	}

	private val viewModel: LocationDetailViewModel by viewModels()

	lateinit var village: Village

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel

		val args: LocationDetailFragmentArgs by navArgs()
		village = args.villageData

	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentLocationDetailBinding.inflate(inflater)
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
	}

	@SuppressLint("SetTextI18n")
	private fun initUI() {
		with(binding) {
			layoutLocationDetail.transitionName = TRANSITION_LOCATION.plus(village.id)

			appBar.addOnOffsetChangedListener(
				AppBarLayout.OnOffsetChangedListener { _, offset ->
					if (offset < Constants.COLLAPSING_TOOLBAR_VISIBILITY_THRESHOLD) {
						viewModel.setCollapsingToolbarState(CollapsingToolbarState.Collapsed())
					} else {
						viewModel.setCollapsingToolbarState(CollapsingToolbarState.Expanded())
					}
				}
			)

			if (village.image != null)
				Glide
					.with(this@LocationDetailFragment)
					.load(village.image)
					.apply(
						RequestOptions()
							.placeholder(R.drawable.jinchuriki)
							.diskCacheStrategy(DiskCacheStrategy.DATA)
					)
					.thumbnail(0.25f)
					.into(villageImage)

			villageName.text = "${village.name?.english}\n${village.name?.kanji}"

			villageCountry.text = village.data?.country ?: NotFound.surpriseMe()

			villageDescription.text = village.description

			nameRomaji.text = village.name?.romaji

			villageLeaders.text =
				village.data?.leader?.joinToString("\n") { it } ?: NotFound.surpriseMe()

			toolbarPrimaryIcon.setOnClickListener {
				findNavController().navigateUp()
			}

			toolbarSecondaryIcon.setOnClickListener {
				launchWebView()
			}
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
		(requireActivity() as MainActivity).launchWebView(ApiConstants.FANDOM_URL + village.id)
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
		if (village.name?.english!!.isOk())
			village.name?.english?.chop(45)!!
		else
			NotFound.surpriseMe()

}

