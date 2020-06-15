package ayds.dodo.movieinfo.moredetails.controller

import ayds.dodo.movieinfo.moredetails.model.MoreDetailsModel
import ayds.dodo.movieinfo.moredetails.view.MoreDetailsUiEvent
import ayds.dodo.movieinfo.moredetails.view.MoreDetailsView
import ayds.observer.Observer

interface MoreDetailsController

internal class MoreDetailsControllerImpl(
        private val moreDetailsView: MoreDetailsView, private val moreDetailsModel: MoreDetailsModel
) : MoreDetailsController{
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