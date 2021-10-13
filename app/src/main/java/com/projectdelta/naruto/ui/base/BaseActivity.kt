package com.projectdelta.naruto.ui.base

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.projectdelta.naruto.R
import com.projectdelta.naruto.data.preference.PreferenceManager
import com.projectdelta.naruto.di.NetworkModule
import com.projectdelta.naruto.di.PreferenceModule
import com.projectdelta.naruto.ui.web.WebViewActivity
import com.projectdelta.naruto.util.networking.connectivity.ConnectivityManager
import com.projectdelta.naruto.util.system.lang.getResourceColor
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

	/**
	 * Only works if `android:fitsSystemWindows="true"`
	 * TODO replace with Window.setNavigationBarTransparentCompat
	 */
	protected open fun makeTransparentStatusBar(isTransparent: Boolean) {
		if (isTransparent) {
			window.statusBarColor = Color.TRANSPARENT
		} else {
			window.statusBarColor = getResourceColor(R.attr.colorToolbar)
		}
	}

	fun launchWebView(url : String) = startActivity(WebViewActivity.newIntent(this , url))

	override fun onDestroy() {
		super.onDestroy()
		connectivityManager.unregisterConnectionObserver()
	}

}