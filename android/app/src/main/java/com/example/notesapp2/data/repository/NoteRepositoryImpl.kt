package com.example.notesapp2.data.repository

import com.example.notesapp2.data.remote.NoteApiService
import com.example.notesapp2.domain.model.Note
import com.example.notesapp2.domain.repository.NoteRepository
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val api: NoteApiService
) : NoteRepository {

    override suspend fun getNotes(
        search: String?
    ): List<Note> =
        api.getNotes(search)

    override suspend fun getNote(id: Int): Note =
        api.getNote(id)

    override suspend fun createNote(note: Note): Note =
        api.createNote(note)

    override suspend fun updateNote(note: Note): Note =
        api.updateNote(note.id, note)

    override suspend fun deleteNote(id: Int) =
        api.deleteNote(id)
}