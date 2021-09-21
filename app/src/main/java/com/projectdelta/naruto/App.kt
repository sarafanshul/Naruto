package com.projectdelta.naruto

import android.app.Application
import android.os.Build
import android.os.StrictMode
import com.projectdelta.naruto.util.CustomDebugTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
open class App : Application() {

	override fun onCreate() {
		super.onCreate()

		setupTheme()

		if (BuildConfig.DEBUG)
			Timber.plant(CustomDebugTree())

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			setupStrictMode()
		} else {
			setupStrictModeLegacy()
		}

	}

	open fun setupTheme() {}

	private fun setupStrictModeLegacy() {
		if (BuildConfig.DEBUG) {
			StrictMode.setThreadPolicy(
				StrictMode.ThreadPolicy.Builder()
					.detectDiskReads()
					.detectDiskWrites()
					.detectAll()
					.penaltyLog()
					.build()
			)
			StrictMode.setVmPolicy(
				StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects()
					.detectLeakedClosableObjects()
					.penaltyLog()
					.build()
			)
		}
	}

	private fun setupStrictMode() {
		if (BuildConfig.DEBUG) {
			StrictMode.setThreadPolicy(
				StrictMode.ThreadPolicy.Builder()
					.detectDiskReads()
					.detectDiskWrites()
					.detectAll()
					.penaltyLog()
					.build()
			)
			StrictMode.setVmPolicy(
				StrictMode.VmPolicy.Builder()
//					.detectNonSdkApiUsage()
					.detectLeakedSqlLiteObjects()
					.detectLeakedClosableObjects()
					.penaltyLog()
					.build()
			)
		}
	}

}