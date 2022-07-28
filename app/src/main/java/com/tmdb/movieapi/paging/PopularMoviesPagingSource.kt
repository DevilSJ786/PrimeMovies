package com.tmdb.movieapi.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tmdb.movieapi.models.PopularList
import com.tmdb.movieapi.models.ResultX
import com.tmdb.movieapi.retrofit.API
import retrofit2.Response

class PopularMoviesPagingSource(private val api: API) : PagingSource<Int, ResultX>() {

    private val start = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultX> {

        val currentPage = params.key ?: start

        return try {

            val response: Response<PopularList> =
                api.getPopularMovies(page=currentPage)

            val moviesList: List<ResultX> = response.body()!!.results

            LoadResult.Page(
                data = moviesList,
                prevKey = if (currentPage == start) null else currentPage - 1,
                nextKey = if (moviesList.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(throwable = e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ResultX>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}