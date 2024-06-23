package com.rudra.quikscore.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.quikscore.R
import com.rudra.quikscore.model.MatchesItem
import com.rudra.quikscore.usecase.GetMatchByIdUseCase
import com.rudra.quikscore.util.ErrorText
import com.rudra.quikscore.util.ErrorType
import com.rudra.quikscore.util.ErrorTypeToErrorTextConverter
import com.rudra.quikscore.util.Resource
import com.rudra.quikscore.util.UiState
import com.rudra.quikscore.util.generateRandomMatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DetailedMatchViewModel @Inject constructor(
    private val getMatchByIdUseCase: GetMatchByIdUseCase,
    private val errorTypeToErrorTextConverter: ErrorTypeToErrorTextConverter
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<MatchesItem>>(UiState.Loading())
    val uiState: StateFlow<UiState<MatchesItem>> = _uiState

    private var simulateJob: Job? = null
    private var isDemoMatch = false

    fun fetchMatch(matchId: String, isDemoMatch: Boolean) {
        this.isDemoMatch = isDemoMatch
        if (isDemoMatch) {
            fetchDemoMatches()

        } else {
            viewModelScope.launch {
                try {
                    _uiState.value = UiState.Loading()
                    val result = getMatchByIdUseCase(matchId)
                    _uiState.value = handleResult(result)
                } catch (e: Exception) {
                    handleException(e)
                }
            }
        }
    }

    fun fetchDemoMatches() {
        // Simulate a list of demo matches
        val random = Random.nextInt(1, 5)
        val list = List(random) { generateRandomMatch() }
        _uiState.value = UiState.Loaded(list.first())
        startGoalSimulation()
    }

    private fun startGoalSimulation() {
        stopGoalSimulation() // Stop any existing simulation job
        simulateJob = viewModelScope.launch {
            while (isActive && isDemoMatch) {
                simulateGoal()
                delay(10_000) // Simulate score update every 10 seconds
            }
        }
    }
    private fun stopGoalSimulation() {
        simulateJob?.cancel()
        simulateJob = null
    }

    private fun simulateGoal() {
        val currentMatch = (uiState.value as? UiState.Loaded)?.data ?: return
        // Simulate goal for either home team or away team randomly
        val updatedMatch = if (Random.nextBoolean()) {
            val newHomeScore = currentMatch.match_hometeam_score.toIntOrNull() ?: 0
            currentMatch.copy(match_hometeam_score = (newHomeScore + 1).toString())
        } else {
            val newAwayScore = currentMatch.match_awayteam_score.toIntOrNull() ?: 0
            currentMatch.copy(match_awayteam_score = (newAwayScore + 1).toString())
        }
        _uiState.value = UiState.Loaded(updatedMatch)
    }


    private fun handleResult(result: Resource<MatchesItem>): UiState<MatchesItem> {
        return when (result) {
            is Resource.Success -> {
                UiState.Loaded(result.data)
            }

            is Resource.Error -> {
                handleResourceError(result.error)
            }
        }
    }

    private fun handleException(exception: Exception) {
        Log.e("DetailedMatchViewModel", "Error fetching match: ${exception.message}")
        _uiState.value = UiState.Error(
            ErrorText.StringResource(
                when (exception) {
                    is IOException -> R.string.error_network_unavailable
                    else -> R.string.error_general
                }
            )
        )
    }
    private fun handleResourceError(error: ErrorType): UiState<MatchesItem> {
        val errorText = errorTypeToErrorTextConverter.convert(error)
        return UiState.Error(errorText)
    }

}
