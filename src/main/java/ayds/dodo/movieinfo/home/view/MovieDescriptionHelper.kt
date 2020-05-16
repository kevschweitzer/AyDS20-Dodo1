package ayds.dodo.movieinfo.home.view

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie

interface MovieDescriptionHelper {
    fun getMovieDescriptionText(movie: OmdbMovie): String
}