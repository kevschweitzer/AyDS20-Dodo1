package ayds.dodo.movieinfo.home.view

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie

internal class MovieDescriptionHelperImpl : MovieDescriptionHelper {
    override fun getMovieDescriptionText(movie: OmdbMovie): String {
        if (movie.title.isEmpty()) {
            return "Movie not found"
        } else {
            val ratingHelper = MovieRatingHelperImpl()
            val ratings = ratingHelper.getRatings(movie)
            var title = movie.title
            if (movie.isLocallyStoraged) {
                title = "[*]" + movie.title
            }
            return ("<html>"
                        +"<body style='width: 400px;'>"
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