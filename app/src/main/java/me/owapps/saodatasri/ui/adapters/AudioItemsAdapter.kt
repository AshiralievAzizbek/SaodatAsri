package me.owapps.saodatasri.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.owapps.saodatasri.data.entities.book.Audio
import me.owapps.saodatasri.databinding.ItemAudioFileBinding
import me.owapps.saodatasri.util.AudioItem

class AudioItemsAdapter(private val audioItem: AudioItem) :
    RecyclerView.Adapter<AudioItemViewHolder>() {

    private val items = arrayListOf<Audio>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioItemViewHolder {
        val binding =
            ItemAudioFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AudioItemViewHolder(binding, audioItem)
    }

    override fun onBindViewHolder(holder: AudioItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Audio>) {
        if (!list.isNullOrEmpty()) {
            items.clear()
            items.addAll(list)
        }
        notifyDataSetChanged()
    }

}

class AudioItemViewHolder(
    private val binding: ItemAudioFileBinding,
    private val audioItem: AudioItem
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Audio) {
        binding.apply {
            title.text = item.name
            title.isSelected = true
            chapter.isSelected = true
        }

        binding.download.setOnClickListener {
            audioItem.onDownload(item)
        }

        binding.playOrPause.setOnClickListener {
            audioItem.onPlay(item)
        }

    }

}
