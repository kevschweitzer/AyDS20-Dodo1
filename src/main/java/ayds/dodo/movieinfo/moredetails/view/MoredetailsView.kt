package ayds.dodo.movieinfo.moredetails.view

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie
import ayds.dodo.movieinfo.home.view.UiEvent
import ayds.observer.Observable

interface MoredetailsView {
    fun openView(movie: OmdbMovie)
    fun onUiEvent(): Observable<MoreDetailsUiEvent>
}