package com.jprojects.moodmate.presentation.screens.home.addMood

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jprojects.moodmate.data.repository.moodRepo.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AddMoodViewModel @Inject constructor(
    private val moodRepository: MoodRepository,
) : ViewModel() {

    var selectedMood by mutableStateOf<String>("")
        private set

    var moodReason by mutableStateOf("")
        private set

    var isSubmitting by mutableStateOf(false)
        private set

    fun onMoodSelected(mood: String) {
        selectedMood = mood
    }

    fun onMoodReasonChanged(reason: String) {
        moodReason = reason
    }

    fun submitMood(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val mood = selectedMood


        viewModelScope.launch {
            isSubmitting = true
            val result = moodRepository.addMood(selectedMood, moodReason)
            isSubmitting = false

            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { onError(it.message ?: "Something went wrong") }
            )
        }
    }
}
