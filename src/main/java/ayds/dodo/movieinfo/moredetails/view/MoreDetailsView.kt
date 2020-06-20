package ayds.dodo.movieinfo.moredetails.view

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie
import ayds.observer.Observable

interface MoreDetailsView {
    var movie :OmdbMovie
    fun openView(movie : OmdbMovie)
    fun onUiEvent(): Observable<MoreDetailsUiEvent>
}