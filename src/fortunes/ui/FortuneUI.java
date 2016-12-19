package fortunes.ui;

import static java.lang.System.out;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import fortunes.database.FortuneDAO;
import fortunes.model.Fortune;

public class FortuneUI {

	Scanner keyboard = new Scanner(System.in);
	FortuneDAO dao = new FortuneDAO();

	public void mainMenu() throws SQLException {
		out.println("Welcome to Fortunes.");
		boolean keepRunning = true;
		while(keepRunning)
		{
			printMainMenu();
			int choice = readUserChoice();
			keepRunning = callMenuItem(choice);
		}
		out.println("Goodbye.");
	}

	public int readUserChoice() {
		String input = keyboard.nextLine().trim();
		Integer id = Integer.parseInt(input);
		return id;
	}

	public void printMainMenu() {
		out.println("1) Show a random fortune");
		out.println("2) Search fortunes");
		out.println("3) List all fortunes");
		out.println("4) Add a fortune");
		out.println("5) Modify a fortune");
		out.println("6) Delete a fortune");
		out.println("7) Show fortune with id=n");
		out.println("0) Quit\n");
		out.print("? ");
	}

	public boolean callMenuItem(int choice) throws SQLException {
		switch (choice) {
		case 0: // quit
			return false;
		case 1: // random
			showRandomFortune();
			break;
		case 2: // search
			searchFortunes();
			break;
		case 3: // list all
			listAllFortunes();
			break;
		case 4: // add
			addFortune();
			break;
		case 5: // modify
			updateFortune();
			break;
		case 6: // delete
			deleteFortune();
			break;
		case 7: // get by id
			showFortuneById();
			break;
		}
		return true;
	}

	public void addFortune() throws SQLException {
		out.println("Please type the fortune you wish to add and press enter when done.\n");
		out.println("> ");
		String text = keyboard.nextLine();
		Fortune f = new Fortune(text);
		dao.save(f);
		out.println("Fortune saved as #" + f.getId() + ".\n");
	}

	public void showFortuneById() throws SQLException {
		out.println("Please type the database id for the fortune you wish to see.");
		out.println("id? ");
		Integer id = readUserChoice();
		Fortune f = dao.get(id);
		if (f == null) {
			out.println("There was no fortune with id = " + id + ". Returning to main menu.");
			return;
		}
		out.println(f);
	}

	public void updateFortune() throws SQLException {
		out.println("Please type the database id for the fortune you wish to edit.");
		out.println("You can discover this from the list or search screens.");
		out.println("id? ");
		Integer id = readUserChoice();

		Fortune f = dao.get(id);
		if (f == null) {
			out.println("There was no fortune with id = " + id + ". Returning to main menu.");
			return;
		}

		out.println("Here is the existing text of the fortune.");
		out.println(f);

		out.println("What should it be instead?");
		out.print("> ");

		String text = keyboard.nextLine();
		f.setSaying(text);
		dao.save(f);
	}

	public void deleteFortune() throws SQLException {
		out.println("Please type the database id for the fortune you wish to delete.");
		out.println("You can discover this from the list or search screens.");
		out.println("id? ");
		Integer id = readUserChoice();

		Fortune f = dao.get(id);
		if (f == null) {
			out.println("There was no fortune with id = " + id + ". Returning to main menu.");
			return;
		}

		out.println("Here is the existing text of the fortune.");
		out.println(f);
		out.println("Do you want to archive (soft delete) or delete (hard delete) this fortune? ");
		out.println("(a for archive, d for delete, c for cancel)");
		out.print("? ");
		String text = keyboard.nextLine();

		if (text.equals("d")) {
			dao.delete(id);
			out.println("Deleted.");
		} else if (text.equals("a")) {
			f.setArchived(true);
			dao.save(f);
			out.println("Archived.");
		} else {
			out.println("You didn't type a or d so canceling.");
		}
	}

	public void listAllFortunes() throws SQLException {
		List<Fortune> results = dao.list();
		displayFortunesPaged(results);
	}

	private void displayFortunesPaged(List<Fortune> results) {
		int count = 0;
		for (Fortune f : results) {
			out.println(f);
			count++;
			// pause with page break every 20 fortunes.
			if (count % 20 == 0) {
				out.println("\n... (" + count + " / " + results.size() + ") press enter for more or q to quit ...");
				String key = keyboard.nextLine();
				if (key.equals("q")) {
					break;
				}
			}
		}
	}

	public void searchFortunes() throws SQLException {
		out.println("Displays fortunes containing a particular word or phrase.");
		out.println("Search text: ");
		out.print("> ");
		String query = keyboard.nextLine();
		List<Fortune> results = dao.search(query);
		if (results.isEmpty()) {
			out.println("No matches.\n");
		} else {
			displayFortunesPaged(results);
		}
	}

	public void showRandomFortune() throws SQLException {
		Fortune f = dao.random();
		out.println(f);
	}

}
