package ayds.dodo.movieinfo.moredetails.controller

import ayds.observer.Observer

interface MoreDetailsController

internal class MoreDetailsControllerImpl(
        // aca va el model y la view como private val
) : MoreDetailsController{
    /*
    // Crear UIEvente de la more details view 
    private val observer: Observer<UiEvent> = object : Observer<UiEvent> {
        override fun update(value: UiEvent) {
            when (value) {

            }
        }
    }*/

    init {
        //moreDetailesView.onUiEvent().subscribe(observer)
    }

}