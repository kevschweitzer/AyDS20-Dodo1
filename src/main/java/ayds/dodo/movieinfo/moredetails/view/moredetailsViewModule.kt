package ayds.dodo.movieinfo.moredetails.view

import ayds.dodo.movieinfo.moredetails.model.MoreDetailsModelModule

object moredetailsViewModule {
    val moredetailsView = moredetailsViewImp(MoreDetailsModelModule.moreDetailsModel )
}