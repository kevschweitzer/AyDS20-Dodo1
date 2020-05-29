package ayds.dodo.movieinfo.moredetails.fulllogic

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DataBase {
    private const val URL = "jdbc:sqlite:./extra_info.db"
    private const val TABLE_NAME = "info"
    private const val ID_COLUMN = "id"
    private const val TITLE_COLUMN = "title"
    private const val PLOT_COLUMN = "plot"
    private const val IMAGE_URL_COLUMN = "image_url"
    private const val SOURCE_COLUMN = "source"
    private const val CREATE_TABLE_QUERY = "create table $TABLE_NAME ($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, $TITLE_COLUMN string, $PLOT_COLUMN string, $IMAGE_URL_COLUMN string, $SOURCE_COLUMN integer)"
    private const val CHECK_QUERY = "SELECT name FROM sqlite_master WHERE type='table' AND name='$TABLE_NAME'";

    private fun getSelectAllQuery(): String {
        return "select * from info"
    }

    private fun getSelectQuery(title: String): String {
        return "select * from info WHERE title = '$title'"
    }

    private fun getInsertQuery(title: String, plot: String, imageUrl: String): String {
        return "insert into info values(null, '$title', '$plot', '$imageUrl', 1)"
    }

    @JvmStatic
    fun createNewDatabase() {
        try {
            DriverManager.getConnection(URL).use { connection ->
                if (connection != null) {
                    val checkStatement = connection.createStatement()
                    val rs = checkStatement.executeQuery(CHECK_QUERY)
                    rs.next()
                    if (rs.getString("name") == null) {
                        val statement = connection.createStatement()
                        statement.queryTimeout = 30
                        statement.executeUpdate(CREATE_TABLE_QUERY)
                    }
                }
            }
        } catch (e: SQLException) {
            System.err.println("Fallo al crear DB: " + e.message)
        }
    }

    @JvmStatic
    fun saveMovieInfo(title: String, plot: String, imageUrl: String) {
        var connection: Connection?
        try {
            connection = DriverManager.getConnection(URL)
            val statement = connection.createStatement()
            statement.queryTimeout = 30
            statement.executeUpdate(getInsertQuery(title, plot, imageUrl))
            connection?.close()
        } catch (e: SQLException) {
            System.err.println("Error on saving movie info " + e.message)
        }
    }

    @JvmStatic
    fun getOverview(title: String): String? {
        var connection: Connection?
        try {
            connection = DriverManager.getConnection(URL)
            val statement = connection.createStatement()

            statement.queryTimeout = 30
            val rs = statement.executeQuery(getSelectQuery(title))
            rs.next()
            connection?.close()
            return rs.getString(PLOT_COLUMN)
        } catch (e: SQLException) {
            System.err.println("getOverview error " + e.message)
        }
        return null
    }

    @JvmStatic
    fun getImageUrl(title: String): String? {
        var connection: Connection?
        try {
            connection = DriverManager.getConnection(URL)
            val statement = connection.createStatement()
            statement.queryTimeout = 30
            val rs = statement.executeQuery(getSelectQuery(title))
            rs.next()
            connection?.close()
            return rs.getString(IMAGE_URL_COLUMN)
        } catch (e: SQLException) {
            System.err.println("getImageTitle error " + e.message)
        }
        return null
    }

    @JvmStatic
    fun testDB() {
        var connection: Connection?
        try {
            connection = DriverManager.getConnection(URL)
            val statement = connection.createStatement()
            statement.queryTimeout = 30
            val rs = statement.executeQuery(getSelectAllQuery())
            while (rs.next()) {
                println("$ID_COLUMN = ${rs.getInt(ID_COLUMN)}")
                println("$TITLE_COLUMN = ${rs.getInt(TITLE_COLUMN)}")
                println("$SOURCE_COLUMN = ${rs.getInt(SOURCE_COLUMN)}")
            }
            connection?.close()
        } catch (e: SQLException) {
            System.err.println(e.message)
        }
    }
}