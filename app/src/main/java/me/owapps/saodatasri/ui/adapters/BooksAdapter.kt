package me.owapps.saodatasri.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.owapps.saodatasri.R
import me.owapps.saodatasri.data.entities.BookX
import me.owapps.saodatasri.databinding.ItemBookBinding

class BooksAdapter(private val onBookClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<BookViewHolder>() {

    private var booksList = ArrayList<BookX>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding: ItemBookBinding =
            ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding, onBookClickListener)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(booksList[position])
    }

    override fun getItemCount() = booksList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<BookX>) {
        if (booksList.isEmpty()) {
            booksList.addAll(list)
        } else {
            booksList.clear()
            booksList.addAll(list)
        }
        notifyDataSetChanged()
    }

}

class BookViewHolder(
    private val binding: ItemBookBinding,
    private val onBookClickListener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(book: BookX) {

        Glide.with(itemView).load(book.image).into(binding.ivBookImage)
        binding.tvBookName.text = book.name
        binding.tvBookNumber.text =
            itemView.resources.getString(R.string.book_number, book.number)
        binding.root.setOnClickListener {
            onBookClickListener(book.number)
        }
    }
}
