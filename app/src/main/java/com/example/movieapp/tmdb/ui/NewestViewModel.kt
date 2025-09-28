package com.example.movieapp.tmdb.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.Movie
import com.tuapp.tmdb.data.MovieRepository
import com.tuapp.tmdb.data.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewestViewModel : ViewModel() {

    private val repo = MovieRepository() // si luego usas Hilt, lo inyectamos

    private val _state = MutableStateFlow<ResultState<List<Movie>>>(ResultState.Loading)
    val state: StateFlow<ResultState<List<Movie>>> = _state

    fun load() {
        _state.value = ResultState.Loading
        viewModelScope.launch {
            _state.value = repo.getNewest()
        }
    }
}
