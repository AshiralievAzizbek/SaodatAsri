package me.owapps.saodatasri.data.remote

import me.owapps.saodatasri.data.entities.BooksResponse
import me.owapps.saodatasri.data.entities.book.BookResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BooksService {

    @GET("api/books")
    suspend fun fetchBooks(): BooksResponse

    @GET("api/books/{id}")
    suspend fun fetchBook(@Path("id") id: Int): BookResponse

}