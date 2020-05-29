package ayds.dodo.movieinfo.moredetails.fulllogic

import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import javax.print.attribute.standard.MediaSize

object DataBase {
    private const val URL = "jdbc:sqlite:./extra_info.db"
    private const val TABLE_NAME = "info"
    private const val ID_COLUMN = "id"
    private const val TITLE_COLUMN = "title"
    private const val PLOT_COLUMN = "plot"
    private const val IMAGE_URL_COLUMN = "image_url"
    private const val CREATE_TABLE_QUERY = "create table $TABLE_NAME ($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, $TITLE_COLUMN string, $PLOT_COLUMN string, $IMAGE_URL_COLUMN string)"

    private fun getSelectQuery(title: String): String {
        return "select * from info WHERE title = '$title'"
    }

    private fun getInsertQuery(title: String, plot: String, imageUrl: String): String {
        return "insert into info values(null, '$title', '$plot', '$imageUrl')"
    }

    private fun getResultSetByTitle(title : String) : ResultSet? {
        val connection = DriverManager.getConnection(URL)
        val statement = connection.createStatement()
        try {
            statement.queryTimeout = 30
            return statement.executeQuery(getSelectQuery(title))
        } catch (e : SQLException){
            System.err.println("getOverview error " + e.message)
        } finally {
            statement.close()
            connection.close()
        }
        return null
    }

    @JvmStatic
    fun createNewDatabase() {
        val connection = DriverManager.getConnection(URL)
        val statement = connection.createStatement()
        try {
            if (connection != null) {
                if (connection.metaData?.getTables(null, null, TABLE_NAME, null)?.next() == false) {
                    statement.queryTimeout = 30
                    statement.executeUpdate(CREATE_TABLE_QUERY)
                }
            }
        } catch (e: SQLException) {
            System.err.println("Fallo al crear DB: " + e.message)
        } finally {
            statement.close()
            connection.close()
        }
    }

    @JvmStatic
    fun saveMovieInfo(title: String, plot: String, imageUrl: String) {
        val connection = DriverManager.getConnection(URL)
        val statement = connection.createStatement()
        try {
            statement.queryTimeout = 30
            statement.executeUpdate(getInsertQuery(title, plot, imageUrl))
        } catch (e: SQLException) {
            System.err.println("Error on saving movie info " + e.message)
        } finally {
            statement.close()
            connection.close()
        }
    }

    @JvmStatic
    fun getOverview(title: String): String? {
        try{
            val rs = getResultSetByTitle(title)
            rs?.next()
            return rs?.getString(PLOT_COLUMN)
        } catch (e: SQLException) {
            System.err.println("getOverview error " + e.message)
        }
        return null
    }

    @JvmStatic
    fun getImageUrl(title: String): String? {
        try {
            val rs = getResultSetByTitle(title)
            rs?.next()
            return rs?.getString(IMAGE_URL_COLUMN)
        } catch (e: SQLException) {
            System.err.println("getImageUrl error " + e.message)
        }
        return null
    }
}