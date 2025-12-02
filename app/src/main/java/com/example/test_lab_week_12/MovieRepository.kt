package com.example.test_lab_week_12

import com.example.test_lab_week_12.api.MovieService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.Dispatchers

class MovieRepository(private val movieService: MovieService) {
    private val apiKey = "da772e5c821799c484dcbf3cbd674626"
    suspend fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            val popularMovies = movieService.getPopularMovies(apiKey)
            // Emit itu ibarat "nembakin" datanya ke yang minta
            emit(popularMovies.results)
        }.flowOn(Dispatchers.IO) // Jalanin di background thread
    }
}