package ayds.dodo.movieinfo.moredetails.model.repository.local

import ayds.dodo.movieinfo.moredetails.model.entities.NonExistentTmdbMovie
import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

object DataBase {
    private const val STATEMENT_TIMEOUT = 30
    private const val URL = "jdbc:sqlite:./extra_info.db"
    private const val TABLE_NAME = "info"
    private const val ID_COLUMN = "id"
    private const val TITLE_COLUMN = "title"
    private const val PLOT_COLUMN = "plot"
    private const val IMAGE_URL_COLUMN = "image_url"
    private const val POSTER_URL_COLUMN = "poster_url"
    private const val CREATE_TABLE_QUERY = "create table $TABLE_NAME ($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, $TITLE_COLUMN string, $PLOT_COLUMN string, $IMAGE_URL_COLUMN string, $POSTER_URL_COLUMN string)"

    private fun getSelectQuery(title: String) = "select * from info WHERE title = '$title'"

    private fun getInsertQuery(title: String, plot: String, imageUrl: String, posterUrl: String) = "insert into info values(null, '$title', '$plot', '$imageUrl', '$posterUrl')"

    @JvmStatic
    fun getTmdbMovie(title: String): TmdbMovie {
        var connection : Connection? = null
        var statement : Statement? = null
        try {
            connection =  DriverManager.getConnection(URL)
            statement =  connection.createStatement()
            statement.queryTimeout = 30
            val rs = statement.executeQuery(getSelectQuery(title))
            return if (!rs.isClosed) {
                rs.next()
                val movie = TmdbMovie()
                movie.title = title
                movie.plot = rs.getString(PLOT_COLUMN)
                movie.imageUrl = rs.getString(IMAGE_URL_COLUMN)
                movie.posterUrl = rs.getString(POSTER_URL_COLUMN)
                rs.close()
                movie
            } else NonExistentTmdbMovie
        } catch (e: SQLException) {
            System.err.println("Error getting movie: " + title + " " + e.message)
        } finally {
            statement?.close()
            connection?.close()
        }
        return NonExistentTmdbMovie
    }

    @JvmStatic
    fun createNewDatabase() {
        var connection : Connection? = null
        var statement : Statement? = null
        try {
            connection = DriverManager.getConnection(URL)
            statement = connection.createStatement()
            if (connection != null) {
                if (connection.metaData?.getTables(null, null, TABLE_NAME, null)?.next() == false) {
                    statement.queryTimeout = 30
                    statement.executeUpdate(CREATE_TABLE_QUERY)
                }
            }
        } catch (e: SQLException) {
            System.err.println("Error creating DB: " + e.message)
        } finally {
            statement?.close()
            connection?.close()
        }
    }

    @JvmStatic
    fun saveMovieInfo(movie: TmdbMovie) {
        if (movie !is NonExistentTmdbMovie) {
            var connection: Connection? = null
            var statement: Statement? = null
            try {
                connection = DriverManager.getConnection(URL)
                statement = connection.createStatement()
                statement.queryTimeout = STATEMENT_TIMEOUT
                statement.executeUpdate(getInsertQuery(movie.title, movie.plot, movie.imageUrl, movie.posterUrl))
                statement.close()
            } catch (e: SQLException) {
                System.err.println("saveMovieInfo error: " + e.message)
            } finally {
                statement?.close()
                connection?.close()
            }
        }
    }
}