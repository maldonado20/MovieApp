package com.tuapp.tmdb.data

import com.example.movieapp.data.remote.NetworkModule
import com.example.movieapp.data.remote.MovieDto
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException
import com.example.movieapp.data.Movie

//Datos de las peliculas


sealed class ResultState<out T> {
    object Loading : ResultState<Nothing>()
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error(val message: String) : ResultState<Nothing>()
}

class MovieRepository {

    private val imageBase = "https://image.tmdb.org/t/p/w500"

    private fun map(dto: MovieDto): Movie = Movie(
        id = dto.id,
        title = dto.title,
        overview = dto.overview ?: "Sin descripción",
        posterUrl = dto.poster_path?.let { "$imageBase$it" },
        rating = dto.vote_average ?: 0f,
        releaseDate = dto.release_date
    )

    // POPULARES
    suspend fun getPopular(): ResultState<List<Movie>> = safeCall {
        val res = NetworkModule.tmdbService.getPopularMovies()
        res.results.map(::map)
    }

    // MÁS NUEVAS ordenadas por fecha
    suspend fun getNewest(): ResultState<List<Movie>> = safeCall {
        val res = NetworkModule.tmdbService.getPopularMovies()
        res.results.map(::map)
    }

    private suspend inline fun <T> safeCall(crossinline block: suspend () -> T): ResultState<T> {
        return try {
            ResultState.Success(block())
        } catch (e: retrofit2.HttpException) {
            val msg = when (e.code()) {
                401 -> "Clave de API inválida (401)"
                404 -> "No se encontraron resultados (404)"
                429 -> "Demasiadas peticiones (429)"
                in 500..599 -> "Error en el servidor (${e.code()})"
                else -> "Error HTTP (${e.code()})"
            }
            ResultState.Error(msg)
        } catch (e: java.io.IOException) {
            ResultState.Error("Sin conexión o tiempo de espera agotado")
        } catch (e: Exception) {
            ResultState.Error("Error inesperado: ${e.message}")
        }
    }
}
