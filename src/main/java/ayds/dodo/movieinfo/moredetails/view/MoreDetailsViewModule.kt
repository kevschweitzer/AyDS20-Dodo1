package ayds.dodo.movieinfo.moredetails.view

import ayds.dodo.movieinfo.moredetails.model.MoreDetailsModelModule

object MoreDetailsViewModule {
    val  moreDetailsView: MoreDetailsView = MoreDetailsViewImp(MoreDetailsModelModule.moreDetailsModel)
}