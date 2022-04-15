package me.owapps.saodatasri.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import me.owapps.saodatasri.data.remote.BooksService
import me.owapps.saodatasri.repository.BooksRepository
import me.owapps.saodatasri.util.Constants.BASE_URL
import me.owapps.saodatasri.util.ResponseHandler
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideApiService(): BooksService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(BooksService::class.java)

    @Provides
    @Singleton
    fun provideResponseHandler(): ResponseHandler = ResponseHandler()

    @Provides
    @Singleton
    fun provideBooksRepository(
        booksService: BooksService,
        responseHandler: ResponseHandler
    ): BooksRepository = BooksRepository(booksService, responseHandler)

}


