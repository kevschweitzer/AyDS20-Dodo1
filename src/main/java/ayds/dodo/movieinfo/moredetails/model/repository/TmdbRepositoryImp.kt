package ayds.dodo.movieinfo.moredetails.model.repository

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie
import ayds.dodo.movieinfo.moredetails.model.entities.NonExistentTmdbMovie
import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie
import ayds.dodo1.TmdbData.external.ExternalService
import ayds.dodo.movieinfo.moredetails.model.repository.local.LocalStorage

class TmdbRepositoryImp(
        private val localStorage: LocalStorage,
        private val externalService: ExternalService
) : TmdbRepository {

    override fun getMovie(movie: OmdbMovie): TmdbMovie? {
        var searchedMovie = localStorage.getTmdbMovie(movie.title)
        if (searchedMovie === NonExistentTmdbMovie) {
            searchedMovie = externalService.getMovie(movie)
            localStorage.saveMovieInfo(searchedMovie)
        }
        return searchedMovie
    }
}