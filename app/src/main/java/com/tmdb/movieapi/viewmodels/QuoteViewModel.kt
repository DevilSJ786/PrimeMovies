package com.tmdb.movieapi.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tmdb.movieapi.models.PopularList
import com.tmdb.movieapi.repository.Repository
import com.tmdb.movieapi.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    init {
        getAllLists()
    }

    fun retry() = getAllLists()
    private fun getAllLists() = viewModelScope.launch(Dispatchers.IO) {
        val data = async { repository.getLiveList() }
        data.await()
    }

    val pagingList = repository.fetchPopularMoviesPaging().cachedIn(viewModelScope)
    val pagingTvList = repository.fetchPopularTvShowPaging().cachedIn(viewModelScope)
    val pagingAnimeList = repository.fetchAnimePaging().cachedIn(viewModelScope)

    val liveMovies: LiveData<Response<PopularList>>
        get() = repository.list

}