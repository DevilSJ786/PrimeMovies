package com.tmdb.movieapi.models

data class PopularList(
    val page: Int,
    val results: List<ResultX>,
    val total_pages: Int,
    val total_results: Int
)