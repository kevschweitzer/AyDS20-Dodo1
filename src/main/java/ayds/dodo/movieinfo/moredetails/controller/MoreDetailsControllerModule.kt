package ayds.dodo.movieinfo.moredetails.controller

import ayds.dodo.movieinfo.moredetails.model.MoreDetailsModelModule
import ayds.dodo.movieinfo.moredetails.view.MoreDetailsViewModule


object MoreDetailsControllerModule {
    private val moreDetailsController: MoreDetailsController =
            MoreDetailsControllerImpl(MoreDetailsViewModule.moredetailsView, MoreDetailsModelModule.moreDetailsModel)

    fun init() {}
}