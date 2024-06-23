package com.rudra.quikscore.usecase

import com.rudra.quikscore.util.Resource
import com.rudra.quikscore.repository.MatchesRepository
import com.rudra.quikscore.model.MatchesItem
import javax.inject.Inject

class GetMatchByIdUseCase @Inject constructor(
    private val repository: MatchesRepository
) {

    suspend operator fun invoke(matchId: String): Resource<MatchesItem> {
        return repository.getMatchUpdate(matchId)
    }
}
