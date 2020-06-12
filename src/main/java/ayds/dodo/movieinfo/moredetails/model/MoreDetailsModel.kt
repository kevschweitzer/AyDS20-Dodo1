package ayds.dodo.movieinfo.moredetails.model

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie
import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie
import ayds.dodo.movieinfo.moredetails.model.repository.TmdbRepositoryImp
import ayds.dodo.movieinfo.moredetails.model.repository.local.db.DataBase
import ayds.observer.Subject

interface MoreDetailsModel {
    fun getMoviePlot(movie : OmdbMovie)
}

internal class MoreDetailsModelImpl(private val repository: TmdbRepositoryImp) : MoreDetailsModel {

    private val movieSubject = Subject<TmdbMovie>()

    override fun getMoviePlot(movie: OmdbMovie) {
        repository.getMovie(movie)?.let {
            movieSubject.notify(it)
        }
    }

}