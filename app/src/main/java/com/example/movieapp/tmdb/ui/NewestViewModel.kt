package com.tuapp.tmdb.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuapp.tmdb.data.MovieRepository
import com.tuapp.tmdb.data.ResultState
import com.tuapp.tmdb.data.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewestViewModel : ViewModel() {

    private val repo = MovieRepository()

    private val _state = MutableStateFlow<ResultState<List<Movie>>>(ResultState.Loading)
    val state: StateFlow<ResultState<List<Movie>>> = _state

    fun load() {
        _state.value = ResultState.Loading
        viewModelScope.launch {
            _state.value = repo.getNewest()
        }
    }
}
