package ayds.dodo.movieinfo.home.view

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie
import ayds.dodo.movieinfo.home.model.entities.Rating
import ayds.dodo.movieinfo.home.view.MovieDescriptionHelperImpl.Companion.HTML_BR

class MovieRatingHelperImpl : MovieRatingHelper {
    override fun getRatings(movie: OmdbMovie): StringBuilder {
        val ratings = StringBuilder()
        var parser: MovieRatingParser
        for (rating in movie.ratings) {
            parser = when (rating.source) {
                "Internet Movie Database" -> {
                    IMDbRatingParser()
                }
                "Metacritic" -> {
                    MetacriticRatingParser()
                }
                else -> DefaultRatingParser()
            }
            ratings.append(parser.parseMovieRating(rating)).append(HTML_BR)
        }
        return ratings
    }
}

interface MovieRatingParser {
    fun parseMovieRating(value: Rating): String
}

internal class IMDbRatingParser : MovieRatingParser {
    override fun parseMovieRating(rating: Rating): String {
        val score = rating.value.split("/").toTypedArray()
        return "IMDB " + score[0]
    }
}

internal class MetacriticRatingParser : MovieRatingParser {
    override fun parseMovieRating(rating: Rating): String {
        val score = rating.value.split("/").toTypedArray()
        return rating.source + " " + score[0] + "%"
    }
}

internal class DefaultRatingParser : MovieRatingParser {
    override fun parseMovieRating(rating: Rating): String {
        return rating.source + " " + rating.value
    }
}