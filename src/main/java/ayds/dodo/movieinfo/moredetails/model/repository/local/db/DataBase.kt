package ayds.dodo.movieinfo.moredetails.model.repository.local.db

import ayds.dodo.movieinfo.moredetails.model.entities.NonExistentTmdbMovie
import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie
import ayds.dodo.movieinfo.moredetails.model.repository.local.LocalStorage
import ayds.dodo.movieinfo.utils.sql.SqlDB
import java.sql.SQLException

internal class DataBase : LocalStorage, SqlDB() {
    override val dbUrl = SqlQueries.URL

    init{
        openConnection()
        createDBIfNeeded()
        closeConnection()
    }

    private fun createDBIfNeeded(){
        try {
            if (connection.metaData?.getTables(null, null, SqlQueries.TABLE_NAME, null)?.next() == false) {
                statement?.executeUpdate(SqlQueriesImp.getCreateTableQuery())
            }
        } catch (e: SQLException) {
            System.err.println("Error creating DB: " + e.message)
        }
    }

    override fun getTmdbMovie(title: String): TmdbMovie {
        openConnection()
        val toRet = getMovie(title)
        closeConnection()
        return toRet
    }

    private fun getMovie(title: String) : TmdbMovie{
        return try {
            val rs = statement?.executeQuery(SqlQueriesImp.getSelectQuery(title))
            if (rs != null)
                SqlQueriesImp.resultSetToMovieMapper(rs)
            else
                NonExistentTmdbMovie
        } catch (e: SQLException) {
            System.err.println("Error getting movie: " + title + " " + e.message)
            NonExistentTmdbMovie
        }
    }

    override fun saveMovieInfo(movie: TmdbMovie) {
        if (movie !is NonExistentTmdbMovie) {
            openConnection()
            saveMovie(movie)
            closeConnection()
        }
    }

    private fun saveMovie(movie: TmdbMovie) {
        try {
            statement?.executeUpdate(SqlQueriesImp.getInsertQuery(movie.title, movie.plot.replace("'", "`"), movie.imageUrl, movie.posterUrl))
            statement?.close()
        } catch (e: SQLException) {
            System.err.println("saveMovieInfo error: " + e.message)
        }
    }
}