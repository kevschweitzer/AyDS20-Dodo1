package ayds.dodo.movieinfo.moredetails.model.repository.local.db

import ayds.dodo.movieinfo.moredetails.model.entities.NonExistentTmdbMovie
import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie
import ayds.dodo.movieinfo.moredetails.model.repository.local.LocalStorage
import ayds.dodo.movieinfo.utils.sql.SqlDB
import java.sql.SQLException

internal class DataBase(private val sqlQueries: SqlQueries) : LocalStorage, SqlDB() {
    override val dbUrl = SqlQueries.URL

    init{
        openConnection()
        createDBIfNeeded()
        closeConnection()
    }

    private fun createDBIfNeeded(){
        try {
            if (connection.metaData?.getTables(null, null, SqlQueries.TABLE_NAME, null)?.next() == false) {
                statement?.executeUpdate(sqlQueries.getCreateTableQuery())
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
            val rs = statement?.executeQuery(sqlQueries.getSelectQuery(title))
            if (rs != null)
                sqlQueries.resultSetToMovieMapper(rs)
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
            statement?.executeUpdate(sqlQueries.getInsertQuery(movie.title, movie.plot.replace("'", "`"), movie.imageUrl, movie.posterUrl))
            statement?.close()
        } catch (e: SQLException) {
            System.err.println("saveMovieInfo error: " + e.message)
        }
    }
}