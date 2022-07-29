package com.tmdb.movieapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tmdb.movieapi.R
import com.tmdb.movieapi.databinding.ItemQuoteLayoutBinding
import com.tmdb.movieapi.models.Anime
import com.tmdb.movieapi.models.AnimeDetail
import com.tmdb.movieapi.models.PopularList
import com.tmdb.movieapi.models.ResultX
import com.tmdb.movieapi.utils.Constants

class SearchAdapter(private val mList: Anime, private var onPosterClick: (movieResult: AnimeDetail) -> Unit) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    class ViewHolder(b: ItemQuoteLayoutBinding) : RecyclerView.ViewHolder(b.root) {
        val binding = b
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemQuoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList.results[position]
        holder.binding.imageView.setOnClickListener {
            onPosterClick(item)
        }
        holder.binding.ratingText.text= item.vote_average.toString()
        holder.binding.imageView.load(Constants.TMDB_POSTER_IMAGE_BASE_URL_W342.plus(item.poster_path)){
            crossfade(true)
            placeholder(R.drawable.placeholder)
        }
    }

    override fun getItemCount(): Int {
        return mList.results.size
    }
}
class SearchAdapterMovie(private val mList: PopularList, private var onPosterClick: (movieResult:ResultX) -> Unit) : RecyclerView.Adapter<SearchAdapterMovie.ViewHolder>() {

    class ViewHolder(b: ItemQuoteLayoutBinding) : RecyclerView.ViewHolder(b.root) {
        val binding = b
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemQuoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList.results[position]
        holder.binding.imageView.setOnClickListener {
            onPosterClick(item)
        }
        holder.binding.ratingText.text= item.vote_average.toString()
        holder.binding.imageView.load(Constants.TMDB_POSTER_IMAGE_BASE_URL_W342.plus(item.poster_path)){
            crossfade(true)
            placeholder(R.drawable.placeholder)
        }
    }

    override fun getItemCount(): Int {
        return mList.results.size
    }
}
