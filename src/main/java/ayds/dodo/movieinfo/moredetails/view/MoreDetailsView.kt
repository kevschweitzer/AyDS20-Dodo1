package ayds.dodo.movieinfo.moredetails.view

import ayds.observer.Observable

interface MoreDetailsView {
    fun onUiEvent(): Observable<MoreDetailsUiEvent>
}