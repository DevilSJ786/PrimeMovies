package com.tmdb.movieapi.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tmdb.movieapi.models.CastList
import com.tmdb.movieapi.models.VideoList
import com.tmdb.movieapi.repository.Repository
import com.tmdb.movieapi.utils.Response
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class MovieViewModel @AssistedInject constructor(
    @Assisted private val id: Int,
    @Assisted private val tv: Boolean,
    private val repository: Repository
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(id: Int, tv: Boolean): MovieViewModel
    }

    companion object {
        fun providesFactory(assistedFactory: Factory, id: Int, tv: Boolean) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(id = id, tv = tv) as T
                }
            }
    }

    init {
        viewModelScope.launch {
            if (tv) {
                val data = async { repository.getTvVideoList(id) }
                data.await()
                val list = async { repository.getTvCastList(id) }
                list.await()
            } else {
                val data = async { repository.getVideoList(id) }
                data.await()
                val list = async { repository.getMovieCastList(id) }
                list.await()
            }
        }
    }

    val videolist: LiveData<Response<VideoList>>
        get() = repository.videoList
    val castList: LiveData<Response<CastList>>
        get() = repository.castList

}