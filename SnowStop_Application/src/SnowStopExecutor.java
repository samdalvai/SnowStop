import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Author: Samuel Dalvai
 * <p>
 * Extends SqlExecutor with SnowstopRelated methods
 */
public class SnowStopExecutor extends SqlExecutor {

    public SnowStopExecutor() {
    }

    /*
     * =================================================================== --
     * METHODS FOR INSERTION OF CUSTOMERS --
     * ================================================================
     */

    public void insertNewCustomer() throws SQLException {
        int code = getNextCustomerCode();
        String name = SnowstopInput.getCustomerNameFromUser();

        if (isInTable("customer", "name = '" + name + "'")) {
            System.out.print("A customer with the same name is already in the database. ");
            if (InputFunction.retry())
                insertNewCustomer();
        } else {
            String zip = SnowstopInput.getZipFromUser();

            if (isInTable("city", "zip = '" + zip + "'")) {
                System.out.println("Here are some possibilities for the city:");
                showTable("city", "zip,name,province", "zip = '" + zip + "'", 18, 10);
            }

            String city = SnowstopInput.getCityFromUser();

            if (!isInTable("city", "zip = '" + zip + "'" + " AND name= '" + city + "'")) {
                System.out.print(
                        "The provided Zip code along with the City does not exist, do you want to add it to the database? (y/n) ");
                char choice = InputFunction.getCharFromUser("yYnN");
                if (choice == 'y' || choice == 'Y')
                    insertNewCity(zip, city);
                else {
                    System.out.println("Exiting customer insertion procedure...");
                    return;
                }
            }

            int discount = SnowstopInput.getCustomerDiscountFromUser();

            insertInTable("customer", Arrays.asList(code, name, zip, city, discount));

            insertCustomerNumber(code);

            System.out.println("Insertion of customer (" + code + "," + name + "," + zip + "," + city + "," + discount
                    + ") completed...");
        }

    }

    private int getNextCustomerCode() throws SQLException {
        Object[] parameters = getRowValues("customer", "MAX(code)");
        if (parameters == null)
            return 40000000; // if there are no customers return the first code

        return (int) (parameters[0]) + 1;
    }

    private void insertCustomerNumber(int customerCode) throws SQLException {
            System.out.print("Please insert the customers phone number (ex. 3481234567): ");
            String number = InputFunction.getStringFromUser("[0-9]{10}");

            if (isInTable("phone","number = '" + number + "'")){
                System.out.print("That number is already in the database. ");
                if (InputFunction.retry())
                    insertCustomerNumber(customerCode);
            } else {
                insertInTable("phone", Arrays.asList(number, customerCode));

                System.out.print("Do you want to add more numbers? (y/n) ");
                char choice = InputFunction.getCharFromUser("yYnN");

                if (choice == 'y' || choice == 'Y')
                    insertCustomerNumber(customerCode);
            }
    }

    /*
     * =================================================================== --
     * METHODS FOR INSERTION OF PRODUCTS --
     * ================================================================
     */

    public void insertNewProduct() throws SQLException {
        int code = SnowstopInput.getNewProductCodeFromUser();

        if (isInTable("product", "code = " + code + "")) {
            System.out.print("A product with the same code is already in the database... ");
            if (InputFunction.retry())
                insertNewProduct();
        } else {
            String name = SnowstopInput.getProductNameFromUser();
            String material = SnowstopInput.getProductMaterialFromUser();
            String color = "null";

            if (material.equals("Painted Steel"))
                color = SnowstopInput.getProductColorFromUser();

            double price = SnowstopInput.getProductPriceFromUser();

            insertInTable("product", Arrays.asList(code, name, material, color, price));
            System.out.println("Insertion of product (" + code + "," + name + "," + material + "," + color + "," + price
                    + ") completed...");

            String retainerType = SnowstopInput.getProductTypeFromUser();
            String category = SnowstopInput.getProductCategoryFromUser();

            switch (category) {
                case "Retainer":
                    insertNewRetainer(code, retainerType);
                    break;
                case "Holder":
                    insertNewHolder(code, retainerType);
                    break;
                case "Accessory":
                    insertNewAccessory(code, retainerType);
                    break;
                default:
                    System.err.println("Something went wrong with the category of product");
                    break;
            }
        }

    }

    private void insertNewRetainer(int code, String type) throws SQLException {
        double resistance = SnowstopInput.getResistanceFromUser();
        int measure = SnowstopInput.getRetainerMeasureFromUser();

        String profile;

        if (type.equals("Tube"))
            profile = "null";
        else
            profile = SnowstopInput.getRetainerProfileFromUser();

            insertInTable("retainer", Arrays.asList(code, resistance, type, measure, profile));
        System.out.println("Insertion of retainer (" + code + "," + resistance + "," + type + "," + measure + ","
                + profile + ") completed...");
    }

    private void insertNewHolder(int code, String type) throws SQLException {
        double resistance = SnowstopInput.getResistanceFromUser();
        String roofType = SnowstopInput.getRoofTypeFromUser();

        insertInTable("holder", Arrays.asList(code, resistance, roofType, type));
        System.out.println(
                "Insertion of holder (" + code + "," + resistance + "," + roofType + "," + type + ") completed...");
    }

    public void insertNewAccessory(int code, String type) throws SQLException {
        String measure = SnowstopInput.getAccessoryMeasureFromUser();
        String accessoryType = SnowstopInput.getAccessoryTypeFromUser();

        insertInTable("accessory", Arrays.asList(code, measure, type, accessoryType));
        System.out.println(
                "Insertion of accessory (" + code + "," + measure + "," + type + "," + accessoryType + ") completed...");
    }

    /*
     * =================================================================== --
     * METHODS FOR INSERTION OF CITIES --
     * ================================================================
     */

    public void insertNewCity() throws SQLException {
        String zip = SnowstopInput.getZipFromUser();
        String name = SnowstopInput.getCityFromUser();

        if (isInTable("city", "zip = '" + zip + "' AND name = '" + name + "'")) {
            System.err.print(
                    "The zip code along with the city is already in the database. ");
            if (InputFunction.retry())
                insertNewCity();
        } else
            insertNewCity(zip, name);
    }

    private void insertNewCity(String zip, String name) throws SQLException {
        System.out.println("Please select the province for " + zip + " " + name + ", (ex. BZ)");
        System.out.println("Here are some possibilities for the province:");
        showTable("province", "shorthand,name", 18, 10);
        String province = SnowstopInput.getProvinceFromUser();

        while (!isInTable("province", "shorthand = '" + province + "'")) {
            System.out.print(
                    "The provided province shorthand does not exist, do you want to add it to the database? (y/n) ");
            char choice = InputFunction.getCharFromUser("yYnN");
            if (choice == 'y' || choice == 'Y') {
                insertProvince(province);
                break;
            } else {
                System.out.println("Please select the province for " + zip + " " + name + ", (ex. BZ)");
                System.out.println("Here are some possibilities for the province:");
                showTable("province", "shorthand,name", 18, 10);
                province = SnowstopInput.getProvinceFromUser();
            }
        }

        int altitude = SnowstopInput.getAltitudeFromUser();

        insertInTable("city", Arrays.asList(zip, name, province, altitude));

        System.out
                .println("Insertion of city (" + zip + "," + name + "," + province + "," + altitude + ") completed...");
    }

    private void insertProvince(String shorthand) throws SQLException {
        String name = SnowstopInput.getProvinceNameFromUser();

        while (isInTable("province", ("name = '" + name + "'"))) {
            System.err.println(
                    "Province with name " + name + " is already in the database, please select another name: ");
            name = InputFunction.getStringFromUser(1, 40).toUpperCase();
        }

        String zone = InputFunction.getChoicesFromUser("Please select the climatic zone for the province:",
                new String[] { "I-A", "I-M", "II", "III" });

        double baseLoad = SnowstopInput.getProvinceBaseLoadFromUser();

        insertInTable("province", Arrays.asList(shorthand, name, zone, baseLoad));

        System.out.println(
                "Insertion of province (" + shorthand + "," + name + "," + zone + "," + baseLoad + ") completed...");
    }

    /*
     * =================================================================== --
     * METHODS FOR INSERTION OF BUILDING SITE AND COMPUTATION --
     * ================================================================
     */

    public void insertNewComputationAndBS() throws SQLException {
        int code = getNextComputationCode();
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());

        Object[] buildingSiteData = getBuildingSiteDataFromUser(code);

        if (buildingSiteData == null) {
            System.err.println("Something went wrong with the insertion of the data. ");
            if (InputFunction.retry())
                insertNewComputationAndBS();
        } else {
            // need to disable constraint because of circular reference between the two
            // entities
            disableForeignKeyBSConstraint();
            insertInTable("buildingsite", Arrays.asList(buildingSiteData));
            System.out.println("Insertion of building site (" + buildingSiteData[0] + "," + buildingSiteData[1] + ","
                    + buildingSiteData[2] + "," + buildingSiteData[3] + "," + buildingSiteData[4] + ","
                    + buildingSiteData[5] + "," + buildingSiteData[6] + "," + buildingSiteData[7] + ") completed...");

            insertInTable("computation", Arrays.asList(code, date, 0, 0));
            System.out.println(
                    "Insertion of computation (" + code + "," + date + "," + "XXX" + "," + "XXX" + ") completed...");

            // now we enable the constraint again
            enableForeignKeyBSConstraint();
        }

    }

    private void disableForeignKeyBSConstraint() throws SQLException {
        executeQueryFromString("ALTER TABLE BuildingSite DROP CONSTRAINT IF EXISTS buildingSiteWithComputation\n");
    }

    private void enableForeignKeyBSConstraint() throws SQLException {
        executeQueryFromString("ALTER TABLE BuildingSite\n"
                + "ADD CONSTRAINT buildingSiteWithComputation FOREIGN KEY (computationCode)\n"
                + "REFERENCES Computation (code)\n" + "ON UPDATE CASCADE\n" + "ON DELETE CASCADE\n"
                + "DEFERRABLE INITIALLY DEFERRED;\n");
    }

    private int getNextComputationCode() throws SQLException {
        Object[] parameters = getRowValues("computation", "MAX(code)");

        if (parameters == null)
            return 1; // if there are no computations return the first code

        return (int) (parameters[0]) + 1;
    }

    public Object[] getBuildingSiteDataFromUser(int computationCode) throws SQLException {
        String name = SnowstopInput.getBSNameFromUser();
        String zip = SnowstopInput.getZipFromUser();

        if (isInTable("city", "zip = '" + zip + "'")) {
            System.out.println("Here are some possibilities fo the city:");
            showTable("city", "zip,name,province", "zip = '" + zip + "'", 18, 10);
        }

        String city = SnowstopInput.getCityFromUser();

        if (isInTable("buildingsite", "name = '" + name + "' AND zip = '" + zip + "' AND city = '" + city + "'")) {
            System.out.print(
                    "A building site with the same name and location is already in the database. ");
            if (InputFunction.retry()) {
                getBuildingSiteDataFromUser(computationCode);
            } else
                return null;
        }
        if (!isInTable("city", "zip = '" + zip + "' AND name = '" + city + "'")) {
            System.out.print(
                    "The provided Zip code along with the City does not exist, do you want to add it to the database? (y/n) ");
            char choice = InputFunction.getCharFromUser("yYnN");
            if (choice == 'y' || choice == 'Y')
                insertNewCity(zip, city);
            else {
                System.out.println("Exiting buildingsite insertion procedure...");
                return null;
            }
        }

        double bsLength = SnowstopInput.GetBSLengthFromUser();
        double bsWidth = SnowstopInput.GetBSWidthFromUser();
        double bsSteepness = SnowstopInput.GetBSSteepnessFromUser();

        String covering = SnowstopInput.getRoofTypeFromUser();

        return new Object[] { name, zip, city, computationCode, bsLength, bsWidth, bsSteepness, covering };

    }

    /*
     * =================================================================== --
     * METHODS FOR INSERTION OF SUPPLY OFFERS --
     * ================================================================
     */

    public void insertNewOffer() throws SQLException {
        if ((int) getRowValues("computation", "count(*)")[0] == 0)
            System.err.println("No snowload computation in the database, please create one first...");
        else{
            int offerCode = getNextOfferCode();
            int computationCode = getComputationCodeFromUser();
            if (computationCode != -1) {
                // insert supply offer with dummy values
                insertInTable("supplyoffer", Arrays.asList(offerCode, computationCode,
                        new java.sql.Date(Calendar.getInstance().getTime().getTime()), 0, 0, 100, 100));
                try {
                    selectProductsForOffer(offerCode, computationCode);
                    System.out.println("Insertion of supply offer (" + offerCode + ") completed...");
                    addCustomerToOffer(offerCode);

                } catch (SQLException e) {
                    System.err.println(e.getMessage() + " reverting changes...");
                    deleteFromTable("supplyOffer", "code = " + offerCode);
                }
            }
        }
    }

    private int getNextOfferCode() throws SQLException {
        Object[] parameters = getRowValues("supplyoffer", "MAX(code)");

        if (parameters == null)
            return 1; // if there are no computations return the first code

        return (int) (parameters[0]) + 1;
    }

    private int getComputationCodeFromUser() throws SQLException {
        System.out.println("Here are some possibilities for the computation with the building site:");
        showTable("computation JOIN buildingsite ON computation.code = buildingsite.computationcode",
                "computation.code,buildingsite.name,buildingsite.zip,buildingsite.city,computation.roofload", 18, 10);
        System.out.print("Please insert the computation code of the building site: ");

        int computationCode = InputFunction.getIntFromUser(0, 100000);
        if (!isInTable("computation", "code = " + computationCode)) {
            System.err.print("The code of the computation is not in the database, do you want to try again? (y/n) ");
            char choice = InputFunction.getCharFromUser("yYnN");
            if (choice == 'y' || choice == 'Y') {
                return getComputationCodeFromUser();
            } else
                return -1;
        }

        return computationCode;
    }

    public void selectProductsForOffer(int offerCode, int computationCode) throws SQLException {
        double roofLoad = getComputationRoofLoad(computationCode);
        String roofType = getBSRoofType(computationCode);
        String retainerType = SnowstopInput.getRetainerTypeFromUser();

        
        if ((int) getRowValues("holder", "count(*)", "roofType = '" + roofType + "' AND retainerType ='" + retainerType + "'")[0] == 0 || (int) getRowValues("retainer", "count(*)", "retainerType ='" + retainerType + "'")[0] == 0){
            System.err.print("No available holder/retainer pair in the database for retainerType " + retainerType + " and roofType " + roofType + ". ");
            if (InputFunction.retry())
                selectProductsForOffer(offerCode, computationCode);
            else
                throw new SQLException("No available holder/retainer");
        } else{
        
            selectHolderForOffer(roofLoad, roofType, retainerType, offerCode);
            selectRetainerForOffer(retainerType, offerCode);

            System.out.print("Do you want to add accessories (max 2)? (y/n) ");
            char choice = InputFunction.getCharFromUser("yYnN");
            if (choice == 'y' || choice == 'Y') {
                selectAccessoryForOffer(0, retainerType, offerCode, "",computationCode);
            }
        }
    }

    private double getComputationRoofLoad(int computationCode) throws SQLException {
        return (double) getRowValues("computationWithBSData", "roofload", "code = " + computationCode)[0];
    }

    private String getBSRoofType(int computationCode) throws SQLException {
        return (String) getRowValues("computationWithBSData", "covering", "code = " + computationCode)[0];
    }

    public void selectHolderForOffer(double roofLoad, String roofType, String retainerType, int offerCode)
            throws SQLException {

        System.out.println("Here are the avaliable holders for the roof:");
        showTable("product JOIN holder ON product.code = holder.code",
                "product.code, product.name, product.material, product.color, product.price, holder.resistance",
                "holder.roofType = '" + roofType + "' AND holder.retainerType = '" + retainerType + "'", 15, 10);

        int holderCode = SnowstopInput.getHolderCodeFromUser();

        if (!isInTable("holder", "code = " + holderCode)) {
            System.out.println("That code is not present in the holder table, please retry...");
            selectHolderForOffer(roofLoad, roofType, retainerType, offerCode);
        } else {
            int quantity = SnowstopInput.getQuantityFromUser(2, 5000);
            double resistance = getHolderResistance(holderCode);
            int distance = SnowstopInput.getDistanceFromUser();
            int rows = SnowstopInput.getRowsFromUser();

            while ((rows * (1000.0 / distance) * resistance) < roofLoad) {
                System.out.println("The roof load (" + roofLoad + ") is higher than the resistance ("
                        + (rows * (1000.0 / distance) * resistance)
                        + "), please change the distance or the number of rows...");
                distance = SnowstopInput.getDistanceFromUser();
                rows = SnowstopInput.getRowsFromUser();

            }

            insertInTable("offerForProduct", Arrays.asList(offerCode, holderCode, quantity));

            updateTable("supplyoffer", "rows", "code = " + offerCode, rows);
            updateTable("supplyoffer", "distance", "code = " + offerCode, distance);
        }
    }

    public double getHolderResistance(int code) throws SQLException {
        Object in = getRowValues("holder", "resistance", "code = " + code)[0];
        return Double.parseDouble(in.toString());
    }

    public void selectRetainerForOffer(String retainerType, int offerCode) throws SQLException {
        System.out.println("Here are the avaliable retainers for the roof:");
        showTable("product JOIN retainer ON product.code = retainer.code",
                "product.code, product.name, retainer.measure, product.material, product.color, product.price",
                "retainer.retainerType = '" + retainerType + "'", 15, 10);

        int retainerCode = SnowstopInput.getRetainerCodeFromUser();

        if (!isInTable("retainer", "code = " + retainerCode)) {
            System.out.println("That code is not present in the retainer table, please retry...");
            selectRetainerForOffer(retainerType, offerCode);
        } else {
            int quantity = SnowstopInput.getQuantityFromUser(2, 5000);
            insertInTable("offerForProduct", Arrays.asList(offerCode, retainerCode, quantity));
        }
    }

    public void selectAccessoryForOffer(int accessoryCounter, String retainerType, int offerCode,
            String previousAccessoryType, int computationCode) throws SQLException {
        if (accessoryCounter == 0 && (int) getRowValues("accessory", "count(*)", "retainerType ='" + retainerType + "'")[0] == 0){
            System.out.println("No available accessory in the database for retainerType: " + retainerType + ". ");
            if (InputFunction.retry()){
                throw new SQLException("No available accessory");
            }                
        } else if (accessoryCounter == 1 && (int) getRowValues("accessory", "count(*)", "retainerType ='" + retainerType + "'" + " AND type <> '" + previousAccessoryType + "'")[0] == 0){
            System.out.println("No available accessory in the database for retainerType: " + retainerType + " that is not of type " + previousAccessoryType + ". ");
            if (InputFunction.retry()){
                throw new SQLException("No available accessory");
            }
        } else {
        
            if (accessoryCounter == 0) {
                System.out.println("Here are the avaliable accessories for the roof:");
                showTable("product JOIN accessory ON product.code = accessory.code",
                        "product.code, product.name, accessory.measure, accessory.type, product.material, product.color, product.price",
                        "accessory.retainerType = '" + retainerType + "'", 15, 10);
            }

            if (accessoryCounter == 1) {
                System.out.println("Here are the avaliable accessories for the roof:");
                showTable("product JOIN accessory ON product.code = accessory.code",
                        "product.code, product.name, accessory.measure, accessory.type, product.material, product.color, product.price",
                        "accessory.retainerType = '" + retainerType + "'" + " AND type <> '" + previousAccessoryType + "'",
                        15, 10);
            }

            int accessoryCode = SnowstopInput.getAccessoryCodeFromUser();

            if (!isInTable("accessory", "code = " + accessoryCode)) {
                System.out.println("That code is not present in the accessory table, please retry...");
                selectAccessoryForOffer(accessoryCounter, retainerType, offerCode, previousAccessoryType, computationCode);
            } else {
                int quantity = SnowstopInput.getQuantityFromUser(2, 5000);
                insertInTable("offerForProduct", Arrays.asList(offerCode, accessoryCode, quantity));

                if (accessoryCounter == 0) {
                    System.out.print("Do you want to add another accessory ? (y/n) ");
                    char choice = InputFunction.getCharFromUser("yYnN");
                    if (choice == 'y' || choice == 'Y') {
                        String accessoryType = (String) getRowValues("accessory", "type",
                                "code = '" + accessoryCode + "'")[0];
                        selectAccessoryForOffer(1, retainerType, offerCode, accessoryType, computationCode);
                    }
                }
            }
        }
    }

    private void deleteOfferWithProducts(int offercode) throws SQLException{
        deleteFromTable("offerforproduct", "offercode = " + offercode);
    }

    public void addCustomerToOffer(int offerCode) throws SQLException {
        System.out.println("Please select the customer for the offer n. " + offerCode);
        String name = SnowstopInput.getCustomerNameFromUser();

        if (!isInTable("customer", "name LIKE '%" + name + "%'")){
            String options = InputFunction.getChoicesFromUser("No results for \"" + name + "\"" + ", you have two options:", new String[]{"Try again","Insert new customer"});

            if (options.equals("Try again"))
                addCustomerToOffer(offerCode);
            else{
                insertNewCustomer();
                addCustomerToOffer(offerCode);
            }
        } else {
            System.out.println("Here are the possible customers with that name:");
            showTable("customer", "*" , "name LIKE '%" + name + "%'", 18, 10);

            int customerCode = SnowstopInput.getCustomerCodeFromUser();
            if (isInTable("offerforcustomer","customercode = " + customerCode + " AND offercode = " + offerCode)){
                System.err.println("ERROR: Cannot add the same customer more than once to the same offer, please retry...");
                addCustomerToOffer(offerCode);
            } else {
                if (!isInTable("customer", "name LIKE '%" + name + "%' AND code = " + customerCode)) {
                    System.out.println("That code is not present in the results, please retry...");
                    addCustomerToOffer(offerCode);
                } else {
                    insertInTable("offerForCustomer", Arrays.asList(offerCode,customerCode));
                    System.out.println("Insertion of supply offer for customer (" + offerCode + "," + customerCode + ") completed...");

                    System.out.print("Do you want to add another customer? (y/n) ");
                    char choice = InputFunction.getCharFromUser("yYnN");
                    if (choice == 'y' || choice == 'Y') {
                        addCustomerToOffer(offerCode);
                    }
                }
            }
        }
    }

    /*
     * =================================================================== --
     * METHODS FOR VISUALISING SUPPLY OFFERS
     * ================================================================
     */

     public void visualizeOffers() throws SQLException{
        if ((int) getRowValues("supplyoffer", "count(*)")[0] == 0)
            System.err.println("No supply offer in the database, please create one first...");
        else {
            System.out.println("Here are all the available offers:");
            showTable("customerWithOfferAndPrice", "*", 18, 10);
            
            int customerCode = SnowstopInput.getCustomerCodeFromUser();
            if (!isInTable("customerWithOfferAndPrice","customercode = " + customerCode)){
                System.err.println("ERROR: that customer has no supply offers...");
                
                if (InputFunction.retry())
                    visualizeOffers();
            } else {
                String customerName = (String) getRowValues("customer", "name", "code = " + customerCode)[0];
                System.out.println("Here are all the offers for customer \"" + customerName + "\":");
                showTable("customerWithOfferAndPrice", "*", "customercode = " + customerCode, 18, 10);
                
                int offerCode = SnowstopInput.getOfferCodeFromUser();
                if (!isInTable("customerWithOfferAndPrice","offerCode = " + offerCode + " AND name = '" + customerName + "'")){
                    System.err.println("ERROR: that supply offer does not exist for the customer \"" + customerName + "\":");                
                    if (InputFunction.retry())
                        visualizeOffers();
                } else {
                    System.out.println("Supply offer details for offer N." + offerCode + ":");
                    showTable("offerWithProductsAndPrice JOIN product ON offerWithProductsAndPrice.productcode = product.code", "offerWithProductsAndPrice.productcode, product.name, product.material, offerWithProductsAndPrice.quantity,offerWithProductsAndPrice.price, offerWithProductsAndPrice.subtotal","offercode = " + offerCode, 18, 10);
                    showTable("computation JOIN supplyoffer ON computation.code = supplyoffer.computationcode", "computation.code AS computation, computation.groundload, computation.roofload, supplyoffer.rows, supplyoffer.distance, supplyoffer.totalResistance, supplyoffer.totalprice","supplyoffer.code = " + offerCode, 15, 10);


                    String options = InputFunction.getChoicesFromUser("You have the following options:", new String[]{"Visualize other supply offers","Main menu"});

                    if (options.equals("Visualize other supply offers"))
                        visualizeOffers();

                }

            }
        }
     }

     /*
     * =================================================================== --
     * METHODS FOR UPDATING PRICE OF PRODUCT
     * ================================================================
     */

     public void updatePriceOfProduct() throws SQLException{
        if ((int) getRowValues("product", "count(*)")[0] == 0)
            System.err.println("No product in the database, please create one first...");
        else {
            String select = InputFunction.getChoicesFromUser("How do you want to search for a product?", new String[]{"Product code","Product name","List all products"});

            if (select.equals("Product code")){
                updateProductPriceByCode();
            }
            else if (select.equals("Product name")){
                String productName = SnowstopInput.getProductNameFromUser();

                if (!isInTable("product", "name LIKE '%" + productName + "%'")) {
                    System.out.println("No results for \"" + productName + "\', please retry..." );
                    updatePriceOfProduct();
                }
                else{
                    System.out.println("Here are some possibilites:");
                    showTable("product", "*", "name LIKE '%" + productName + "%'", 15, 10);
                    updateProductPriceByCode();
                }
            }
            else {
                System.out.println("Here are all the products:");
                showTable("product", "*", 15, 10);
                updateProductPriceByCode();
            }
        }
     }

     private void updateProductPriceByCode() throws SQLException{
        int productCode = SnowstopInput.getProductCodeFromUser();
            if (!isInTable("product", "code = " + productCode)){
                System.err.println("ERROR: the product with code \"" + productCode + "\" does not exist...");                
                if (InputFunction.retry())
                    updatePriceOfProduct();
            } else{
                showTable("product", "*", "code = " + productCode, 15, 10);
                double newPrice = SnowstopInput.getProductNewPriceFromUser();

                updateTable("product", "price", "code = " + productCode, newPrice);
                System.out.println("Price updated for product with code \"" + productCode +"\". New price: " + newPrice + " €");

                System.out.print("Do you want to update another product? (y/n) ");
                        char choice = InputFunction.getCharFromUser("yYnN");
                        if (choice == 'y' || choice == 'Y')
                            updatePriceOfProduct();
            }
     }

     /*
     * =================================================================== --
     * METHODS FOR UPDATE OF CITIES
     * ================================================================
     */
    
    public void updateCityData() throws SQLException{
        if ((int) getRowValues("city", "count(*)")[0] == 0)
            System.err.println("No city in the database, please create one first...");
        else {
            String select = InputFunction.getChoicesFromUser("How do you want to search for a city?", new String[]{"City Zip code","City name"});

            if (select.equals("City Zip code")){
                updateCityDataByZipCode();
            }
            else{
                String cityName = SnowstopInput.getCityFromUser();

                if (!isInTable("city", "name LIKE '" + cityName + "%'")) {
                    System.out.println("No results for \"" + cityName + "\'..." );
                    if (InputFunction.retry())
                            updateCityData();
                }
                else{
                    System.out.println("Here are some possibilites:");
                    showTable("city", "*", "name LIKE '" + cityName + "%'", 18, 10);
                    String zipCode = SnowstopInput.getZipFromUser();
                    cityName = SnowstopInput.getCityFromUser();
                    updateCity(zipCode, cityName);
                }
            }
        }
    }

    private void updateCityDataByZipCode() throws SQLException{
        String zipCode = SnowstopInput.getZipFromUser();
            if (!isInTable("city", "zip = '" + zipCode + "'")){
                System.err.println("ERROR: no city with zip code \"" + zipCode + "\" exists...");                
                if (InputFunction.retry())
                        updateCityData();
            } else{
                System.out.println("Here are some possibilites:");
                showTable("city", "*", "zip = '" + zipCode + "'", 18, 10);
                String cityName = SnowstopInput.getCityFromUser();
                updateCity(zipCode, cityName);                
            }
     }

     private void updateCity(String zip, String name) throws SQLException{
        if (!isInTable("city", "zip = '" + zip + "'" + " AND name = '" + name + "'")){
            System.err.println("ERROR: no city with zip code \"" + zip + "\" and name \"" + name + "\" exists...");                
            if (InputFunction.retry())
                updateCityData();
        } else{
            String newZip = SnowstopInput.getNewZipFromUser();
            String newName = SnowstopInput.getNewCityNameFromUser();

            if (!isInTable("city", "zip = '" + newZip + "'" + " AND name = '" + newName + "'")){
                updateTable("city", "zip", "zip = '" + zip + "'" + " AND name = '" + name + "'", newZip);
                updateTable("city", "name", "zip = '" + newZip + "'" + " AND name = '" + name + "'", newName);
                
                System.out.println("Zip updated for city with zip code \"" + zip + "\" and \"" + name + "\".");
                System.out.println("New data: ");
                showTable("city", "*", "zip = '" + newZip + "'" + " AND name = '" + newName + "'", 18, 10);

                System.out.print("Do you want to update another city? (y/n) ");
                        char choice = InputFunction.getCharFromUser("yYnN");
                        if (choice == 'y' || choice == 'Y')
                            updateCityData();
            } else {
                System.err.println("ERROR: a city with zip code \"" + newZip + "\" and name \"" + newName + "\" is already in the database... Please retry");
                updateCityData();
            }
        }



     }
}