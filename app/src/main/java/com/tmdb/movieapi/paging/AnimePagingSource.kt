package com.tmdb.movieapi.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tmdb.movieapi.models.Anime
import com.tmdb.movieapi.models.AnimeDetail
import com.tmdb.movieapi.retrofit.API
import retrofit2.Response

class AnimePagingSource(
    val api: API
) : PagingSource<Int, AnimeDetail>() {

    private val STARTING_PAGE = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeDetail> {

        val currentPage = params.key ?: STARTING_PAGE

        return try {

            val response: Response<Anime> =
                api.getAnime(page=currentPage)

            val moviesList: List<AnimeDetail> = response.body()!!.results

            LoadResult.Page<Int, AnimeDetail>(
                data = moviesList,
                prevKey = if (currentPage == STARTING_PAGE) null else currentPage - 1,
                nextKey = if (moviesList.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error<Int, AnimeDetail>(throwable = e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AnimeDetail>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}