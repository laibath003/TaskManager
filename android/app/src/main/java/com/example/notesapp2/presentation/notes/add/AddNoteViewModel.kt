package com.example.notesapp2.presentation.notes.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp2.domain.model.Note
import com.example.notesapp2.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _addNoteState =
        MutableStateFlow<AddNoteState>(AddNoteState.Idle)

    val addNoteState: StateFlow<AddNoteState> = _addNoteState

    fun addNote(
        title: String,
        content: String
    ) {

        if (title.isBlank() || content.isBlank()) {
            _addNoteState.value =
                AddNoteState.Error("Title and content are required")
            return
        }

        viewModelScope.launch {

            _addNoteState.value = AddNoteState.Loading

            try {

                val note = Note(
                    id = 0,
                    title = title,
                    content = content,
                    createdAt = ""
                )

                repository.createNote(note)

                _addNoteState.value =
                    AddNoteState.Success

            } catch (e: Exception) {

                _addNoteState.value =
                    AddNoteState.Error(
                        e.message ?: "Failed to create note"
                    )
            }
        }
    }
}

sealed class AddNoteState {

    object Idle : AddNoteState()

    object Loading : AddNoteState()

    object Success : AddNoteState()

    data class Error(
        val message: String
    ) : AddNoteState()
}