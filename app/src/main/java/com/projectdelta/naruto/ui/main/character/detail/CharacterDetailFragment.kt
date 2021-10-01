package com.projectdelta.naruto.ui.main.character.detail

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
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
import com.projectdelta.naruto.util.CollapsingToolbarState
import com.projectdelta.naruto.util.Constants.COLLAPSING_TOOLBAR_VISIBILITY_THRESHOLD
import com.projectdelta.naruto.util.Constants.TRANSITION_CHARACTER
import com.projectdelta.naruto.util.NotFound
import com.projectdelta.naruto.util.callback.TodoCallback
import com.projectdelta.naruto.util.system.lang.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterDetailFragment : BaseViewBindingFragment<FragmentCharacterDetailBinding>() {

	companion object {
		@JvmStatic
		fun newInstance() = CharacterDetailFragment()
	}

	private val viewModel : CharacterDetailViewModel by viewModels()

	lateinit var character : Character

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel
		prepareTransitions()
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentCharacterDetailBinding.inflate(layoutInflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		view.doOnPreDraw {
			startPostponedEnterTransition()
		}

		initUI()
		subscribeObservers()
	}

	private fun initUI() {
		val args: CharacterDetailFragmentArgs by navArgs()
		character = args.characterData

		binding.layoutCharacterDetail.transitionName = TRANSITION_CHARACTER.plus(character.id)

		binding.appBar.addOnOffsetChangedListener(
			AppBarLayout.OnOffsetChangedListener{ _ ,offset ->
				if (offset < COLLAPSING_TOOLBAR_VISIBILITY_THRESHOLD) {
					viewModel.setCollapsingToolbarState(CollapsingToolbarState.Collapsed())
				} else {
					viewModel.setCollapsingToolbarState(CollapsingToolbarState.Expanded())
				}
			}
		)

		Glide
			.with(this)
			.load(character.images?.first())
			.apply(
				RequestOptions()
					.placeholder(R.drawable.placeholder_white_leaf)
					.diskCacheStrategy(DiskCacheStrategy.DATA)
			)
			.dontTransform()
			.dontAnimate()
			.into(binding.characterImageView)
	}

	/**
	 *  ref [1](https://www.youtube.com/watch?v=KuV8Y9-T-oA) ,
	 *  [2](https://github.com/AlexSheva-mason/Rick-Morty-Database/blob/b94ffac84abf765dbc6136d7c6303d44fdd8f642/app/src/main/java/com/shevaalex/android/rickmortydatabase/ui/base/BaseDetailFragment.kt#L232)
	 *  TODO(Glide stuck / weird behaviour on exit)
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
		viewModel.collapsingToolbarState.observe(this, { state ->

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

	@Suppress("all")
	private fun displayToolbarTitle(textView: TextView, title: String?, useAnimation: Boolean) {
		if (title != null) {
			showToolbarTitle(textView, title, useAnimation)
		} else {
			hideToolbarTitle(textView, useAnimation)
		}
	}

	private fun hideToolbarTitle(textView: TextView, animation: Boolean) {
		if (animation) {
			textView.fadeOut(
				object : TodoCallback {
					override fun execute() {
						textView.text = ""
					}
				}
			)
		} else {
			textView.text = ""
			textView.gone()
		}
	}

	private fun showToolbarTitle(textView: TextView, title: String, animation: Boolean) {
		textView.text = title
		if (animation) {
			textView.fadeIn()
		} else {
			textView.visible()
		}
	}

}