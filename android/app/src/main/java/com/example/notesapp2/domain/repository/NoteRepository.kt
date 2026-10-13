package com.example.notesapp2.domain.repository

import com.example.notesapp2.domain.model.Note

interface NoteRepository {

    suspend fun getNotes(
        search: String? = null
    ): List<Note>

    suspend fun getNote(id: Int): Note

    suspend fun createNote(note: Note): Note

    suspend fun updateNote(note: Note): Note

    suspend fun deleteNote(id: Int)
}