package com.example.movieapp.tmdb.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.data.Movie
import com.example.movieapp.databinding.ActivityNewestBinding
import com.tuapp.tmdb.data.ResultState
import com.tuapp.tmdb.ui.MovieAdapter
import kotlinx.coroutines.launch

class NewestActivity : ComponentActivity() {

    private lateinit var b: ActivityNewestBinding
    private val vm: NewestViewModel by viewModels()
    private val adapter = MovieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityNewestBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.rvMovies.layoutManager = LinearLayoutManager(this)
        b.rvMovies.adapter = adapter

        b.swipeRefresh.setOnRefreshListener { vm.load() }

        observeState()
        vm.load()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collect { st ->
                    when (st) {
                        is ResultState.Loading -> b.swipeRefresh.isRefreshing = true
                        is ResultState.Success<*> -> {
                            b.swipeRefresh.isRefreshing = false
                            adapter.submit(st.data as List<Movie>)
                        }
                        is ResultState.Error -> {
                            b.swipeRefresh.isRefreshing = false
                            Toast.makeText(this@NewestActivity, st.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}
