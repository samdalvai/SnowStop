import java.sql.SQLException;

/**
 * Author: Samuel Dalvai
 * <p>
 * Class that manages the main menu for the interaction with the Snowstop DB
 */
public class SnowStopTerminal {

    private final SnowStopExecutor executor;
    private final String database;
    private final String username;
    private final String password;

    public SnowStopTerminal(String database, String username, String password) {
        this.executor = new SnowStopExecutor();
        this.database = database;
        this.username = username;
        this.password = password;

    }

    public void run() throws SQLException, ClassNotFoundException {

        executor.connect(database, username, password);
        executor.getInfo();
        System.out.println("Your are connected to the database: " + database);
        showOptionsMainMenu();

    }

    private void showOptionsMainMenu() {

        int choice;
        try {
            while (true) {
                System.out.println("\n*** MAIN MENU SNOWSTOP DB ***");
                System.out.println("Select one of the following options:\n" +
                        "(1) Insertion of a new customer\n" +
                        "(2) Insertion of a new product\n" +
                        "(3) Insertion of a new city\n" +
                        "(4) Creation of a snowload computation for a building site\n" +
                        "(5) Creation of a supply offer\n" +
                        "(6) List the supply offers for a customer\n" +
                        "(7) Update prices of snowstop products\n" +
                        "(8) Update data of cities\n" +
                        "(9) Termination of the program");
                choice = InputFunction.getIntFromUser(1, 9);

                switch (choice) {
                    case 1: {
                        customerMenu();
                        break;
                    }
                    case 2: {
                        productMenu();
                        break;
                    }
                    case 3: {
                        cityMenu();
                        break;
                    }
                    case 4: {
                        computationMenu();
                        break;
                    }
                    case 5: {
                        offerMenu();
                        break;
                    }
                    case 6: {
                        supplyOfferForCustomerMenu();
                        break;
                    }
                    case 7: {
                        updatePriceMenu();
                        break;
                    }
                    case 8: {   
                        updateCityDataMenu();
                        break;
                    }
                    case 9: {
                        programQuit();
                        return;
                    }

                }
            }
        } catch (Exception e) {
            System.err.println("Something went wrong -> " + e.getMessage() + "\n");
            showOptionsMainMenu();
        }

    }

    private void customerMenu() throws SQLException {
        System.out.println("\n*** CUSTOMER MENU - SNOWSTOP DB ***");
        executor.insertNewCustomer();
    }

    private void productMenu() throws SQLException {
        System.out.println("\n*** PRODUCT MENU - SNOWSTOP DB ***");
        executor.insertNewProduct();
    }

    private void cityMenu() throws SQLException {
        System.out.println("\n*** CITY MENU - SNOWSTOP DB ***");
        executor.insertNewCity();
    }

    private void computationMenu() throws SQLException {
        System.out.println("\n*** SNOWLOAD COMPUTATION MENU - SNOWSTOP DB ***");
        executor.insertNewComputationAndBS();
    }

    private void offerMenu() throws SQLException {
        System.out.println("\n*** SUPPLY OFFER MENU - SNOWSTOP DB ***");
        executor.insertNewOffer();
    }

    private void supplyOfferForCustomerMenu() throws SQLException {
        System.out.println("\n*** SUPPLY OFFER FOR CUSTOMER MENU - SNOWSTOP DB ***");
        executor.visualizeOffers();
    }

    private void updatePriceMenu() throws SQLException {
        System.out.println("\n*** UPDATE PRICE OF PRODUCT - SNOWSTOP DB ***");
        executor.updatePriceOfProduct();
    }

    private void updateCityDataMenu() throws SQLException {
        System.out.println("\n*** UPDATE DATA OF CITIES - SNOWSTOP DB ***");
        executor.updateCityData();
    }

    private void programQuit() throws SQLException {
        executor.closeConnection();
        System.out.println("Quitting program, goodbye");
    }

}
