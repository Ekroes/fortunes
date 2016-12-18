package fortunes.ui;

/**
 * Provides a menu and user interface for managing fortune cookies. Demonstrates
 * proper separation of responsibilities and use of a database for storing data,
 * while keeping the app very simple for learners.
 * 
 * @author jenny
 *
 */
public class FortuneApp {

	public static void main(String[] args) throws Exception {
		FortuneUI ui = new FortuneUI();
		ui.mainMenu();
	}

}
