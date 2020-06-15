package ayds.dodo.movieinfo.moredetails.model.repository.local.db

import ayds.dodo.movieinfo.moredetails.model.entities.NonExistentTmdbMovie
import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie
import ayds.dodo.movieinfo.moredetails.model.repository.local.LocalStorage
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

internal class DataBase : LocalStorage{
    private val STATEMENT_TIMEOUT = 30
    private lateinit var connection : Connection

    init{
        openConnection()
        createDBIfNeeded()
        closeConnection()
    }

    private fun createDBIfNeeded(){
        var statement : Statement? = null
        try {
            statement = connection.createStatement()
            if (connection.metaData?.getTables(null, null, SqlQueries.TABLE_NAME, null)?.next() == false) {
                statement.queryTimeout = STATEMENT_TIMEOUT
                statement.executeUpdate(SqlQueriesImp.getCreateTableQuery())
            }
        } catch (e: SQLException) {
            System.err.println("Error creating DB: " + e.message)
        } finally {
            statement?.close()
        }
    }

    private fun openConnection() {
        try {
            connection = DriverManager.getConnection(SqlQueries.URL)
        } catch (e: SQLException) {
            println("Could not create connection $SqlQueries.URL $e")
        }
    }

    private fun closeConnection() {
        try {
            connection.close()
        } catch (e: SQLException) {
            System.err.println(e)
        }
    }

    override fun getTmdbMovie(title: String): TmdbMovie {
        openConnection()
        val toRet = getMovie(title)
        closeConnection()
        return toRet
    }

    private fun getMovie(title: String) : TmdbMovie{
        var statement : Statement? = null
        return try {
            statement =  connection.createStatement()
            statement.queryTimeout = 30
            val rs = statement.executeQuery(SqlQueriesImp.getSelectQuery(title))
            SqlQueriesImp.resultSetToMovieMapper(rs)
        } catch (e: SQLException) {
            System.err.println("Error getting movie: " + title + " " + e.message)
            NonExistentTmdbMovie
        } finally {
            statement?.close()
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
        var statement: Statement? = null
        try {
            statement = connection.createStatement()
            statement.queryTimeout = STATEMENT_TIMEOUT
            statement.executeUpdate(SqlQueriesImp.getInsertQuery(movie.title, movie.plot.replace("'", "`"), movie.imageUrl, movie.posterUrl))
            statement.close()
        } catch (e: SQLException) {
            System.err.println("saveMovieInfo error: " + e.message)
        } finally {
            statement?.close()
        }
    }
}