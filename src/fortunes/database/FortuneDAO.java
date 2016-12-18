package fortunes.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fortunes.model.Fortune;

/**
 * Loads/saves fortunes from the database.
 * 
 * @author jenny
 *
 */
public class FortuneDAO {

	Random rand = null;

	public FortuneDAO() {
		rand = new Random();
	}

	/**
	 * Returns a random fortune from the database.  Random seed is maintained
	 * as long as the app is running, so you should get a new random number
	 * each time without pattern repeats.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Fortune random() throws SQLException {
		int count = countAll();
		int target = rand.nextInt(count);
		return getNth(target);
	}

	/**
	 * Returns how many fortunes are in the db.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int countAll() throws SQLException {
		String sql = "select count(*) as c from fortune";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			conn = DbUtil.openConnection();
			pstmt = conn.prepareStatement(sql);

			res = pstmt.executeQuery();
			if (res.next()) {
				return res.getInt("c");
			} else {
				return 0;
			}
		} finally {
			DbUtil.silentCloseConnection(conn);
			DbUtil.silentCloseStatement(pstmt);
		}

	}

	/**
	 * Gets the Nth fortune in the table regardless of its id; for example 3
	 * would get the third fortune. This differs from get() because some
	 * fortunes may have been deleted, so ids don't match order.
	 * 
	 * @param n
	 * @return
	 * @throws SQLException
	 */
	public Fortune getNth(int n) throws SQLException {
		String sql = "select * from fortune order by id limit ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			conn = DbUtil.openConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (n + 1));

			res = pstmt.executeQuery();

			// Skip ahead to the nth item.
			for (int i = 0; i <= n; i++) {
				res.next();
			}
			return new Fortune(res.getInt("id"), res.getString("saying"));
		} finally {
			DbUtil.silentCloseConnection(conn);
			DbUtil.silentCloseStatement(pstmt);
		}
	}

	/**
	 * Gets a fortune with a specific primary key.
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public Fortune get(Integer id) throws SQLException {
		String sql = "select * from fortune where id=?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			conn = DbUtil.openConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);

			res = pstmt.executeQuery();
			if (res.next()) {
				return new Fortune(res.getInt("id"), res.getString("saying"));
			} else {
				return null;
			}
		} finally {
			DbUtil.silentCloseConnection(conn);
			DbUtil.silentCloseStatement(pstmt);
		}
	}

	/**
	 * Retrieves a list of all fortunes.
	 * @return
	 * @throws SQLException
	 */
	public List<Fortune> list() throws SQLException {
		List<Fortune> fortunes = new ArrayList<Fortune>();

		String sql = "select * from fortune order by id";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			conn = DbUtil.openConnection();
			pstmt = conn.prepareStatement(sql);
			res = pstmt.executeQuery();
			while (res.next()) {
				fortunes.add(new Fortune(res.getInt("id"), res.getString("saying")));
			}
			return fortunes;
		} finally {
			DbUtil.silentCloseConnection(conn);
			DbUtil.silentCloseStatement(pstmt);
		}
	}
	
	/**
	 * Searches for a word or phrase anywhere within the text.  Used to
	 * retrieve, for example, all mentions of "holiday" or whatever you want.
	 * @param text
	 * @return
	 * @throws SQLException
	 */
	public List<Fortune> search(String text) throws SQLException 
	{
		List<Fortune> fortunes = new ArrayList<Fortune>();
		String sql = "select * from fortune where saying like ? order by id";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			conn = DbUtil.openConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + text + "%");
			res = pstmt.executeQuery();
			while (res.next()) {
				fortunes.add(new Fortune(res.getInt("id"), res.getString("saying")));
			}
			return fortunes;
		} finally {
			DbUtil.silentCloseConnection(conn);
			DbUtil.silentCloseStatement(pstmt);
		}
	}
	
	/** 
	 * Saves a fortune to the database (inserts or updates as appropriate).
	 * @param f
	 * @throws SQLException
	 */
	public void save(Fortune f) throws SQLException {
		if (f.getId() == null) {
			insert(f);
		} else {
			update(f);
		}
	}

	private void update(Fortune f) throws SQLException {
		String sql = "update fortune set saying=? where id=?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			conn = DbUtil.openConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, f.getSaying());
			pstmt.setInt(2, f.getId());
			pstmt.executeUpdate();
		} finally {
			DbUtil.silentCloseConnection(conn);
			DbUtil.silentCloseStatement(pstmt);
		}
	}

	private void insert(Fortune f) throws SQLException {
		String sql = "insert into fortune (saying) values (?)";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			conn = DbUtil.openConnection();
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, f.getSaying());
			pstmt.executeUpdate();
			try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					f.setId(generatedKeys.getInt(1));
				} else {
					throw new SQLException("Inserting failed, no id created.");
				}
			}
		} finally {
			DbUtil.silentCloseConnection(conn);
			DbUtil.silentCloseStatement(pstmt);
		}
	}
	
	public void delete(Integer id)
	throws SQLException {
		String sql = "delete from fortune where id=?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DbUtil.openConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} finally {
			DbUtil.silentCloseConnection(conn);
			DbUtil.silentCloseStatement(pstmt);
		}
		
	}

}
