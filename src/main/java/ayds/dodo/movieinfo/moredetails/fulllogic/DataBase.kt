package ayds.dodo.movieinfo.moredetails.fulllogic

import java.sql.DriverManager
import java.sql.SQLException

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

    private fun getInsertQuery(title: String, plot: String, imageUrl: String, posterUrl : String) = "insert into info values(null, '$title', '$plot', '$imageUrl', '$posterUrl')"

    private fun getColumnByTitle(title: String, column: String): String? {
        var toRet : String? = null
        DriverManager.getConnection(URL).use { connection ->
            try {
                val statement = connection.createStatement()
                statement.queryTimeout = STATEMENT_TIMEOUT
                val rs = statement.executeQuery(getSelectQuery(title))
                toRet = if (!rs.isClosed) {
                    rs.next()
                    rs.getString(column)
                } else {
                    null
                }
            } catch (e: SQLException) {
                System.err.println("getImageUrl error: " + e.message)
            }
        }
        return toRet
    }

    @JvmStatic
    fun createNewDatabase() {
        DriverManager.getConnection(URL).use { connection ->
            try {
                val statement = connection.createStatement()
                if (connection != null) {
                    if (connection.metaData?.getTables(null, null, TABLE_NAME, null)?.next() == false) {
                        statement.queryTimeout = STATEMENT_TIMEOUT
                        statement.executeUpdate(CREATE_TABLE_QUERY)
                    }
                }
            } catch (e: SQLException) {
                System.err.println("createNewDatabase error: " + e.message)
            }
        }
    }

    @JvmStatic
    fun saveMovieInfo(title: String, plot: String, imageUrl: String, posterUrl: String) {
        DriverManager.getConnection(URL).use { connection ->
            try {
                val statement = connection.createStatement()
                statement.queryTimeout = STATEMENT_TIMEOUT
                statement.executeUpdate(getInsertQuery(title, plot, imageUrl, posterUrl))
            } catch (e: SQLException) {
                System.err.println("saveMovieInfo error: " + e.message)
            }
        }
    }

    @JvmStatic
    fun getOverview(title: String): String? {
        return getColumnByTitle(title, PLOT_COLUMN)
    }

    @JvmStatic
    fun getImageUrl(title: String): String? {
        return getColumnByTitle(title, IMAGE_URL_COLUMN)
    }

    @JvmStatic
    fun getPosterUrl(title: String) : String? {
        return getColumnByTitle(title, POSTER_URL_COLUMN)
    }
}