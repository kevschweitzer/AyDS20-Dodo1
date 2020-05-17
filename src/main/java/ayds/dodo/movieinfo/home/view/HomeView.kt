package ayds.dodo.movieinfo.home.view

import ayds.observer.Observable

interface HomeView {
    fun openView()
    fun error(msj:String)
    val movieTitle: String
    fun onUiEvent(): Observable<UiEvent>
}