package com.rudra.quikscore.usecase

import com.rudra.quikscore.util.Resource
import com.rudra.quikscore.model.MatchesItem
import com.rudra.quikscore.repository.MatchesRepository
import javax.inject.Inject

class GetLiveMatchesUseCase @Inject constructor(
    private val repository: MatchesRepository
) {

    suspend operator fun invoke(limit: Int): Resource<List<MatchesItem>> {
        return repository.getLiveMatches(limit)
    }
}
