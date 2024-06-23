package com.rudra.quikscore.repository

import android.content.Context
import android.net.ConnectivityManager
import com.rudra.quikscore.util.ErrorType
import com.rudra.quikscore.util.Resource
import com.rudra.quikscore.model.MatchesItem
import com.rudra.quikscore.network.ApiService
import com.rudra.quikscore.util.toErrorType
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
class MatchesRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val context: Context
) : MatchesRepository {

    override suspend fun getLiveMatches(limit: Int): Resource<List<MatchesItem>> {
        return try {
            if (isNetworkAvailable()) {
                val response = apiService.getLiveScores(limit)
                if (response.isSuccessful) {
                    val matches = response.body() ?: emptyList()
                    Resource.Success(matches)
                } else {
                    Resource.Error(HttpException(response).toErrorType())
                }
            } else {
                Resource.Error(ErrorType.Api.Network)
            }
        } catch (e: Exception) {
            Resource.Error(e.toErrorType())
        }
    }
    override suspend fun getMatchUpdate(matchId: String): Resource<MatchesItem> {
        return try {
            if (isNetworkAvailable()) {
                val response = apiService.getMatchUpdate(matchId)
                if (response.isSuccessful) {
                    val match = response.body()?.firstOrNull()
                    match?.let {
                        Resource.Success(it)
                    } ?: Resource.Error(ErrorType.Api.NotFound)
                } else {
                    Resource.Error(HttpException(response).toErrorType())
                }
            } else {
                Resource.Error(ErrorType.Api.Network)
            }
        } catch (e: IOException) {
            Resource.Error(ErrorType.Api.Network)
        } catch (e: Exception) {
            Resource.Error(e.toErrorType())
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }
}
