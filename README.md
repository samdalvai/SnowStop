## SnowstopDB
The aim of this project is to develop a database that could enable the automatization of the companies processes involved in the production of supply offers and the calculation of snow retaining systems. The "so called" Snowstop database will enable employees of the company to easily perform calculations on the snow load of a particular building site and propose to customers a supply offer that will provide a tailored snow retaining system based on such calculations. The altitude and the dimensions of the roof for which a snow load calculation is perfomed will affect the actual amount of snow that will deposit on it and the type and resistance of the retaining system needed.

<br>

## Folder structure of the project
* Folder `Diagrams/`: contains the first conceptual schema draft, the definitive conceptual schema and the restructured conceptual schema.
* Folder `Documentation/`: contains the documentation with the description of the 4 conceptual design phases.
* Folder `SQL/`: contains all the `.sql` files that define the database based on the conceptual design. The folder `tables/` contains the definitions of tables and views, the folder `triggers/` contains the definitions for the triggers, the folder `data/` contains the default data to be loaded into the database for each relation. Most of the values, such as for the Italian cities and the products are based on real data that has been collected through scraping mechanisms. A small part of the data is fictional and has been inserted only for demonstration purposes.
* Folder `Snowstop_Application/`: contains the application that interacts with the database.

<br>

## Prerequisites

* In order to load the database schema and the starting data you need to install a version of PostgreSQL 9.6.x
* In order to make the application work you need at least Java 1.8.

<br>

## Getting Started

### Instructions for the creation of the database
* Open a terminal window in the folder `SQL/`
* In the terminal connect to PostgreSQL with the command `psql -W -U <YOUR-USERNAME>`
* Create a database named as you wish with the command `CREATE DATABASE <YOUR-DATABASE-NAME>;`
* Connect to the database with the command `\c <YOUR-DATABASE-NAME>`
* From the same command line run the command `\i load.sql`. This will create all the tables, the triggers and the views of the database and load all the default data for each relation. If you prefer to only load the definitions without the default data you can run the command `\i load-no-data.sql` instead

### Instructions for the running the application
* Before the compilation of the .java files you need to set your database name (that you have chosen in the previous steps), your username and your password in the main method of the class `App`, located in the `Snowstop_Application/src/` folder, otherwise the application **WILL NOT** be able to connect to the database:
```
public static void main(String[] args) throws Exception {
        String db = "<YOUR-DATABASE-NAME>"; 
        String username = "<YOUR-USERNAME>";
        String password = "<YOUR-PASSWORD>";
        ...
```

(Alternatively, you can pass the values for the three variables as parameters to the application when you launch it.)

<br>

* Open a terminal window in the folder `Snowstop_Application/`
* Run the command `javac -classpath src/ src/*.java` to compile the .java files
* Run the command `java -classpath "lib/postgresql-42.2.18.jar;src/" App` to run the application. Please note, you might have to substitute "`;`" with "`:`" depending on your operating system.
* The lib folder contains the jar files that is needed for running JDBC
* As an alternative to the last 4 points, you can youse an IDE for the automatic management of classpath and library dependencies

## Author
Samuel Dalvai