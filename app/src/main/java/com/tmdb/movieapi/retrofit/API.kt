package com.tmdb.movieapi.retrofit

import com.tmdb.movieapi.models.Anime
import com.tmdb.movieapi.models.CastList
import com.tmdb.movieapi.models.PopularList
import com.tmdb.movieapi.models.VideoList
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface API {

    @GET("3/movie/popular?api_key=473910cbc3db65a9f6898e311f96c47b")
    suspend fun getPopularMovies(
        @Query("language") lang: String? = "en-US",
        @Query("page") page: Int = 1,
        ):Response<PopularList>

    @GET("3/tv/popular?api_key=473910cbc3db65a9f6898e311f96c47b")
    suspend fun getPopularTvShows(
        @Query("language") lang: String? = "en-US",
        @Query("page") page: Int = 1
    ): Response<Anime>

    @GET("3/discover/tv?api_key=473910cbc3db65a9f6898e311f96c47b")
    suspend fun getAnime(
        @Query("with_genres") genres: String = "16",
        @Query("sort_by") sortBy: String? = "popularity.desc",
        @Query("first_air_date.gte") firstAirDateGreaterThan: String = "2010-01-01",
        @Query("page") page: Int = 1,
        @Query("language") lang: String? = "en-US",
        @Query("with_original_language") origLang: String = "en",
        @Query("include_null_first_air_dates") include: Boolean = false
    ): Response<Anime>



    @GET("3/movie/now_playing?api_key=473910cbc3db65a9f6898e311f96c47b")
    suspend fun getLiveMovies(
        @Query("language") lang: String? = "en-US",
        @Query("page") page: Int = 1
    ): Response<PopularList>

    @GET("3/tv/{tv_id}/videos?api_key=473910cbc3db65a9f6898e311f96c47b")
    suspend fun getTvShowDetail(
        @Path("tv_id") tvId: Int,
        @Query("language") lang: String? = "en-US"
    ): Response<VideoList>

    @GET("3/movie/{movie_id}/videos?api_key=473910cbc3db65a9f6898e311f96c47b")
    suspend fun getVideos(
        @Path("movie_id") movieId: Int?=526896,
        @Query("language") lang: String? = "en-US"
    ): Response<VideoList>
    @GET("3/movie/{movie_id}/credits?api_key=473910cbc3db65a9f6898e311f96c47b")
    suspend fun getMovieCast(
        @Path("movie_id") movieId: Int,
        @Query("language") lang: String? = "en-US"
    ): Response<CastList>

    @GET("3/tv/{tv_id}/credits?api_key=473910cbc3db65a9f6898e311f96c47b")
    suspend fun getTvShowsCast(
        @Path("tv_id") tvId: Int,
        @Query("language") lang: String? = "en-US"
    ): Response<CastList>

}
