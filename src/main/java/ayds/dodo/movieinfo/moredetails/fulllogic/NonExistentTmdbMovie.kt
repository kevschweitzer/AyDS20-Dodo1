package ayds.dodo.movieinfo.moredetails.fulllogic

object NonExistentTmdbMovie : TmdbMovie() {
    const val NO_RESULTS_MESSAGES = "No Results"
    const val IMAGE_NOT_FOUND = "https://farm5.staticflickr.com/4363/36346283311_1dec5bb2c2.jpg"
    override var plot = NO_RESULTS_MESSAGES
    override var imageUrl = IMAGE_NOT_FOUND
}