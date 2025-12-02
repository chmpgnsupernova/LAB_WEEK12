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
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.fetchMovies()
                .catch { exception ->
                    _error.value = "An exception occurred: ${exception.message}"
                }
                .collect { movies ->
                    // 🔥 START ASSIGNMENT LOGIC 🔥

                    // 1. Ambil tahun sekarang (misal: "2025")
                    val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR).toString()

                    // 2. Filter & Sort datanya
                    val finalMovies = movies
                        .filter { movie ->
                            // Cuma ambil film yang rilis tahun ini
                            movie.releaseDate?.startsWith(currentYear) == true
                        }
                        .sortedByDescending {
                            // Urutin berdasarkan popularitas tertinggi
                            it.popularity
                        }

                    // 3. Masukin data yang udah mateng ke StateFlow
                    _popularMovies.value = finalMovies

                    // 🔥 END ASSIGNMENT LOGIC 🔥
                }
        }
    }
}