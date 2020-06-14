package ayds.dodo.movieinfo.moredetails.model

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie
import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie
import ayds.dodo.movieinfo.moredetails.model.repository.TmdbRepositoryImp
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel {

    fun getMoviePlot(movie : OmdbMovie)

    fun movieObservable(): Observable<TmdbMovie>

}

internal class MoreDetailsModelImpl(private val repository: TmdbRepositoryImp) : MoreDetailsModel {

    private val movieSubject = Subject<TmdbMovie>()

    override fun getMoviePlot(movie: OmdbMovie) {
        repository.getMovie(movie)?.let {
            movieSubject.notify(it)
        }
    }

    override fun movieObservable(): Observable<TmdbMovie> = movieSubject
}