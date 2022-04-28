package com.projectdelta.naruto.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.projectdelta.naruto.R
import com.projectdelta.naruto.databinding.ActivityMainBinding
import com.projectdelta.naruto.ui.base.BaseViewBindingActivity
import com.projectdelta.naruto.util.NotFound
import com.projectdelta.naruto.util.system.lang.slideDown
import com.projectdelta.naruto.util.system.lang.slideUp
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseViewBindingActivity<ActivityMainBinding>() {

	private val viewModel: MainViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		_binding = ActivityMainBinding.inflate(layoutInflater)

		setContentView(binding.root)

		setupNavController()

		registerObservers()

		initUI()

	}

	private fun registerObservers() {
		viewModel.bottomNavVisibility.observe(this, { integer: Int ->
			when (integer) {
				View.GONE -> binding.bottomPanel.slideDown()
				View.VISIBLE -> binding.bottomPanel.slideUp()
				else -> throw NotFound.TheFuckHappened("Only GONE & VISIBLE state supported!")
			}
		})
	}

	private fun setupNavController() {
		val navController = findNavController(R.id.nav_host_fragment)
		binding.bottomPanel.setupWithNavController(navController)

		navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
			if (destination.id in listOf(
					R.id.characterDetailFragment,
					R.id.locationDetailFragment,
					R.id.episodeDetailFragment
				)
			) {
				makeTransparentStatusBar(true)
				viewModel.hideBottomNav()
			} else {
				makeTransparentStatusBar(false)
				viewModel.showBottomNav()
			}
		}
	}

	private fun initUI() {
		connectivityManager.isNetworkAvailable.observe(this, { comms ->
			when (comms) {
				true -> {
					binding.connectionTv.visibility = View.GONE
					window.statusBarColor = Color.TRANSPARENT
				}
				false -> {
					window.statusBarColor = getColor(R.color.matte_red)
					binding.connectionTv.visibility = View.VISIBLE
					Timber.w("No Internet connection!")
				}
			}
		})
	}
}
