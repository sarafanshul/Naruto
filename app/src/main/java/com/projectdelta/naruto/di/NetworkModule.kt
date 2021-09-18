package com.projectdelta.naruto.di

import com.projectdelta.naruto.data.remote.ChapterApi
import com.projectdelta.naruto.data.remote.CharacterApi
import com.projectdelta.naruto.util.Constants.CONNECTION_TIMEOUT
import com.projectdelta.naruto.util.Constants.READ_TIMEOUT
import com.projectdelta.naruto.util.Constants.WRITE_TIMEOUT
import com.projectdelta.naruto.util.networking.ApiConstants.BASE_URL
import com.projectdelta.naruto.util.networking.ApiResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

	@Singleton
	@Provides
	fun provideOkHttpClient(): OkHttpClient {
		return OkHttpClient.Builder()
			.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
			.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
			.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
			.retryOnConnectionFailure(false)
			.build()
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

}