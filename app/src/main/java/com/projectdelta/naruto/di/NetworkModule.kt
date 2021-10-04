package com.projectdelta.naruto.di

import android.app.Application
import com.projectdelta.naruto.BuildConfig
import com.projectdelta.naruto.data.remote.ChapterApi
import com.projectdelta.naruto.data.remote.CharacterApi
import com.projectdelta.naruto.data.remote.JutsuApi
import com.projectdelta.naruto.data.remote.VillageApi
import com.projectdelta.naruto.util.Constants.CONNECTION_TIMEOUT
import com.projectdelta.naruto.util.Constants.READ_TIMEOUT
import com.projectdelta.naruto.util.Constants.WRITE_TIMEOUT
import com.projectdelta.naruto.util.networking.ApiConstants.BASE_URL
import com.projectdelta.naruto.util.networking.ApiResultCallAdapterFactory
import com.projectdelta.naruto.util.networking.connectivity.ConnectivityManager
import com.projectdelta.naruto.util.networking.connectivity.ConnectivityManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

	@Singleton
	@Provides
	fun provideConnectivityManager(application: Application): ConnectivityManager {
		return ConnectivityManagerImpl(application)
	}

	@EntryPoint
	@Singleton
	@InstallIn(SingletonComponent::class)
	interface ConnectivityManagerProviderEntryPoint {
		fun connectivityManager(): ConnectivityManager
	}

	@Singleton
	@Provides
	fun provideLoggingInterceptor(): HttpLoggingInterceptor {
		return HttpLoggingInterceptor().apply {
			level = HttpLoggingInterceptor.Level.HEADERS
		}
	}

	@Singleton
	@Provides
	fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
		return OkHttpClient.Builder().apply {
			connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
			readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
			writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
			retryOnConnectionFailure(false)
			if (BuildConfig.DEBUG)
				addNetworkInterceptor(loggingInterceptor)
		}.build()
	}

	@Singleton
	@Provides
	fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.client(okHttpClient)
			.addCallAdapterFactory(ApiResultCallAdapterFactory())
			.addConverterFactory(GsonConverterFactory.create())
	}

	@Singleton
	@Provides
	fun provideChapterApiService(retrofitBuilder: Retrofit.Builder): ChapterApi {
		return retrofitBuilder
			.build()
			.create(ChapterApi::class.java)
	}

	@Singleton
	@Provides
	fun provideCharacterApiService(retrofitBuilder: Retrofit.Builder): CharacterApi {
		return retrofitBuilder
			.build()
			.create(CharacterApi::class.java)
	}

	@Singleton
	@Provides
	fun provideVillageApiService(retrofitBuilder: Retrofit.Builder): VillageApi {
		return retrofitBuilder
			.build()
			.create(VillageApi::class.java)
	}

	@Singleton
	@Provides
	fun provideJutsuApiService(retrofitBuilder: Retrofit.Builder): JutsuApi {
		return retrofitBuilder
			.build()
			.create(JutsuApi::class.java)
	}

}