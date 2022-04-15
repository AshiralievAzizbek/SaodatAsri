package me.owapps.saodatasri.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.owapps.saodatasri.data.entities.Book
import me.owapps.saodatasri.repository.BooksRepository


class HomeViewModel(private val booksRepository: BooksRepository) :
    ViewModel() {


    private val _books = MutableLiveData<List<Book>>()
    val mBooks: LiveData<List<Book>> get() = _books

    fun fetchBooks() {
        viewModelScope.launch {
            val response = booksRepository.getBooks()
            _books.value = response.data!!.data
        }

    }

}