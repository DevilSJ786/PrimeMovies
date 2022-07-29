package com.tmdb.movieapi.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tmdb.movieapi.models.Anime
import com.tmdb.movieapi.models.PopularList
import com.tmdb.movieapi.repository.Repository
import com.tmdb.movieapi.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository:Repository,
) : ViewModel() {
    var isMovie = true
    fun search(isMovie: Boolean, searchQuery: String) = viewModelScope.launch {
            if (isMovie) {
                val data = async { repository.getTvSearch(searchQuery) }
                data.await()
            } else {
                val data = async { repository.getMovieSearch(searchQuery) }
                data.await()
            }
        }

    val tvList: LiveData<Response<Anime>>
        get() = repository.searchTvList
    val movieList: LiveData<Response<PopularList>>
        get() = repository.searchMoviesList

}