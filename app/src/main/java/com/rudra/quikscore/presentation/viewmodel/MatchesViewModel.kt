package com.rudra.quikscore.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.quikscore.util.ErrorText
import com.rudra.quikscore.util.ErrorTypeToErrorTextConverter
import com.rudra.quikscore.R
import com.rudra.quikscore.util.Resource
import com.rudra.quikscore.util.UiState
import com.rudra.quikscore.util.generateRandomMatch
import com.rudra.quikscore.model.MatchesItem
import com.rudra.quikscore.usecase.GetLiveMatchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val getLiveMatchesUseCase: GetLiveMatchesUseCase,
    private val errorTypeToErrorTextConverter: ErrorTypeToErrorTextConverter
) : ViewModel() {

    // StateFlows for UI state management
    private val _uiState = MutableStateFlow<UiState<List<MatchesItem>>>(UiState.Loading())
    val uiState: StateFlow<UiState<List<MatchesItem>>> get() = _uiState

    private val _demoMatches = MutableStateFlow<List<MatchesItem>>(emptyList())
    val demoMatches: StateFlow<List<MatchesItem>> get() = _demoMatches.asStateFlow()

    private var liveMatchUpdateJob: Job? = null
    private var simulateJob: Job? = null

    private var currentPage = 0
    private val limit = 10

    init {
        startMatchSimulation()
    }

    fun fetchDemoMatches() {
        val random = Random.nextInt(1, 5)
        val list = List(random) { generateRandomMatch() }
        _demoMatches.value = list
        startGoalSimulation()
    }

    private fun startMatchSimulation() {
        stopMatchSimulation()
        liveMatchUpdateJob = viewModelScope.launch {
            while (isActive) {
                fetchLiveMatches()
                fetchDemoMatches()
                delay(60_000) // 60 seconds
            }
        }
    }

    private fun stopMatchSimulation() {
        liveMatchUpdateJob?.cancel()
        liveMatchUpdateJob = null
    }

    private fun startGoalSimulation() {
        stopGoalSimulation()
        simulateJob = viewModelScope.launch {
            while (isActive) {
                simulateGoal()
                delay(10_000) // 10 seconds
            }
        }
    }

    private fun stopGoalSimulation() {
        simulateJob?.cancel()
        simulateJob = null
    }

    private fun simulateGoal() {
        val currentMatches = _demoMatches.value.toMutableList()

        if (currentMatches.isNotEmpty()) {
            val randomIndex = Random.nextInt(currentMatches.size)
            val currentMatch = currentMatches[randomIndex]

            val newHomeScore = currentMatch.match_hometeam_score.toIntOrNull() ?: 0
            val newAwayScore = currentMatch.match_awayteam_score.toIntOrNull() ?: 0

            val updatedMatch = if (Random.nextBoolean()) {
                currentMatch.copy(match_hometeam_score = (newHomeScore + 1).toString())
            } else {
                currentMatch.copy(match_awayteam_score = (newAwayScore + 1).toString())
            }
            currentMatches[randomIndex] = updatedMatch

            _demoMatches.value = currentMatches
        }
    }

    fun fetchLiveMatches() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading() // Set loading state
            _uiState.value = handleResult {
                getLiveMatchesUseCase(limit)
            }
        }
    }


    private suspend fun <T> handleResult(action: suspend () -> Resource<T>): UiState<T> {
        return try {
            when (val result = action()) {
                is Resource.Success -> {
                    UiState.Loaded(result.data)
                }
                is Resource.Error -> {
                    UiState.Error(errorTypeToErrorTextConverter.convert(result.error))
                }
            }
        } catch (exception: IOException) {
            UiState.Error(ErrorText.StringResource(R.string.error_network_unavailable))
        } catch (e: Exception) {
            UiState.Error(ErrorText.StringResource(R.string.error_general))
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopMatchSimulation()
        stopGoalSimulation()
    }
}
