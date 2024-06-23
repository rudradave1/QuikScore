package com.rudra.quikscore.di

import android.content.Context
import com.rudra.quikscore.util.ErrorTypeToErrorTextConverter
import com.rudra.quikscore.util.ErrorTypeToErrorTextConverterImpl
import com.rudra.quikscore.network.ApiService
import com.rudra.quikscore.repository.MatchesRepository
import com.rudra.quikscore.repository.MatchesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val FOOTBALL_API_BASE_URL = "https://apiv3.apifootball.com/"
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)
                if (!response.isSuccessful) {
                    throw IOException("HTTP error code: ${response.code}")
                }
                response
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(FOOTBALL_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideMatchesRepository(
        apiService: ApiService,
        @ApplicationContext context: Context
    ): MatchesRepository {
        return MatchesRepositoryImpl(apiService, context)
    }

    @Provides
    @Singleton
    fun provideErrorTypeToErrorTextConverter(): ErrorTypeToErrorTextConverter {
        return ErrorTypeToErrorTextConverterImpl()
    }
}
