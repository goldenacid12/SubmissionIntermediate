package com.dicoding.latihan.submissionintermediate.view.story

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.latihan.submissionintermediate.databinding.StoryViewBinding
import com.dicoding.latihan.submissionintermediate.response.ListStoryItem

class StoryAdapter: PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var mListener: AdapterView.OnItemClickListener

    //function when clicked
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    interface OnItemClickListener : AdapterView.OnItemClickListener {
        fun onItemClick(v: Int)
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding =
            StoryViewBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(private val binding: StoryViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            binding.tvUsername.text = data.name
            binding.description.text = data.description
            Glide.with(binding.cardView.context).load(data.photoUrl).into(binding.story)
        }
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}

