package ayds.dodo.movieinfo.moredetails.model.repository.local.db

import ayds.dodo.movieinfo.moredetails.model.entities.NonExistentTmdbMovie
import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie
import java.sql.ResultSet

object SqlQueriesImp : SqlQueries{
    override fun getSelectQuery(title: String) = "select * from info WHERE title = '$title'"

    override fun getInsertQuery(title: String, plot: String, imageUrl: String, posterUrl: String) = "insert into info values(null, '$title', '$plot', '$imageUrl', '$posterUrl')"

    override fun getCreateTableQuery() = "create table ${SqlQueries.TABLE_NAME} (${SqlQueries.ID_COLUMN} INTEGER PRIMARY KEY AUTOINCREMENT, ${SqlQueries.TITLE_COLUMN} string, ${SqlQueries.PLOT_COLUMN} string, ${SqlQueries.IMAGE_URL_COLUMN} string, ${SqlQueries.POSTER_URL_COLUMN} string)"

    @JvmStatic
    fun resultSetToMovieMapper(rs : ResultSet) : TmdbMovie{
        return if (!rs.isClosed) {
            rs.next()
            val movie = TmdbMovie()
            movie.title = SqlQueries.TITLE_COLUMN
            movie.plot = rs.getString(SqlQueries.PLOT_COLUMN)
            movie.imageUrl = rs.getString(SqlQueries.IMAGE_URL_COLUMN)
            movie.posterUrl = rs.getString(SqlQueries.POSTER_URL_COLUMN)
            rs.close()
            movie
        } else NonExistentTmdbMovie
    }
}