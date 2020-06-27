package ayds.dodo.movieinfo.moredetails.controller

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie
import ayds.dodo.movieinfo.moredetails.model.MoreDetailsModel
import ayds.dodo.movieinfo.moredetails.view.MoreDetailsUiEvent
import ayds.dodo.movieinfo.moredetails.view.MoreDetailsView
import ayds.observer.Observer
import java.awt.Desktop
import java.io.IOException
import java.net.URI

interface MoreDetailsController {
    fun start(movie: OmdbMovie)
}

internal class MoreDetailsControllerImpl(
        private val moreDetailsView: MoreDetailsView,
        private val moreDetailsModel: MoreDetailsModel
) : MoreDetailsController {

    private val observer: Observer<MoreDetailsUiEvent> = object : Observer<MoreDetailsUiEvent> {
        override fun update(value: MoreDetailsUiEvent) {
            when (value) {
                MoreDetailsUiEvent.POSTER_ACTION -> showPoster()
            }
        }
    }

    init {
        moreDetailsView.onUiEvent().subscribe(observer)
    }

    override fun start(movie: OmdbMovie) {
        Thread {
            moreDetailsModel.getMoviePlot(movie)
        }.start()
    }

    private fun showPoster() {
        try {
            Desktop.getDesktop().browse(URI.create(moreDetailsModel.getPosterUrl()))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}