package com.techhub.app.features.interview.di

import com.techhub.app.features.interview.data.repository.InterviewRepositoryImpl
import com.techhub.app.features.interview.domain.repository.InterviewRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterviewModule {

    @Binds
    @Singleton
    abstract fun bindInterviewRepository(
        impl: InterviewRepositoryImpl
    ): InterviewRepository
}
