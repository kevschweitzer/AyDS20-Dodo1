package ayds.dodo.movieinfo.moredetails.fulllogic

import java.sql.DriverManager
import java.sql.SQLException

object DataBase {
    private const val URL = "jdbc:sqlite:./extra_info.db"
    private const val TABLE_NAME = "info"
    private const val ID_COLUMN = "id"
    private const val TITLE_COLUMN = "title"
    private const val PLOT_COLUMN = "plot"
    private const val IMAGE_URL_COLUMN = "image_url"
    private const val CREATE_TABLE_QUERY = "create table $TABLE_NAME ($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, $TITLE_COLUMN string, $PLOT_COLUMN string, $IMAGE_URL_COLUMN string)"

    private fun getSelectQuery(title: String) = "select * from info WHERE title = '$title'"

    private fun getInsertQuery(title: String, plot: String, imageUrl: String) = "insert into info values(null, '$title', '$plot', '$imageUrl')"

    private fun getColumnByTitle(title: String, column: String): String? {
        var toRet : String? = null
        DriverManager.getConnection(URL).use { connection ->
            try {
                val statement = connection.createStatement()
                statement.queryTimeout = 30
                val rs = statement.executeQuery(getSelectQuery(title))
                toRet = if (!rs.isClosed) {
                    rs.next()
                    rs.getString(column)
                } else {
                    null
                }
            } catch (e: SQLException) {
                System.err.println("getImageUrl error " + e.message)
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
                        statement.queryTimeout = 30
                        statement.executeUpdate(CREATE_TABLE_QUERY)
                    }
                }
            } catch (e: SQLException) {
                System.err.println("Fallo al crear DB: " + e.message)
            }
        }
    }

    @JvmStatic
    fun saveMovieInfo(title: String, plot: String, imageUrl: String) {
        DriverManager.getConnection(URL).use { connection ->
            try {
                val statement = connection.createStatement()
                statement.queryTimeout = 30
                statement.executeUpdate(getInsertQuery(title, plot, imageUrl))
            } catch (e: SQLException) {
                System.err.println("Error on saving movie info " + e.message)
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
}