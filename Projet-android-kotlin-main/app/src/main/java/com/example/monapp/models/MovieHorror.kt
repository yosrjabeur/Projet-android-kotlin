package com.example.monapp.models

data class MovieHorror(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)