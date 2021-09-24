package com.projectdelta.naruto.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.projectdelta.naruto.data.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

	@Singleton
	@Provides
	fun providesSharedPreference(application: Application): SharedPreferences {
		return application.getSharedPreferences(
			application.packageName + "_preferences",
			Context.MODE_PRIVATE
		)
	}

	@Singleton
	@Provides
	fun providesPreferenceManager(application: Application, sharedPreferences: SharedPreferences ): PreferenceManager {
		return PreferenceManager(application, sharedPreferences)
	}

	@EntryPoint
	@Singleton
	@InstallIn(SingletonComponent::class)
	interface PreferenceManagerProviderEntryPoint {
		fun providesPreferenceManager(): PreferenceManager
	}

}