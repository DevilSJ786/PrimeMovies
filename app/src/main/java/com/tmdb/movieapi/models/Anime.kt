package com.tmdb.movieapi.models

data class Anime(
    val page: Int,
    val results: List<AnimeDetail>,
    val total_pages: Int,
    val total_results: Int
)