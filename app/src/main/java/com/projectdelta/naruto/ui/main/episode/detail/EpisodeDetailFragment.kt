package com.projectdelta.naruto.ui.main.episode.detail

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
import com.projectdelta.naruto.data.model.entity.chapter.Chapter
import com.projectdelta.naruto.databinding.FragmentEpisodeDetailBinding
import com.projectdelta.naruto.ui.base.BaseViewBindingFragment
import com.projectdelta.naruto.ui.main.MainActivity
import com.projectdelta.naruto.util.CollapsingToolbarState
import com.projectdelta.naruto.util.Constants.COLLAPSING_TOOLBAR_VISIBILITY_THRESHOLD
import com.projectdelta.naruto.util.Constants.TRANSITION_EPISODE
import com.projectdelta.naruto.util.NotFound
import com.projectdelta.naruto.util.networking.ApiConstants
import com.projectdelta.naruto.util.system.lang.chop
import com.projectdelta.naruto.util.system.lang.fadeIn
import com.projectdelta.naruto.util.system.lang.fadeOut
import com.projectdelta.naruto.util.system.lang.isOk
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EpisodeDetailFragment : BaseViewBindingFragment<FragmentEpisodeDetailBinding>() {

	companion object {

		@JvmStatic
		fun newInstance() = EpisodeDetailFragment()
	}

	lateinit var episode : Chapter

	private val viewModel : EpisodeDetailViewModel by viewModels()

	val dateFormat = SimpleDateFormat( "dd MMMM yy", Locale.ENGLISH )

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val args : EpisodeDetailFragmentArgs by navArgs()
		episode = args.chapterData
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentEpisodeDetailBinding.inflate(inflater)
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
		with(binding){
			layoutEpisodeDetail.transitionName = TRANSITION_EPISODE.plus(episode.id)

			appBar.addOnOffsetChangedListener(
				AppBarLayout.OnOffsetChangedListener{ _, offset ->
					if (offset < COLLAPSING_TOOLBAR_VISIBILITY_THRESHOLD) {
						viewModel.setCollapsingToolbarState(CollapsingToolbarState.Collapsed())
					} else {
						viewModel.setCollapsingToolbarState(CollapsingToolbarState.Expanded())
					}
				}
			)

			Glide
				.with(this@EpisodeDetailFragment)
				.load(episode.images?.first())
				.apply(
					RequestOptions()
						.placeholder(R.drawable.placeholder_white_leaf)
						.diskCacheStrategy(DiskCacheStrategy.DATA)
				)
				.thumbnail(0.25f)
				.into(episodeImage)

			// name
			episodeName.text = episode.name?.english + "\n" + episode.name?.kanji

			// air-date
			episodeAirJap.text = dateFormat.format(episode.date?.japanese!!)
			episodeAirEng.text = dateFormat.format(episode.date?.english!!)

			//description
			episodeDescription.text = episode.description

			// number
			episodeNumber.text = episode.episode?.series + " #" + episode.episode?.episode

			// arc
			episodeArc.text = episode.arc

			// music
			episodeMusicOp.text = episode.music?.opening
			episodeMusicCl.text = episode.music?.ending
			episodeMusicOp.isSelected = true
			episodeMusicCl.isSelected = true

			toolbarSecondaryIcon.setOnClickListener {
				launchWebView()
			}

			toolbarPrimaryIcon.setOnClickListener {
				findNavController().navigateUp()
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
		(requireActivity() as MainActivity).launchWebView( ApiConstants.FANDOM_URL + episode.id)
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
		if( episode.name?.english!!.isOk())
			episode.name?.english?.chop(45)!!
		else
			NotFound.surpriseMe()

}