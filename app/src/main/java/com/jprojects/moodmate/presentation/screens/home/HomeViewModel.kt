package com.jprojects.moodmate.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jprojects.moodmate.data.repository.moodRepo.MoodRepository
import com.jprojects.moodmate.domain.model.MoodDetails
import com.jprojects.moodmate.domain.model.UserDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moodRepository: MoodRepository
) : ViewModel() {

    private val _userDetails = MutableStateFlow<UserDetails?>(null)
    val userDetails: StateFlow<UserDetails?> = _userDetails

    private val _moods = MutableStateFlow<List<MoodDetails>>(emptyList())
    val moods: StateFlow<List<MoodDetails>> = _moods

    init {
        observeUserDetails()
        observeMoods()
    }

    private fun observeUserDetails() {
        viewModelScope.launch {
            moodRepository.fetchUserDetails()
                .catch { it.printStackTrace() }
                .collect { _userDetails.value = it }
        }
    }

    private fun observeMoods() {
        viewModelScope.launch {
            moodRepository.fetchMood()
                .catch { it.printStackTrace() }
                .collect { _moods.value = it }
        }
    }
}
