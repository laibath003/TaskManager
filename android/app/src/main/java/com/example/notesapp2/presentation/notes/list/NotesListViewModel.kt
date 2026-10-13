package com.example.notesapp2.presentation.notes.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp2.data.local.TokenManager
import com.example.notesapp2.domain.model.Note
import com.example.notesapp2.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _notesState =
        MutableStateFlow<NotesState>(NotesState.Idle)

    val notesState: StateFlow<NotesState> = _notesState

    fun loadNotes(search: String? = null) {

        viewModelScope.launch {

            _notesState.value =
                NotesState.Loading

            try {

                val notes =
                    repository.getNotes(search)

                _notesState.value =
                    NotesState.Success(notes)

            } catch (e: Exception) {

                _notesState.value =
                    NotesState.Error(
                        e.message ?: "Failed to load notes"
                    )
            }
        }
    }

    private val _logoutState =
        MutableStateFlow<LogoutState>(LogoutState.Idle)

    val logoutState: StateFlow<LogoutState> = _logoutState

    fun logout() {

        viewModelScope.launch {

            try {

                tokenManager.clearToken()

                _logoutState.value =
                    LogoutState.Success

            } catch (e: Exception) {

                _logoutState.value =
                    LogoutState.Error(
                        "Logout failed"
                    )
            }
        }
    }
}

sealed class NotesState {

    object Idle : NotesState()

    object Loading : NotesState()

    data class Success(
        val notes: List<Note>
    ) : NotesState()

    data class Error(
        val message: String
    ) : NotesState()
}

sealed class LogoutState {

    object Idle : LogoutState()

    object Success : LogoutState()

    data class Error(
        val message: String
    ) : LogoutState()
}