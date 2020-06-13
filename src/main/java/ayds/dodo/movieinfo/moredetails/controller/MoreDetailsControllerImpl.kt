package ayds.dodo.movieinfo.moredetails.controller

import ayds.dodo.movieinfo.home.view.HomeView
import ayds.dodo.movieinfo.moredetails.model.MoreDetailsModel
import ayds.dodo.movieinfo.moredetails.view.MoreDetailsUiEvent
import ayds.dodo.movieinfo.moredetails.view.MoredetailsView
import ayds.observer.Observer

interface MoreDetailsController

internal class MoreDetailsControllerImpl(
        private val moreDetailsView: MoredetailsView,private val moreDetailsModel: MoreDetailsModel
) : MoreDetailsController{
    //Esto no se si va porque more details no tiene eventos a los que responder.
    // Crear UIEvente de la more details view 
    private val observer: Observer<MoreDetailsUiEvent> = object : Observer<MoreDetailsUiEvent> {
        override fun update(value: MoreDetailsUiEvent) {
            when (value) {
                MoreDetailsUiEvent.SEARCH_ACTION -> onSearchMovieAction()
            }
        }
    }

    init {
        moreDetailsView.onUiEvent().subscribe(observer)
    }

    private fun onSearchMovieAction() {
        Thread {
            moreDetailsModel.getMoviePlot(moreDetailsView.movie)
        }.start()
    }
}