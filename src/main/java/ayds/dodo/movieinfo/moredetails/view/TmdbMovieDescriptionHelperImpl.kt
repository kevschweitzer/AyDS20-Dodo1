package ayds.dodo.movieinfo.moredetails.view

import ayds.dodo.movieinfo.moredetails.model.entities.NonExistentTmdbMovie
import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie

class TmdbMovieDescriptionHelperImpl : TmdbMovieDescriptionHelper {
    companion object {
        const val HTML_OPEN = "<html>"
        const val BODY_STYLE = "<body style=\"width: 400px;\">"
        const val FONT_STYLE_OPEN = "<font face=\"arial\">"
        const val FONT_CLOSE = "</font>"
        const val BOLD_OPEN = "<b>"
        const val BOLD_CLOSE = "</b>"
        const val NEW_LINE = "\n"
        const val IMAGE_URL_BASE = "https://image.tmdb.org/t/p/w400/"
    }

    override fun getMovieDescriptionText(movie: TmdbMovie): String {
        return if (movie !is NonExistentTmdbMovie) {
            val builder = StringBuilder()
            with(builder) {
                val plot = movie.plot.replace("\\n", "\n")
                val textWithBold = plot
                        .replace(movie.title.toRegex(), BOLD_OPEN + movie.title.toUpperCase() + BOLD_CLOSE)
                append(HTML_OPEN + BODY_STYLE)
                append(FONT_STYLE_OPEN)
                append(textWithBold)
                append(FONT_CLOSE)
                append(NEW_LINE)
                append(createMoviePosterAnchor(movie))
                toString()
            }
        } else {
            movie.plot
        }
    }

    private fun createMoviePosterAnchor(movie: TmdbMovie): String? {
        return "<a href=" + IMAGE_URL_BASE + movie.posterUrl + ">View Movie Poster</a>"
    }
}