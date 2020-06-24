package ayds.dodo.movieinfo.moredetails.controller

import ayds.dodo.movieinfo.moredetails.model.MoreDetailsModel
import ayds.dodo.movieinfo.moredetails.view.MoreDetailsUiEvent
import ayds.dodo.movieinfo.moredetails.view.MoreDetailsView
import ayds.observer.Observer
import java.awt.Desktop
import java.io.IOException
import java.net.URI

interface MoreDetailsController

internal class MoreDetailsControllerImpl(
        private val moreDetailsView: MoreDetailsView,
        private val moreDetailsModel: MoreDetailsModel
) : MoreDetailsController {

    private val observer: Observer<MoreDetailsUiEvent> = object : Observer<MoreDetailsUiEvent> {
        override fun update(value: MoreDetailsUiEvent) {
            when (value) {
                MoreDetailsUiEvent.SEARCH_ACTION -> onSearchMovieAction()
                MoreDetailsUiEvent.POSTER_ACTION -> showPoster()
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

    private fun showPoster() {
        val movie = moreDetailsModel.getLastMovie()
        try {
            Desktop.getDesktop().browse(URI.create(moreDetailsModel.getPosterUrl()))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}