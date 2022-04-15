package me.owapps.saodatasri.repository

import dagger.hilt.android.scopes.ActivityScoped
import me.owapps.saodatasri.data.entities.BooksResponse
import me.owapps.saodatasri.data.remote.BooksService
import me.owapps.saodatasri.util.Resource
import me.owapps.saodatasri.util.ResponseHandler
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class BooksRepository @Inject constructor(
    private val booksService: BooksService,
    private val responseHandler: ResponseHandler
) {

    suspend fun getBooks(): Resource<BooksResponse> {
        return try {
            responseHandler.handleSuccess(booksService.fetchBooks())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

}