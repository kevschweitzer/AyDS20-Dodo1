package ayds.dodo.movieinfo.moredetails.model.repository.external.tmdb

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie
import ayds.dodo.movieinfo.moredetails.model.entities.NonExistentTmdbMovie
import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import java.io.IOException

internal interface TmdbResponseToTmdbMovie {
    fun getMovie(movie : OmdbMovie, body: String?) : TmdbMovie
}

class TmdbResponseToTmdbMovieImp : TmdbResponseToTmdbMovie {
    private val IMAGE_URL_BASE = "https://image.tmdb.org/t/p/w400/"
    private val RESULTS_JSON = "results"
    private val OVERVIEW_JSON = "overview"
    private val POSTER_PATH_JSON = "poster_path"
    private val BACKDROP_PATH_JSON = "backdrop_path"
    private val RELEASE_DATE = "release_date"

    override fun getMovie(movie : OmdbMovie, body : String?): TmdbMovie {
        try {
            val resultIterator: Iterator<JsonElement> = getJsonElementIterator(body) as Iterator<JsonElement>
            val result: JsonObject? = getInfoFromTmdb(resultIterator, movie)
            if (result != null) {
                val backdropPath: String? = getBackdrop(result)
                val posterPath = result[POSTER_PATH_JSON]
                val extract = result[OVERVIEW_JSON]
                if (extract !== JsonNull.INSTANCE && posterPath !== JsonNull.INSTANCE) {
                    val path = if (backdropPath != null && backdropPath != "") IMAGE_URL_BASE + backdropPath else NonExistentTmdbMovie.IMAGE_NOT_FOUND
                    return createTmdbMovie(movie, extract.asString, path, posterPath.asString)
                }
            }
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return NonExistentTmdbMovie
    }

    private fun getBackdrop(result: JsonObject): String? {
        val backdropPathJson = result[BACKDROP_PATH_JSON]
        var backdropPath: String? = null
        if (!backdropPathJson.isJsonNull) {
            backdropPath = backdropPathJson.asString
        }
        return backdropPath
    }

    private fun getInfoFromTmdb(resultIterator: Iterator<JsonElement>, movie: OmdbMovie): JsonObject? {
        var result: JsonObject? = null
        while (resultIterator.hasNext()) {
            result = resultIterator.next().asJsonObject
            val year = result[RELEASE_DATE].asString.split("-").toTypedArray()[0]
            if (year == movie.year) break
        }
        return result
    }

    @Throws(IOException::class)
    private fun getJsonElementIterator(body: String?): Iterator<JsonElement?> {
        val gson = Gson()
        val jobj = gson.fromJson(body, JsonObject::class.java)
        return jobj[RESULTS_JSON].asJsonArray.iterator()
    }

    private fun createTmdbMovie(movie: OmdbMovie, text: String, path: String, posterPath: String): TmdbMovie {
        val tmdbMovie = TmdbMovie()
        tmdbMovie.title = movie.title
        tmdbMovie.plot = text
        tmdbMovie.imageUrl = path
        tmdbMovie.posterUrl = posterPath
        return tmdbMovie
    }
}