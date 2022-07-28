package com.tmdb.movieapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tmdb.movieapi.R
import com.tmdb.movieapi.databinding.CastItemBinding
import com.tmdb.movieapi.models.Cast
import com.tmdb.movieapi.utils.Constants

class CastAdapter(private val mList: List<Cast>) : RecyclerView.Adapter<CastAdapter.ViewHolder>() {

    class ViewHolder(b: CastItemBinding) : RecyclerView.ViewHolder(b.root) {
        val binding = b
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.castName.text = mList[position].name
        holder.binding.castAction.text=mList[position].character
        holder.binding.imageviewCast.load(Constants.TMDB_CAST_IMAGE_BASE_URL_W185.plus(mList[position].profile_path)){
            crossfade(true)
            placeholder(R.drawable.placeholder)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}