package com.projectdelta.naruto.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.projectdelta.naruto.R
import com.projectdelta.naruto.databinding.ActivityMainBinding
import com.projectdelta.naruto.ui.base.BaseViewBindingActivity
import com.projectdelta.naruto.util.system.lang.toast
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

		initUI()

	}

	private fun setupNavController() {
		val navController = findNavController(R.id.nav_host_fragment)
		binding.bottomPanel.setupWithNavController(navController)

		navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
//			// TODO(IMPL)
//			if( destination.id == R.id.characterDetailFragment ){
//				toast("show bottomNav @Detail")
//				Timber.w("Force show Bottom Nav , Impl/Fix later")
//				binding.bottomPanel.visibility = View.VISIBLE
//			}
		}
	}


	private fun initUI() {
		connectivityManager.isNetworkAvailable.observe(this , { coms ->
			when( coms ){
				true -> binding.connectionTv.visibility = View.GONE
				false -> {
					binding.connectionTv.visibility = View.VISIBLE
					Timber.w("No Internet connection!")
				}
			}
		})
	}
}