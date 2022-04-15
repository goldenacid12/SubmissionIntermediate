package com.dicoding.latihan.submissionintermediate.view.story

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.latihan.submissionintermediate.R
import com.dicoding.latihan.submissionintermediate.response.ListStoryItem

class StoryAdapter (
    private val listReview: MutableList<ListStoryItem>,
    private val context: StoryActivity
    ) : RecyclerView.Adapter<StoryAdapter.ViewHolder>()
    {
        private lateinit var mListener: AdapterView.OnItemClickListener

        //function when clicked
        fun setOnItemClickListener(listener: AdapterView.OnItemClickListener) {
            mListener = listener
        }

        interface OnItemClickListener {
            fun onItemClick(v: Int)
        }

        override fun onCreateViewHolder(
            viewGroup: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val view =
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.story_view, viewGroup, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewholder: ViewHolder, position: Int) {
            //assign data to correspondence variable
            val data = listReview[position]
            val userLogin = viewholder.tvItem1
            val userAvatar = viewholder.tvItem2
            userLogin.text = data.name
            Glide.with(viewholder.itemView.context).load(data.photoUrl).into(userAvatar)

            //if RecyclerView clicked
            viewholder.itemView.setOnClickListener {
                /*val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, data.login)
                intent.putExtra(DetailActivity.EXTRA_FAV, data.id)
                intent.putExtra(DetailActivity.EXTRA_AVATAR, data.avatarUrl)
                context.startActivity(intent)*/
            }
        }

        //return list size
        override fun getItemCount(): Int = listReview.size

        //make variable for viewing
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvItem1: TextView = view.findViewById(R.id.tvUsername)
            val tvItem2: ImageView = view.findViewById(R.id.story)
        }
    }