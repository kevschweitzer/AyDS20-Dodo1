package ayds.dodo.movieinfo.home.view

import ayds.dodo.movieinfo.home.model.entities.NonExistentOmdbMovie
import ayds.dodo.movieinfo.home.model.entities.OmdbMovie

internal class MovieDescriptionHelperImpl : MovieDescriptionHelper {
    companion object {
        const val HTML_OPEN = "<html>"
        const val HTML_DESCRIPTION_STYLE = "<body style='width: 400px;'>"
        const val HTML_BR = "<br>"
        const val HTML_BR_TWICE = HTML_BR + HTML_BR
        const val HTML_DIRECTOR = "Director: "
        const val HTML_ACTOR = "Actors: "
        const val HTML_RATINGS = "Ratings: "
        const val HTML_RUNTIME = "Runtime: "
        const val HTML_BODY_CLOSE = "</body>"
        const val HTML_CLOSE = "</html>"
        const val HTML_DASH = " - "
    }

    override fun getMovieDescriptionText(movie: OmdbMovie): String {
        return if (movie == NonExistentOmdbMovie) {
            "Movie not found"
        } else {
            val ratings = MovieRatingHelperImpl().getRatings(movie)
            var title = movie.title
            if (movie.isLocallyStoraged) {
                title = "[*]" + movie.title
            }
            (HTML_OPEN
                    + HTML_DESCRIPTION_STYLE
                    + title + HTML_DASH + movie.year + HTML_BR_TWICE
                    + HTML_DIRECTOR + movie.director + HTML_BR_TWICE
                    + HTML_ACTOR + movie.actors + HTML_BR_TWICE
                    + HTML_RATINGS + HTML_BR + ratings.toString() + HTML_BR
                    + movie.plot + HTML_BR
                    + HTML_RUNTIME + movie.runtime
                    + HTML_BODY_CLOSE
                    + HTML_CLOSE)
        }
    }
}