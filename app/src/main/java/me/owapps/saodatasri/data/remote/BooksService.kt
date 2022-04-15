package me.owapps.saodatasri.data.remote

import me.owapps.saodatasri.data.entities.BooksResponse
import retrofit2.http.GET

interface BooksService {

    @GET("api/books")
    suspend fun fetchBooks(): BooksResponse

}