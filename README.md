# About Fortunes

This is a project to demonstrate a console app using JDBC for data load/save.  Tracks 
quotes / fortune cookie sayings, with the inital set pre-loaded from a file.

## Getting Start

Fork this project on github, so you have your own copy.  Then find the green "Clone or download" button,
to get the url to clone it.  Then from git bash, do:

    git clone <yoururl>

After you have a copy of the project locally, go into Eclipse.  File, Import, From Existing 
Workspace, (browse to the folder), checkbox the project name fortunes.  Then tell it Finish.

Wait a moment for the compile to finish, and then move on to Setup.

## Setup

1. Verify that 'javadbapp' user exists in your database, or see below for how to create it.
2. Run the create.sql by hand using your root database user.  This creates tables and grants permissions.
Only do this once.
3. Open the QuotesLoader code in Eclipse, right click, run as java application.  This inserts rows 
into the database.  Only do this once.
4. Open the FortuneApp code in Eclipse, right clck, run as java application.  This is the real program
that produces a useful user interface.  You can quit and restart this as many times as you want.

### Database User Information

This app uses a database username and password that are expected to already exist in your database.
If they do not, you can create them through MySQL Workbench, "Users and Privileges", to match
the user/pass in DbUtil.java.  Once the user exists, then run the create.sql to make the schema and
tables exist.

## Packages/Folders

fortunes.database - Contains the database connection utilities.  Contains the data access
object (DAO) for the fortune table.

fortunes.model - Contains the entity model; the fortune class that represents one saying.

fortunes.ui - Contains the app entry point FortuneApp for the console UI.  Contains a separate 
standalone application QuotesLoader that can read the quotes.txt file from disk and copy those quotes
into the database so it's not an empty database.

sql - Contains the create.sql to initialize the database.  Contains the quotes.txt original data
file before it gets loaded into the database.  You will run the create.sql by hand in MySQL Workbench
using your root account so it has permissions to set up the new schema and grants.

## Database Driver

The project comes with the MySQL driver (mysql-connector-java-5.1.35.jar) for talking with a
mysql database.  This is already in the project's build path if you open the project using Eclipse.
It will be picked up automatically during the Class.forName() step in DbUtil.

## Deleting All Quotes

If you want to delete all the quotes that were loaded into the database, and start with a 
fresh empty table, run this SQL statement from a MySQL Workbench query window:

    truncate fortune;


