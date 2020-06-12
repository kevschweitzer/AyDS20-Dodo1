package ayds.dodo.movieinfo.moredetails.model.repository.local.db

interface SqlQueries {
    fun getSelectQuery(title: String) : String

    fun getInsertQuery(title: String, plot: String, imageUrl: String, posterUrl: String) : String

    fun getCreateTableQuery() : String

    companion object{
        const val URL = "jdbc:sqlite:./extra_info.db"
        const val TABLE_NAME = "info"
        const val ID_COLUMN = "id"
        const val TITLE_COLUMN = "title"
        const val PLOT_COLUMN = "plot"
        const val IMAGE_URL_COLUMN = "image_url"
        const val POSTER_URL_COLUMN = "poster_url"
    }
}