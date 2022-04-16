package com.dicoding.latihan.submissionintermediate.view.story

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.latihan.submissionintermediate.R
import com.dicoding.latihan.submissionintermediate.response.ListStoryItem
import com.dicoding.latihan.submissionintermediate.view.detail.DetailActivity

class StoryAdapter(
    private val listReview: MutableList<ListStoryItem>,
    private val context: StoryActivity
) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
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
    ): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.story_view, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewholder: ViewHolder, position: Int) {
        //assign data to correspondence variable
        val data = listReview[position]
        val storyLogin = viewholder.tvItem1
        val storyAvatar = viewholder.tvItem2
        val storyDescription = viewholder.tvItem3
        storyLogin.text = data.name
        storyDescription.text = data.description
        Glide.with(viewholder.itemView.context).load(data.photoUrl).into(storyAvatar)

        //if RecyclerView clicked
        viewholder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_NAME, data.name)
            intent.putExtra(DetailActivity.EXTRA_DESC, data.description)
            intent.putExtra(DetailActivity.EXTRA_PHOTO, data.photoUrl)

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    viewholder.itemView.context as Activity,
                    Pair(storyAvatar, "photo"),
                    Pair(storyLogin, "user"),
                    Pair(storyDescription, "description")
                )
            context.startActivity(intent, optionsCompat.toBundle())
        }
    }

    //return list size
    override fun getItemCount(): Int = listReview.size

    //make variable for viewing
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItem1: TextView = view.findViewById(R.id.tvUsername)
        val tvItem2: ImageView = view.findViewById(R.id.story)
        val tvItem3: TextView = view.findViewById(R.id.description)
    }
}