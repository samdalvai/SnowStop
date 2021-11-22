/**
 * Author: Samuel Dalvai
 * 
 * Methods devoted to getting specific input related to Snowstop values from the user
 */
public class SnowstopInput {

    /* ===================================================================
    -- METHODS FOR INSERTION OF CUSTOMERS
    -- ================================================================ */

    public static String getCustomerNameFromUser() {
        System.out.print("Please insert the customer name (max 50 characters): ");
        return InputFunction.getStringFromUser(1, 50).toUpperCase();
    }

    public static int getCustomerDiscountFromUser() {
        System.out.print("Please insert the discount for the customer (0 to 30): ");
        return InputFunction.getIntFromUser(0, 30);
    }

    public static int getCustomerCodeFromUser(){
        System.out.print("Please insert the code of the customer (40000000 to 50000000): ");
        return InputFunction.getIntFromUser(40000000, 50000000);
    }


    /* ===================================================================
    -- METHODS FOR INSERTION OF PRODUCTS
    -- ================================================================ */

    public static int getNewProductCodeFromUser() {
        System.out.print("Please insert the ***new*** product code (between 100000 and 999999): ");
        return InputFunction.getIntFromUser(100000, 999999);
    }

    public static int getProductCodeFromUser() {
        System.out.print("Please insert the product code (between 100000 and 999999): ");
        return InputFunction.getIntFromUser(100000, 999999);
    }

    public static String getProductNameFromUser() {
        System.out.print("Please insert the product name (max 40 characters): ");
        return InputFunction.getStringFromUser(1, 40);
    }

    public static String getProductMaterialFromUser() {
        return InputFunction.getChoicesFromUser("Please select the type of material:", new String[]{"Stainless Steel", "Aluminium", "Copper", "Painted Steel", "Zink Steel"});
    }

    public static String getProductColorFromUser() {
        return InputFunction.getChoicesFromUser("Please select the color:", new String[]{"Red", "Brown", "Red", "Grey"});
    }

    public static double getProductPriceFromUser() {
        System.out.print("Please the price of the new product (> 0): ");
        return InputFunction.getDoubleFromUser(0, Integer.MAX_VALUE);
    }

    public static double getProductNewPriceFromUser() {
        System.out.print("Please insert the ***new*** price for the product (> 0): ");
        return InputFunction.getDoubleFromUser(0, Integer.MAX_VALUE);
    }

    public static String getProductTypeFromUser() {
        return InputFunction.getChoicesFromUser("Please select the type of product compatibility:", new String[]{"Grid", "Tube"});
    }

    public static String getProductCategoryFromUser() {
        return InputFunction.getChoicesFromUser("Please select the product category:", new String[]{"Retainer", "Holder", "Accessory"});
    }

    public static double getResistanceFromUser() {
        System.out.print("Please insert the resistance of the product (> 0): ");
        return InputFunction.getDoubleFromUser(0);
    }

    public static int getRetainerMeasureFromUser() {
        System.out.print("Please insert the measure of the retainer (> 0): ");
        return InputFunction.getIntFromUser(0, 250);
    }

    public static String getRetainerProfileFromUser() {
        System.out.print("Please insert the profile of the retainer (at most 20 characters): ");
        return InputFunction.getStringFromUser(1, 20);
    }

    public static String getRoofTypeFromUser() {
        return InputFunction.getChoicesFromUser("Please select the roof type compatibility:", new String[]{"concrete-tile", "ondulated-plate", "trapezoidal-sheet", "standing-seam-sheet", "flat-tile"});
    }

    public static String getAccessoryMeasureFromUser() {
        System.out.print("Please insert the measure of the accessory (at most 30 characters): ");
        return InputFunction.getStringFromUser(1, 30);
    }

    public static String getAccessoryTypeFromUser() {
        return InputFunction.getChoicesFromUser("Please select the type of accessory:", new String[]{"Ice-Retainer", "Connection"});
    }


    /* ===================================================================
    -- METHODS FOR INSERTION OF CITIES
    -- ================================================================ */

    public static String getZipFromUser() {
        System.out.print("Please insert the ZIP code (ex. 39040): ");
        return InputFunction.getStringFromUser("\\d{5}").toUpperCase();
    }

    public static String getNewZipFromUser() {
        System.out.print("Please insert the **new** ZIP code (ex. 39040): ");
        return InputFunction.getStringFromUser("\\d{5}").toUpperCase();
    }
    
    public static String getNewCityNameFromUser() {
        System.out.print("Please insert the ***new*** city name (ex. MILANO): ");
        return InputFunction.getStringFromUser(1, 40).toUpperCase();
    }
   
    public static String getCityFromUser() {
        System.out.print("Please insert the city name (ex. MILANO): ");
        return InputFunction.getStringFromUser(1, 40).toUpperCase();
    }

    public static String getProvinceFromUser() {
        System.out.print("Please insert the province shorthand (ex. BZ): ");
        return InputFunction.getStringFromUser("[A-Z]{2}|[a-z]{2}").toUpperCase();
    }

    public static int getAltitudeFromUser() {
        System.out.print("Please insert the altitude for the city (must be geater than 0 and less than 2500): ");
        return InputFunction.getIntFromUser(1, 2500);
    }

    public static String getProvinceNameFromUser() {
        System.out.print("Please insert the province name (ex. MILANO): ");
        return InputFunction.getStringFromUser(1, 40).toUpperCase();
    }

    public static double getProvinceBaseLoadFromUser(){
        System.out.print("Please insert the baseload for the province (must be > 0 and <= 5): ");
        return InputFunction.getDoubleFromUser(0, 5);
    }

    /* ===================================================================
    -- METHODS FOR INSERTION OF BUILDING SITE AND COMPUTATION
    -- ================================================================ */

    public static String getBSNameFromUser() {
        System.out.print("Please insert the building site name (max 30 characters): ");
        return InputFunction.getStringFromUser(1, 30);
    }

    public static double GetBSLengthFromUser() {
        System.out.print("Please insert the roof length (> 0): ");
        return InputFunction.getDoubleFromUser(0);
    }

    public static double GetBSWidthFromUser() {
        System.out.print("Please insert the roof width (> 0): ");
        return InputFunction.getDoubleFromUser(0);
    }

    public static double GetBSSteepnessFromUser() {
        System.out.print("Please insert the roof steepness (>= 0 <= 90): ");
        return InputFunction.getDoubleFromUser(0, 90);
    }

    /* ===================================================================
    -- METHODS FOR INSERTION OF SUPPLY OFFERS
    -- ================================================================ */


    public static String getRetainerTypeFromUser() {
        return InputFunction.getChoicesFromUser("Please select the type of snowstop system:", new String[]{"Grid", "Tube"});
    }

    public static int getHolderCodeFromUser(){
        System.out.print("Please insert the code of the holder (between 100000 and 999999): ");
        return InputFunction.getIntFromUser(100000, 999999);
    }


    public static int getQuantityFromUser(int min, int max) {
        System.out.print("Please select the quantity (" + min + " to " + max + "): ");
        return InputFunction.getIntFromUser(min, max);
    }

    public static int getDistanceFromUser() {
        System.out.print("Select the maximum distance for the holder (100 to 1000): ");
        return InputFunction.getIntFromUser(100, 1000);
    }

    public static int getRowsFromUser() {
        System.out.print("Select the number of rows of the snowstop system (1 to 20): ");
        return InputFunction.getIntFromUser(1, 20);
    }

    public static int getRetainerCodeFromUser(){
        System.out.print("Please insert the code of the retainer (between 100000 and 999999): ");
        return InputFunction.getIntFromUser(100000, 999999);
    }

    public static int getAccessoryCodeFromUser() {
        System.out.print("Please insert the code of the accessory (between 100000 and 999999): ");
        return InputFunction.getIntFromUser(100000, 999999);
    }

    /*
     * =================================================================== --
     * METHODS FOR VISUALISING SUPPLY OFFERS
     * ================================================================
     */

    public static int getOfferCodeFromUser(){
        System.out.print("Please select an offercode to get more details: ");
        return InputFunction.getIntFromUser(1,100000);
    }

    
}
