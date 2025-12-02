package com.example.test_lab_week_12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    // Ganti LiveData jadi StateFlow
    private val _popularMovies = MutableStateFlow(emptyList<Movie>())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    init {
        fetchPopularMovies()
    }

    private fun fetchPopularMovies() {
        viewModelScope.launch {
            movieRepository.fetchMovies()
                .catch { exception ->
                    // Kalo error di tengah jalan, tangkep di sini
                    _error.value = "An exception occurred: ${exception.message}"
                }
                .collect { movies ->
                    // Kalo sukses, simpen datanya
                    _popularMovies.value = movies
                }
        }
    }
}