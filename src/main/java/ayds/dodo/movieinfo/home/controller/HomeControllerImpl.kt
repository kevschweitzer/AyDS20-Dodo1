package ayds.dodo.movieinfo.home.controller

import ayds.dodo.movieinfo.home.model.HomeModel
import ayds.dodo.movieinfo.home.view.HomeView
import ayds.dodo.movieinfo.home.view.UiEvent
import ayds.dodo.movieinfo.moredetails.controller.MoreDetailsControllerModule
import ayds.dodo.movieinfo.moredetails.view.MoreDetailsViewModule
import ayds.observer.Observer

interface HomeController

internal class HomeControllerImpl(
        private val homeView: HomeView, private val homeModel: HomeModel
) : HomeController {

    private val observer: Observer<UiEvent> = object : Observer<UiEvent> {
        override fun update(value: UiEvent) {
            when (value) {
                UiEvent.SEARCH_ACTION -> onSearchMovieAction()
                UiEvent.MORE_DETAILS_ACTION -> onMoreDetailsAction()
            }
        }
    }

    init {
        homeView.onUiEvent().subscribe(observer)
    }

    private fun onSearchMovieAction() {
        Thread {
            homeModel.searchMovie(homeView.movieTitle)
        }.start()
    }

    private fun onMoreDetailsAction() {
        MoreDetailsControllerModule.init()
        homeModel.getLastMovie()?.let { MoreDetailsViewModule.moredetailsView.openView(it) }
    }
}