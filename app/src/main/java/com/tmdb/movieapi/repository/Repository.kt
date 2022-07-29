package com.tmdb.movieapi.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.tmdb.movieapi.models.*
import com.tmdb.movieapi.paging.AnimePagingSource
import com.tmdb.movieapi.paging.PopularMoviesPagingSource
import com.tmdb.movieapi.paging.PopularTvShowPagingSource
import com.tmdb.movieapi.retrofit.API
import com.tmdb.movieapi.utils.Response
import javax.inject.Inject

class Repository @Inject constructor(private val api: API) {

    private val listLiveData = MutableLiveData<Response<PopularList>>()
    val list: LiveData<Response<PopularList>>
        get() = listLiveData
    private val videoLiveData = MutableLiveData<Response<VideoList>>()
    val videoList: LiveData<Response<VideoList>>
        get() = videoLiveData
    private val castLiveData = MutableLiveData<Response<CastList>>()
    val castList: LiveData<Response<CastList>>
        get() = castLiveData
    private val searchMovieLiveData = MutableLiveData<Response<PopularList>>()
    val searchMoviesList: LiveData<Response<PopularList>>
        get() = searchMovieLiveData
    private val searchTvLiveData = MutableLiveData<Response<Anime>>()
    val searchTvList: LiveData<Response<Anime>>
        get() = searchTvLiveData

    suspend fun getTvSearch(searchQuery:String){
        try {
            val result = api.fetchTvSearchedResults(searchQuery=searchQuery)
            if (result.body() != null) {
                searchTvLiveData.postValue(Response.Success(result.body()!!))
            }
        }catch (e:Exception){
            searchTvLiveData.postValue(Response.Error(e.message.toString()))
        }
    }
    suspend fun getMovieSearch(searchQuery:String){
        try {
            val result = api.fetchMovieSearchedResults(searchQuery=searchQuery)
            if (result.body() != null) {
                searchMovieLiveData.postValue(Response.Success(result.body()!!))
            }
        }catch (e:Exception){
            searchMovieLiveData.postValue(Response.Error(e.message.toString()))
        }
    }

    suspend fun getLiveList() {
        try {
            val result = api.getLiveMovies()
            if (result.body() != null) {
                listLiveData.postValue(Response.Success(result.body()!!))
            }
        } catch (e: Exception) {
            listLiveData.postValue(Response.Error(e.message.toString()))
        }
    }

    suspend fun getMovieCastList(id: Int) {
        try {
            val result = api.getMovieCast(movieId = id)
            if (result.body() != null) {
                castLiveData.postValue(Response.Success(result.body()!!))
            }
        } catch (e: Exception) {
            castLiveData.postValue(Response.Error(e.message.toString()))
        }
    }

    suspend fun getTvCastList(id: Int) {
        try {
            val result = api.getTvShowsCast(tvId = id)
            if (result.body() != null) {
                castLiveData.postValue(Response.Success(result.body()!!))
            }
        } catch (e: Exception) {
            castLiveData.postValue(Response.Error(e.message.toString()))
        }
    }


    suspend fun getVideoList(id: Int) {
        try {
            val result = api.getVideos(movieId = id)
            if (result.body() != null) {
                videoLiveData.postValue(Response.Success(result.body()!!))
            }
        } catch (e: Exception) {
            videoLiveData.postValue(Response.Error(e.message.toString()))
        }
    }

    suspend fun getTvVideoList(id: Int) {
        try {
            val result = api.getTvShowDetail(tvId = id)
            if (result.body() != null) {
                videoLiveData.postValue(Response.Success(result.body()!!))
            }
        } catch (e: Exception) {
            videoLiveData.postValue(Response.Error(e.message.toString()))
        }
    }

    fun fetchPopularMoviesPaging(): LiveData<PagingData<ResultX>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = { PopularMoviesPagingSource(api) }).liveData
    }

    fun fetchPopularTvShowPaging(): LiveData<PagingData<AnimeDetail>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = { PopularTvShowPagingSource(api) }).liveData
    }

    fun fetchAnimePaging(): LiveData<PagingData<AnimeDetail>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = { AnimePagingSource(api) }).liveData
    }

}