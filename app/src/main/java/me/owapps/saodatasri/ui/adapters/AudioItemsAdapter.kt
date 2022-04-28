package me.owapps.saodatasri.ui.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.owapps.saodatasri.data.entities.Raw
import me.owapps.saodatasri.databinding.ItemAudioFileBinding

class AudioItemsAdapter : RecyclerView.Adapter<AudioItemViewHolder>() {

    private val items = arrayListOf<Raw>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioItemViewHolder {
        val binding =
            ItemAudioFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AudioItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AudioItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Raw>) {
        if (!list.isNullOrEmpty()) {
            items.clear()
            items.addAll(list)
        }
        notifyDataSetChanged()
    }

}

class AudioItemViewHolder(private val binding: ItemAudioFileBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Raw) {
        binding.apply {
            title.text = item.soundName
            chapter.text = item.description
            title.isSelected = true
            chapter.isSelected = true
        }
    }

}