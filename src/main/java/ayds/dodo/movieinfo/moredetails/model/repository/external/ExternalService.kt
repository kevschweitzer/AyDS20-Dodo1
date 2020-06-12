package ayds.dodo.movieinfo.moredetails.model.repository.external

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie
import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie

interface ExternalService {
    fun getMovie(movie : OmdbMovie) : TmdbMovie
}