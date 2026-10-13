package com.example.notesapp2.data.remote

import com.example.notesapp2.domain.model.Note
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NoteApiService {

    @GET("notes")
    suspend fun getNotes(
        @Query("search") search: String? = null
    ): List<Note>

    @GET("notes/{id}")
    suspend fun getNote(
        @Path("id") id: Int
    ): Note

    @POST("notes")
    suspend fun createNote(
        @Body note: Note
    ): Note

    @PUT("notes/{id}")
    suspend fun updateNote(
        @Path("id") id: Int,
        @Body note: Note
    ): Note

    @DELETE("notes/{id}")
    suspend fun deleteNote(
        @Path("id") id: Int
    )
}