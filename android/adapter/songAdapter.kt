package com.elzaman.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elzaman.android.dataclass.Song
import com.elzaman.android.zamangameel.AlarmListner
import com.elzaman.android.zamangameel.MusicItemListner
import com.elzaman.android.zamangameel.databinding.RecyclerItemBinding

class songAdapter(val clicklistner: MusicItemListner, val context: Context,val alarmlistner: AlarmListner) :
    ListAdapter<Song, songAdapterViewHolder>(songAdapterDiffUtillCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): songAdapterViewHolder {
        return songAdapterViewHolder.from(parent)
    }
    override fun onBindViewHolder(holder: songAdapterViewHolder, position: Int) {
        holder.bind(getItem(position)!!,clicklistner,context,alarmlistner)
    }
}

class songAdapterViewHolder private constructor(val binding: RecyclerItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
         companion object{
             fun from(parent: ViewGroup): songAdapterViewHolder {
                 val layoutInflater = LayoutInflater.from(parent.context)
                 val binding = RecyclerItemBinding.inflate(layoutInflater, parent, false)
                 return songAdapterViewHolder(binding)
             }
         }

    fun bind(item: Song, clicklistner: MusicItemListner,context: Context, alarmlistner: AlarmListner) {
        binding.song = item
        binding.clickListener = clicklistner
        binding.alarmlistner= alarmlistner
        binding.executePendingBindings()

    }
}

class songAdapterDiffUtillCallBack : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.songId == newItem.songId
    }
    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {

        return oldItem == newItem
    }
}
