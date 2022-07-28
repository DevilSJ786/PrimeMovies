package com.tmdb.movieapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tmdb.movieapi.databinding.LayoutLoadingStateBinding

class PagingStateAdapter(val retry: () -> Unit) :
    LoadStateAdapter<PagingStateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(
            LayoutLoadingStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class ViewHolder(private val binding: LayoutLoadingStateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) = binding.apply {
            retryButton.setOnClickListener { retry() }
            when (loadState) {
                is LoadState.Loading -> {
                    progressBar.isGone = false
                    retryButton.isGone = true
                }

                is LoadState.Error -> {
                    progressBar.isGone = true
                    retryButton.isGone = false
                }
                is LoadState.NotLoading->{}
            }
        }
    }
}