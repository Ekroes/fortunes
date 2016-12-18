package fortunes.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Helper routines for connecting to the database. The username/password are
 * here in plain text, just for development purposes. They could alternately be
 * stored in a db.properties file (not included in the git repository) and read
 * at runtime for improved security.
 * 
 * @author jenny
 *
 */
/**
 * @author jenny
 *
 */
public class DbUtil {

	private static String url = "jdbc:mysql://localhost:3306/";
	private static String schema = "fortunes";

	private static String username = "javadbapp";
	private static String password = "javacoderocks";

	public static Connection openConnection() throws SQLException {
		try {
			// loads the driver, first-time only.
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// A class not found occurs if mysql-connector-java-*.jar is
			// not in the project's Build Path. We don't want to have to
			// catch this checked exception everywhere, so wrap it
			// in an unchecked to make it silent.
			throw new RuntimeException(e);
		}

		// Create a new database connection and return it to the caller.
		return DriverManager.getConnection(url + schema, username, password);
	}

	/**
	 * Closes a database connection without whining, regardless of whether it
	 * was ever open or if it had problems along the way. This avoids obscuring
	 * any prior exceptions with other noise.
	 * 
	 * @param conn
	 */
	public static void silentCloseConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException se) {
				// and ignore it intentionally so we don't obscure
				// any previously thrown exceptions.
			}
		}
	}

	/**
	 * Closes a database statement without whining, regardless of whether it
	 * was ever open or if it had problems along the way. This avoids obscuring
	 * any prior exceptions with other noise.
	 * 
	 * @param pstmt
	 */
	public static void silentCloseStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException se) {
				// and ignore it intentionally so we don't obscure
				// any previously thrown exceptions.
			}
		}
	}

}
