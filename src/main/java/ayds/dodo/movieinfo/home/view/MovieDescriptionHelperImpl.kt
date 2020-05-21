package ayds.dodo.movieinfo.home.view

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie

internal class MovieDescriptionHelperImpl : MovieDescriptionHelper {
    override fun getMovieDescriptionText(movie: OmdbMovie): String {
        return if (movie.title.isEmpty()) {
            "Movie not found"
        } else {
            val ratings = MovieRatingHelperImpl().getRatings(movie)
            var title = movie.title
            if (movie.isLocallyStoraged) {
                title = "[*]" + movie.title
            }
            ("<html>"
                    + "<body style='width: 400px;'>"
                    + title + " - " + movie.year + "<br><br>"
                    + "Director: " + movie.director + "<br><br>"
                    + "Actors: " + movie.actors + "<br><br>"
                    + "Ratings: <br>" + ratings.toString() + "<br>"
                    + movie.plot + " <br>"
                    + "Runtime: " + movie.runtime
                    + "</body>"
                    + "</html>")
        }
    }
}