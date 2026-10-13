package com.example.notesapp2.presentation.notes.edit

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
class EditNoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _noteState =
        MutableStateFlow<EditNoteState>(EditNoteState.Idle)

    val noteState: StateFlow<EditNoteState> = _noteState

    fun loadNote(id: Int) {

        viewModelScope.launch {

            _noteState.value = EditNoteState.Loading

            try {

                val note = repository.getNote(id)

                _noteState.value =
                    EditNoteState.Loaded(note)

            } catch (e: Exception) {

                _noteState.value =
                    EditNoteState.Error(
                        e.message ?: "Failed to load note"
                    )
            }
        }
    }

    fun updateNote(
        id: Int,
        title: String,
        content: String
    ) {

        if (title.isBlank() || content.isBlank()) {

            _noteState.value =
                EditNoteState.Error(
                    "Title and content are required"
                )

            return
        }

        viewModelScope.launch {

            _noteState.value = EditNoteState.Loading

            try {

                val note = Note(
                    id = id,
                    title = title,
                    content = content,
                    createdAt = ""
                )

                repository.updateNote(note)

                _noteState.value =
                    EditNoteState.Updated

            } catch (e: Exception) {

                _noteState.value =
                    EditNoteState.Error(
                        e.message ?: "Failed to update note"
                    )
            }
        }
    }

    fun deleteNote(id: Int) {

        viewModelScope.launch {

            _noteState.value = EditNoteState.Loading

            try {

                repository.deleteNote(id)

                _noteState.value =
                    EditNoteState.Deleted

            } catch (e: Exception) {

                _noteState.value =
                    EditNoteState.Error(
                        e.message ?: "Failed to delete note"
                    )
            }
        }
    }
}

sealed class EditNoteState {

    object Idle : EditNoteState()

    object Loading : EditNoteState()

    data class Loaded(
        val note: Note
    ) : EditNoteState()

    object Updated : EditNoteState()

    object Deleted : EditNoteState()

    data class Error(
        val message: String
    ) : EditNoteState()
}