package me.owapps.saodatasri.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.owapps.saodatasri.R
import me.owapps.saodatasri.data.entities.Book
import me.owapps.saodatasri.databinding.ItemBookBinding

class BooksAdapter(private val onBookClickListener: View.OnClickListener) :
    RecyclerView.Adapter<BookViewHolder>() {

    private var booksList = ArrayList<Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding: ItemBookBinding =
            ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding, onBookClickListener)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(booksList[position])
    }

    override fun getItemCount() = booksList.size

    fun setList(list: List<Book>) {
        if (booksList.isEmpty())
            booksList.addAll(list)
        else {
            booksList.clear()
            booksList.addAll(list)
        }
    }

}

class BookViewHolder(
    private val binding: ItemBookBinding,
    private val onBookClickListener: View.OnClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(book: Book) {
        binding.tvBookName.text = book.bookName
        binding.tvBookNumber.text =
            itemView.resources.getString(R.string.book_number, book.position)
        binding.root.setOnClickListener(onBookClickListener)
    }
}
