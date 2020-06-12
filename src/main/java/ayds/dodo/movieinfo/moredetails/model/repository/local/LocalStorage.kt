package ayds.dodo.movieinfo.moredetails.model.repository.local

import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie

interface LocalStorage {
    fun getTmdbMovie(title:String) : TmdbMovie
    fun saveMovieInfo(movie : TmdbMovie)
}