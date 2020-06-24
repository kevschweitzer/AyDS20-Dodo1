package ayds.dodo.movieinfo.moredetails.model

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie
import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie
import ayds.dodo.movieinfo.moredetails.model.repository.TmdbRepositoryImp
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel {

    fun getMoviePlot(movie : OmdbMovie)

    fun movieObservable(): Observable<TmdbMovie>

    fun getLastMovie(): TmdbMovie?

    fun getPosterUrl():String
}

internal class MoreDetailsModelImpl(private val repository: TmdbRepositoryImp) : MoreDetailsModel {

    private val IMAGE_URL_BASE = "https://image.tmdb.org/t/p/w400/"

    private val movieSubject = Subject<TmdbMovie>()

    override fun getMoviePlot(movie: OmdbMovie) {
        repository.getMovie(movie)?.let {
            movieSubject.notify(it)
        }
    }

    override fun getPosterUrl(): String {
        return IMAGE_URL_BASE + (movieSubject.lastValue()?.posterUrl)
    }

    override fun movieObservable(): Observable<TmdbMovie> = movieSubject

    override fun getLastMovie(): TmdbMovie? = movieSubject.lastValue()
}