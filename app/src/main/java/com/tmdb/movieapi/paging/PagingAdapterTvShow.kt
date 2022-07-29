package com.tmdb.movieapi.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tmdb.movieapi.R
import com.tmdb.movieapi.databinding.ItemQuoteLayoutBinding
import com.tmdb.movieapi.models.AnimeDetail
import com.tmdb.movieapi.utils.Constants.TMDB_POSTER_IMAGE_BASE_URL_W342

class PagingAdapterTvShow( private var onTvClick: (movieResult: AnimeDetail) -> Unit): PagingDataAdapter<AnimeDetail, PagingAdapterTvShow.ListviewHolder>(COMPARATOR) {

    class ListviewHolder(b: ItemQuoteLayoutBinding): RecyclerView.ViewHolder(b.root){
        val binding = b
    }

    override fun onBindViewHolder(holder: ListviewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.binding.imageView.setOnClickListener {
                onTvClick(item)
            }
            holder.binding.ratingText.text= item.vote_average.toString()
            holder.binding.imageView.load(TMDB_POSTER_IMAGE_BASE_URL_W342.plus(item.poster_path)){
                crossfade(true)
                placeholder(R.drawable.placeholder)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListviewHolder {
        val binding= ItemQuoteLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListviewHolder(binding)
    }
    companion object{
        private val COMPARATOR= object : DiffUtil.ItemCallback<AnimeDetail>(){
            override fun areItemsTheSame(oldItem: AnimeDetail, newItem: AnimeDetail): Boolean {
                return oldItem.id==newItem.id
            }

            override fun areContentsTheSame(oldItem: AnimeDetail, newItem: AnimeDetail): Boolean {
                return oldItem==newItem
            }

        }
    }


}