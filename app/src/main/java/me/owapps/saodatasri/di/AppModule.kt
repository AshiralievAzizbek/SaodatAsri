package me.owapps.saodatasri.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.owapps.saodatasri.exoplayer.MediaServiceConnection
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMediaServiceConnection(@ApplicationContext context: Context) =
        MediaServiceConnection(context)

}