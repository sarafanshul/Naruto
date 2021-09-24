package com.projectdelta.naruto.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.projectdelta.naruto.data.preference.PreferenceManager
import com.projectdelta.naruto.di.NetworkModule
import com.projectdelta.naruto.di.PreferenceModule
import com.projectdelta.naruto.util.networking.connectivity.ConnectivityManager
import dagger.hilt.android.EntryPointAccessors

abstract class BaseActivity : AppCompatActivity() {

	/**
	 * Injects dependencies in classes not supported by Hilt
	 * [Refer](https://developer.android.com/training/dependency-injection/hilt-android#not-supported)
	 */
	private val connectivityManagerHiltEntryPoint: NetworkModule.ConnectivityManagerProviderEntryPoint by lazy {
		EntryPointAccessors.fromApplication(
			applicationContext ,
			NetworkModule.ConnectivityManagerProviderEntryPoint::class.java
		)
	}
	private val preferencesManagerHiltEntryPoint: PreferenceModule.PreferenceManagerProviderEntryPoint by lazy {
		EntryPointAccessors.fromApplication(
			applicationContext ,
			PreferenceModule.PreferenceManagerProviderEntryPoint::class.java
		)
	}

	lateinit var connectivityManager : ConnectivityManager
	lateinit var preferenceManager: PreferenceManager

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		preferenceManager = preferencesManagerHiltEntryPoint.providesPreferenceManager()

		connectivityManager = connectivityManagerHiltEntryPoint.connectivityManager()
		connectivityManager.registerConnectionObserver()

	}

	override fun onDestroy() {
		super.onDestroy()
		connectivityManager.unregisterConnectionObserver()
	}

}