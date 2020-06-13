package ayds.dodo.movieinfo.moredetails.controller

import ayds.dodo.movieinfo.moredetails.model.MoreDetailsModelModule
import ayds.dodo.movieinfo.moredetails.view.moredetailsViewModule


object MoreDetailsControllerModule {
    fun init() {
         MoreDetailsControllerImpl(moredetailsViewModule.moredetailsView, MoreDetailsModelModule.moreDetailsModel)
    }
}