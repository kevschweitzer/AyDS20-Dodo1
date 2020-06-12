package ayds.dodo.movieinfo.moredetails.view

import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie

interface TmdbMovieDescriptionHelper {
    fun getMovieDescriptionText(movie: TmdbMovie): String
}