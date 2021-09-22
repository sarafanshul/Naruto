package com.projectdelta.naruto.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.projectdelta.naruto.di.NetworkModule
import com.projectdelta.naruto.util.networking.connectivity.ConnectivityManager
import dagger.hilt.android.EntryPointAccessors

abstract class BaseActivity : AppCompatActivity() {

	/**
	 * Injects dependencies in classes not supported by Hilt
	 * [Refer](https://developer.android.com/training/dependency-injection/hilt-android#not-supported)
	 */
	private val hiltEntryPoint: NetworkModule.ConnectivityManagerProviderEntryPoint by lazy {
		EntryPointAccessors.fromApplication(
			applicationContext ,
			NetworkModule.ConnectivityManagerProviderEntryPoint::class.java
		)
	}

	lateinit var connectivityManager : ConnectivityManager

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		connectivityManager = hiltEntryPoint.connectivityManager()
		connectivityManager.registerConnectionObserver()
	}

	override fun onDestroy() {
		super.onDestroy()
		connectivityManager.unregisterConnectionObserver()
	}

}