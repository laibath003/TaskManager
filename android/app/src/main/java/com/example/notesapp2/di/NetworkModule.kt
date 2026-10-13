package com.example.notesapp2.di

import com.example.notesapp2.data.remote.AuthApiService
import com.example.notesapp2.data.remote.AuthInterceptor
import com.example.notesapp2.data.remote.NoteApiService
import com.example.notesapp2.data.remote.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        authInterceptor: AuthInterceptor
    ): retrofit2.Retrofit {
        return RetrofitInstance.create(authInterceptor)
    }

    @Provides
    @Singleton
    fun provideNoteApiService(
        retrofit: retrofit2.Retrofit
    ): NoteApiService {
        return retrofit.create(NoteApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApiService(
        retrofit: retrofit2.Retrofit
    ): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
}