package com.tmdb.movieapi.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tmdb.movieapi.R
import com.tmdb.movieapi.databinding.ItemQuoteLayoutBinding
import com.tmdb.movieapi.models.ResultX
import com.tmdb.movieapi.utils.Constants.TMDB_POSTER_IMAGE_BASE_URL_W342

class PagingAdapter( private var onPosterClick: (movieResult: ResultX) -> Unit): PagingDataAdapter<ResultX, PagingAdapter.ListviewHolder>(COMPARATOR) {
    class ListviewHolder(b: ItemQuoteLayoutBinding):RecyclerView.ViewHolder(b.root){
        val binding = b
    }

    override fun onBindViewHolder(holder: ListviewHolder, position: Int) {
         val item = getItem(position)
        if (item != null) {
            holder.binding.imageView.setOnClickListener {
                onPosterClick(item)
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
        private val COMPARATOR= object :DiffUtil.ItemCallback<ResultX>(){
            override fun areItemsTheSame(oldItem: ResultX, newItem: ResultX): Boolean {
                return oldItem.id==newItem.id
            }

            override fun areContentsTheSame(oldItem: ResultX, newItem: ResultX): Boolean {
               return oldItem==newItem
            }

        }
    }
}