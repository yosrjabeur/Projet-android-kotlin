package com.example.monapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monapp.models.ActeurModel
import com.example.monapp.models.Movie
import com.example.monapp.models.SeriesModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainViewModel : ViewModel() {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val api = retrofit.create(Api::class.java)
    private val api_key = "b57151d36fecd1b693da830a2bc5766f"


    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun getImageUrl(path: String?, size: String = "w500"): String {
        return "https://image.tmdb.org/t/p/$size$path"
    }

    // Films
    private val _movies = MutableStateFlow<List<Movie>>(listOf())
    val movies: StateFlow<List<Movie>> = searchQuery
        .combine(_movies) { query, movies ->
            if (query.isBlank()) movies else movies.filter { it.title.contains(query, ignoreCase = true) }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun getTrendingMovies() {
        viewModelScope.launch {
            try {
                val response = api.lastmovies(api_key)
                _movies.value = response.results
            } catch (e: Exception) {
                Log.e("MainViewModel", "Erreur lors du chargement des films : ${e.message}")
            }
        }
    }

    // Acteurs
    private val _acteurs = MutableStateFlow<List<ActeurModel>>(listOf())
    val acteurs: StateFlow<List<ActeurModel>> = searchQuery
        .combine(_acteurs) { query, acteurs ->
            if (query.isBlank()) acteurs else acteurs.filter { it.name.contains(query, ignoreCase = true) }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun lastActors() {
        viewModelScope.launch {
            _acteurs.value = api.lastActors(api_key).results
        }
    }

    // Séries

    private val _series = MutableStateFlow<List<SeriesModel>>(listOf())
    val series: StateFlow<List<SeriesModel>> = searchQuery
        .combine(_series) { query, series ->
            if (query.isBlank()) series else series.filter { it.name.contains(query, ignoreCase = true) }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun lastSeries() {
        viewModelScope.launch {
            _series.value = api.lastSeries(api_key).results
        }
    }


    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

}
