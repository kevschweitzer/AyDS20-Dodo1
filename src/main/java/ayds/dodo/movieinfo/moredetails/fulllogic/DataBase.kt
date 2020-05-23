package ayds.dodo.movieinfo.moredetails.fulllogic

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DataBase {
    private const val url = "jdbc:sqlite:./extra_info.db"
    private const val table_create_query = "create table info (id INTEGER PRIMARY KEY AUTOINCREMENT, title string, plot string, image_url string, source integer)"

    @JvmStatic
    fun createNewDatabase() {
        try {
            DriverManager.getConnection(url).use { connection ->
                if (connection != null) {
                    val meta = connection.metaData
                    val statement = connection.createStatement()
                    statement.queryTimeout = 30
                    statement.executeUpdate(table_create_query)
                }
            }
        } catch (e: SQLException) {
            println("Fallo al crear DB: " + e.message)
        }
    }

    @JvmStatic
    fun saveMovieInfo(title: String, plot: String, imageUrl: String) {
        var connection: Connection? = null
        try {
            connection = DriverManager.getConnection(url)
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
        var connection: Connection? = null
        try {
            connection = DriverManager.getConnection(url)
            val statement = connection.createStatement()
            statement.queryTimeout = 30
            val rs = statement.executeQuery(getSelectQuery(title))
            rs.next()
            connection?.close()
            return rs.getString("plot")
        } catch (e: SQLException) {
            System.err.println("getOverview error " + e.message)
        }
        return null
    }

    @JvmStatic
    fun getImageUrl(title: String): String? {
        var connection: Connection? = null
        try {
            connection = DriverManager.getConnection(url)
            val statement = connection.createStatement()
            statement.queryTimeout = 30
            val rs = statement.executeQuery(getSelectQuery(title))
            rs.next()
            connection?.close()
            return rs.getString("image_url")
        } catch (e: SQLException) {
            System.err.println("getImageTitle error " + e.message)
        }
        return null
    }

    fun testDB() {
        var connection: Connection? = null
        try {
            connection = DriverManager.getConnection(url)
            val statement = connection.createStatement()
            statement.queryTimeout = 30
            val rs = statement.executeQuery(getSelectAllQuery())
            while (rs.next()) { // read the result set
                println("id = " + rs.getInt("id"))
                println("title = " + rs.getString("title"))
                println("source = " + rs.getString("source"))
            }
            connection?.close()
        } catch (e: SQLException) {
            System.err.println(e.message)
        }
    }

    private fun getSelectAllQuery() : String{
        return "select * from info"
    }

    private fun getSelectQuery(title:String) : String{
        return "select * from info WHERE title = '$title'"
    }

    private fun getInsertQuery(title: String, plot: String, imageUrl: String) : String{
        return "insert into info values(null, '$title', '$plot', '$imageUrl', 1)"
    }
}