package ayds.dodo.movieinfo.home.view

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie

class MovieRatingHelperImpl : MovieRatingHelper {
    override fun  getRatings(movie: OmdbMovie): StringBuilder {
        val ratings = StringBuilder()
        for (rating in movie.ratings) {
            when (rating.source) {
                "Internet Movie Database" -> {
                    val score = rating.value.split("/").toTypedArray()
                    ratings.append("IMDB").append(" ").append(score[0]).append("\n")
                }
                "Rotten Tomatoes" -> ratings.append(rating.source).append(" ").append(rating.value).append("\n")
                "Metacritic" -> {
                    val score = rating.value.split("/").toTypedArray()
                    ratings.append(rating.source).append(" ").append(score[0]).append("%").append("\n")
                }
                else -> ratings.append(rating.source).append(" ").append(rating.value).append("\n")
            }
        }
        return ratings
    }
}