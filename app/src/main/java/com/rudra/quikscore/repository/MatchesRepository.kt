package com.rudra.quikscore.repository

import com.rudra.quikscore.util.Resource
import com.rudra.quikscore.model.MatchesItem

interface MatchesRepository {
    suspend fun getLiveMatches(limit: Int): Resource<List<MatchesItem>>
    suspend fun getMatchUpdate(matchId: String): Resource<MatchesItem>
}
