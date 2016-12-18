package fortunes.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import fortunes.database.FortuneDAO;
import fortunes.model.Fortune;

/** 
 * Loads quotes from a quotes.txt file into the database, to initialize the
 * empty database with some real content.  This should only be run once with
 * a brand new database; otherwise you'll have duplicate content.
 * @author jenny
 *
 */
public class QuotesLoader {
	
	public void load(String filename)
	throws IOException, SQLException
	{
		System.out.println("Loading...");
		int count = 0;
		FortuneDAO dao = new FortuneDAO();
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));

		String line = null;
		StringBuffer quote = new StringBuffer();
		while((line = br.readLine()) != null) {
			if (line.equals("*")) {
				dao.save(new Fortune(quote.toString()));
				count++;
				quote = new StringBuffer();
			} else {
				quote.append(line.trim() + " ");
			}
		}
		// last one exists without a trailing asterisk; save it too.
		dao.save(new Fortune(quote.toString()));
		count++;
		br.close();

		// On the first run, this lets us see if the counts match.
		// They might not match forever since we might change data later ourselves.
		System.out.println("Loaded " + count + " quotes into the database.");
		System.out.println("Database now contains " + dao.countAll() + " quotes.");
	}

	public static void main(String[] args) 
	throws Exception
	{
		QuotesLoader loader = new QuotesLoader();
		loader.load("sql/quotes.txt");
	}

}
