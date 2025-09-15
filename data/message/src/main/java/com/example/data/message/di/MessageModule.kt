package com.example.data.message.di

import com.example.data.message.remote.MessageRemoteSource
import com.example.data.message.repository.MessageRepository
import com.example.data.message.repository.MessageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MessageModule {

    @Provides
    @Singleton
    internal fun provideMessageRemoteSource(): MessageRemoteSource = MessageRemoteSource()

    @Provides
    @Singleton
    internal fun provideMessageRepository(messageRemoteSource: MessageRemoteSource): MessageRepository =
        MessageRepositoryImpl(messageRemoteSource = messageRemoteSource)


}