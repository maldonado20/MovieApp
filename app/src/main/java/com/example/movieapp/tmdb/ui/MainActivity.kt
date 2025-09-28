package com.example.movieapp.tmdb.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.databinding.ActivityMainBinding
import com.tuapp.tmdb.data.ResultState
import com.tuapp.tmdb.ui.MovieAdapter
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var b: ActivityMainBinding
    private val vm: PopularViewModel by viewModels()
    private val adapter = MovieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
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
                        is ResultState.Success -> {
                            b.swipeRefresh.isRefreshing = false
                            adapter.submit(st.data)
                        }
                        is ResultState.Error -> {
                            b.swipeRefresh.isRefreshing = false
                            Toast.makeText(this@MainActivity, st.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}
