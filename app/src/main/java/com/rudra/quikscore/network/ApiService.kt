package com.rudra.quikscore.network

import com.rudra.quikscore.BuildConfig
import com.rudra.quikscore.model.MatchesItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("?action=get_events&match_live=1")
    suspend fun getLiveScores(
        @Query("limit") limit: Int,
        @Query("APIkey") apiKey: String = BuildConfig.api_football_api
    ): Response<List<MatchesItem>>

    @GET("?action=get_events&match_live=1")
    suspend fun getMatchUpdate(
        @Query("match_id") matchId: String,
        @Query("APIkey") apiKey: String = BuildConfig.api_football_api
    ): Response<List<MatchesItem>>
}