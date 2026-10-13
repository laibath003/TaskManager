package com.example.notesapp2.di

import com.example.notesapp2.data.repository.AuthRepositoryImpl
import com.example.notesapp2.data.repository.NoteRepositoryImpl
import com.example.notesapp2.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNoteRepository(
        repositoryImpl: NoteRepositoryImpl
    ): NoteRepository
}