package ayds.dodo.movieinfo.home.view

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie

interface MovieRatingHelper {
    fun getRatings(movie: OmdbMovie): StringBuilder
}