package com.example.movieapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

//Lista
data class MovieListResponse (
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)

//Peliculas básicas
data class MovieDto (
    val id: Long,
    val title: String,
    val overview: String?,
    val poster_path: String?,
    val vote_average: Float,
    val release_date: String? //fromato "YYYY-MM-DD"
)

//Interfaz de endpoints
interface TmdbService {

    //Mandamos a llamar las peliculas
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): MovieListResponse

    //Llamamos la peliculas ordenadas de mas reciente a menos
    @GET("discover/movie")
    suspend fun getRecentMovies(
        //LE AGREGAMOS LOS FILTROS
        @Query ("page") page: Int = 1,
        @Query("language") language: String = "es-ES",
        @Query("sort_by") sortBy: String = "primary_release_date.desc",
        @Query("include_adult") includeAdult: Boolean = false,

        ): MovieListResponse

}
