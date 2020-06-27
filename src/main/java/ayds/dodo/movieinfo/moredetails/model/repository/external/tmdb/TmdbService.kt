package ayds.dodo.movieinfo.moredetails.model.repository.external.tmdb

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie
import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie
import ayds.dodo.movieinfo.moredetails.model.repository.external.ExternalService
import retrofit2.Response

internal class TmdbService(
        private val tmdbAPI: TheMovieDBAPI,
        private val tmdbMovieResolver: TmdbResponseToTmdbMovie
) : ExternalService{

    override fun getMovie(movie: OmdbMovie): TmdbMovie {
        val callResponse = getTmdbMovieFromService(movie.title)
        return tmdbMovieResolver.getMovie(movie, callResponse?.body())
    }

    private fun getTmdbMovieFromService(title: String) = tmdbAPI.getTerm(title)?.execute()
}