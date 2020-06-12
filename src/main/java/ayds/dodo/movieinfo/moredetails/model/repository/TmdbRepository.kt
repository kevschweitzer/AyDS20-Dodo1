package ayds.dodo.movieinfo.moredetails.model.repository

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie
import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie

interface TmdbRepository {
    fun getMovie(movie : OmdbMovie) : TmdbMovie?
}