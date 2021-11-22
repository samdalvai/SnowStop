import java.sql.*;
import java.util.List;

/**
 * Author: Samuel Dalvai
 * <p>
 * Base Class for interacting with the database, handles data insertion, deletion, update
 * and visualization
 */
public class SqlExecutor {

    protected Connection conn;
    protected DatabaseMetaData md;
    protected Statement stmt;
    protected PreparedStatement pst;
    protected ResultSet rs;
    protected ResultSetMetaData rsmd;

    public SqlExecutor() {
    }

    // connect to the database
    public void connect(String database, String username, String password) throws ClassNotFoundException, SQLException {

        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/" + database;
        this.conn = DriverManager.getConnection(url, username, password);

        System.out.println("Successfully connected to DB " + database);
    }

    // get info about connection
    public void getInfo() throws SQLException {

        this.md = conn.getMetaData();
        System.out.println("Information on the driver:");
        System.out.println("Name: " + md.getDriverName() + " - version: " + md.getDriverVersion());
        System.out.println("Connection: " + md.getConnection());
        System.out.println("Url: " + md.getURL());
        System.out.println("DB: " + md.getDatabaseProductName());
        System.out.println("Username: " + md.getUserName());
    }

    /**
     * Execute a generic query from a string
     */
    public void executeQueryFromString(String query) throws SQLException {

        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        stmt.execute(query);

        System.out.println("Correctly executed query... ");
    }

    /**
     * Execute a query from a file, no results are expected
     */
    public void executeQueryFromFile(String filePath) throws SQLException {

        String query = InputFunction.readFromFile(filePath);

        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        stmt.execute(query);

        System.out.println("Correctly executed file: " + filePath);
    }


    /**
     * Update table with generic value
     * <p>
     * In case of a type java.sql.Date pass new java.sql.Date(Calendar.getInstance().getTime().getTime()
     * as parameter to get current date
     */
    public void updateTable(String tableName, String columnName, String condition, Object newValue) throws SQLException {
        String query = "UPDATE " + tableName + " " +
                "SET " + columnName + " = ? " +
                "WHERE " + condition;

        pst = conn.prepareStatement(query);

        setStatementParameter(1, newValue);

        int rowCount = pst.executeUpdate();
        pst.close();

        if (rowCount == 0)
            System.out.println("Nothing to update");
        else
            System.out.println("Update successful... " + rowCount + " element" + ((rowCount > 1) ? "s" : "") + " updated");
    }

    /**
     * Use Arrays.asList(...,...,...,...,...) to initialize a generic List
     */
    public void insertInTable(String tableName, List<Object> parameters) throws SQLException {
        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " VALUES (");
        for (int i = 0; i < parameters.size(); i++) {
            if (i < parameters.size() - 1)
                query.append("?,");
            else
                query.append("?);");
        }

        pst = conn.prepareStatement(query.toString());

        for (int i = 0; i < parameters.size(); i++)
            setStatementParameter(i + 1, parameters.get(i));

        int rowCount = pst.executeUpdate();
        pst.close();

        if (rowCount == 0)
            System.out.println("Insertion failed");
        else
            System.out.println("Insertion successful... " + rowCount + " element" + ((rowCount > 1) ? "s" : "") +
                    " inserted");
    }


    /**
     * Delete a value or multiple values
     */
    public void deleteFromTable(String tableName, String condition) throws SQLException {

        String query = "DELETE FROM " + tableName + " " +
                "WHERE " + condition;

        pst = conn.prepareStatement(query);

        int rowCount = pst.executeUpdate();
        pst.close();

        if (rowCount == 0)
            System.out.println("Nothing to delete");
        else
            System.out.println("Deletion successful... " + rowCount + " element" + ((rowCount > 1) ? "s" : "") + " deleted");
    }

    /**
     * Set a parameter to the correct type in a prepared statement
     */
    private void setStatementParameter(int index, Object value) throws SQLException {
        switch (value.getClass().getName()) {
            case "java.lang.Integer":
                pst.setInt(index, (int) value);
                break;
            case "java.lang.Double":
                pst.setDouble(index, (double) value);
                break;
            case "java.lang.String":
                if (value.equals("null") || value.equals("NULL"))
                    pst.setNull(index, Types.NULL);
                else
                    pst.setString(index, (String) value);
                break;
            case "java.sql.Date":
                pst.setDate(index, (Date) value);
                break;
            default:
                throw new SQLException("Unrecognized type of value");
        }
    }

    /**
     * Returns true if at least 1 tuple selected with some condition is in the table
     */
    public boolean isInTable(String tableName, String condition) throws SQLException {
        String query = "SELECT * FROM " + tableName + " " +
                "WHERE " + condition;

        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);
        rs.first();

        int rowCount = rs.getRow();

        return rowCount > 0;
    }


    /* ===================================================================
    -- METHODS FOR EXTRACTION OF VALUES FROM TABLES
    -- ================================================================ */

    /**
     * Returns an array of Objects of the type of the specific columns,
     * the query must return only 1 result, otherwise an exception is
     * thrown
     * <p>
     * Insert '*' in the field columns if all the column value have to be
     * extracted, otherwise select the specific columns separated by a comma
     */
    public Object[] getRowValues(String tableName, String columns, String condition) throws SQLException {


        String query = "SELECT " + columns + " FROM " + tableName + " " +
                "WHERE " + condition;

        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);

        rsmd = rs.getMetaData();

        Object[] values = new Object[rsmd.getColumnCount()];

        if (!rs.next())
            System.out.println("No results in " + tableName + " for condition " + condition);
        else {
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                if (rsmd.getColumnTypeName(i + 1).equals("numeric") || rsmd.getColumnTypeName(i + 1).equals("int8")) {
                    if (rs.getString(i + 1).contains("."))
                        values[i] = rs.getDouble(i + 1);
                    else
                        values[i] = rs.getInt(i + 1);
                } else if (rsmd.getColumnTypeName(i + 1).equals("varchar"))
                    values[i] = rs.getString(i + 1);
                else if (rsmd.getColumnTypeName(i + 1).equals("date"))
                    values[i] = rs.getDate(i + 1);
                else
                    throw new SQLException("Unrecognized type of value");
            }
        }

        if (rs.next())
            throw new SQLException("Error, more than one tuple satisfies the condition " + condition);

        return values;
    }

    /**
     * Returns an array of Objects of the type of the specific columns,
     * the query must return only 1 result, otherwise an exception is
     * thrown
     * <p>
     * Insert '*' in the field columns if all the column value have to be
     * extracted, otherwise select the specific columns separated by a comma
     */
    public Object[] getRowValues(String tableName, String columns) throws SQLException {

        String query = "SELECT " + columns + " FROM " + tableName;

        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);

        rsmd = rs.getMetaData();

        Object[] values = new Object[rsmd.getColumnCount()];


        if (!rs.next())
            System.out.println("No results in " + tableName);
        else {
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                if (rs.getString(i + 1) == null)
                    return null;
                if (rsmd.getColumnTypeName(i + 1).equals("numeric") || rsmd.getColumnTypeName(i + 1).equals("int8")) {
                    if (rs.getString(i + 1).contains("."))
                        values[i] = rs.getDouble(i + 1);
                    else
                        values[i] = rs.getInt(i + 1);
                } else if (rsmd.getColumnTypeName(i + 1).equals("varchar"))
                    values[i] = rs.getString(i + 1);
                else if (rsmd.getColumnTypeName(i + 1).equals("date"))
                    values[i] = rs.getDate(i + 1);
                else
                    throw new SQLException("Unrecognized type of value");
            }
        }

        if (rs.next())
            throw new SQLException("Error, more than one tuple in the result");


        return values;
    }


    /* ===================================================================
    -- METHODS FOR VISUALIZATION OF TABLES
    -- ================================================================ */

    /**
     * Select columns from a table
     */
    public void showTable(String tableName, String columns, int fixedColumnWidth, int rowLimit) throws SQLException {
        String query = "SELECT " + columns + " " +
                "FROM  " + tableName;

        // make Statement scrollable, later the number of rows can be counted
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);

        displayLabels(fixedColumnWidth);
        displayValues(fixedColumnWidth, rowLimit);
    }

    /**
     * Select columns from a table under some condition
     */
    public void showTable(String tableName, String columns, String condition, int fixedColumnWidth, int rowLimit) throws SQLException {
        String query = "SELECT " + columns + " " +
                "FROM  " + tableName + " " +
                "WHERE " + condition;

        // make Statement scrollable, later the number of rows can be counted
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);

        displayLabels(fixedColumnWidth);
        displayValues(fixedColumnWidth, rowLimit);
    }

    /**
     * Prints the header names along with the header separator, based on the
     * desired uniform width for all the columns.
     */
    private void displayLabels(int fixedColumnWidth) throws SQLException {

        rsmd = rs.getMetaData();

        rs.last();
        System.out.println("***** Schema: " + rsmd.getTableName(1) + " | Total results: " + rs.getRow() + " *****");
        rs.first();

        int numberOfColumns = rsmd.getColumnCount();

        displaySeparator(fixedColumnWidth, numberOfColumns);
        displayHeaderNames(fixedColumnWidth, numberOfColumns);
        displaySeparator(fixedColumnWidth, numberOfColumns);
    }

    /**
     * Print the header names
     */
    private void displayHeaderNames(int fixedColumnWidth, int numberOfColumns) throws SQLException {
        for (int i = 1; i <= numberOfColumns; i++) {
            String columnName = rsmd.getColumnLabel(i);
            displayColumnValue(fixedColumnWidth, numberOfColumns, i, columnName);
        }
    }

    
    /**
     * Display the header name separator
     */
    private void displaySeparator(int fixedColumnWidth, int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++)
            for (int j = 0; j < fixedColumnWidth + 2; j++)
                System.out.print("-");

        System.out.print("-");
        System.out.println();
    }


    /**
     * Display values of columns, limit of results can be set with rowLimit
     */
    private void displayValues(int fixedColumnWidth, int rowLimit) throws SQLException {
        int numberOfColumns = rsmd.getColumnCount();
        int rowCounter = 0;

        rs.previous();

        while (rs.next()) {
            if (rowCounter < rowLimit) {
                for (int i = 1; i <= numberOfColumns; i++) {
                    String columnValue = rs.getString(i);

                    // correctly handle null values from DB
                    if (columnValue == null)
                        columnValue = "null";

                    displayColumnValue(fixedColumnWidth, numberOfColumns, i, columnValue);
                }
                rowCounter++;
            } else {
                System.out.print("... More? (y/n) ");
                char choice = InputFunction.getCharFromUser("yYnN");

                if (choice == 'y' || choice == 'Y') {
                    rowCounter = 0;
                    rs.previous();
                } else if (choice == 'n' || choice == 'N')
                    break;
            }

        }
        stmt.close();
        displaySeparator(fixedColumnWidth, numberOfColumns);
    }


    /**
     * Displays all the values of a row with formatting
     */
    private void displayColumnValue(int fixedColumnWidth, int numberOfColumns, int currentColumn, String columnValue) {
        if (columnValue.length() >= fixedColumnWidth) {
            columnValue = columnValue.substring(0, fixedColumnWidth - 3);
            columnValue = columnValue + "..";
        }

        System.out.print("| " + columnValue);

        for (int j = 0; (columnValue.length() + j) < fixedColumnWidth; j++) {
            System.out.print(" ");
        }

        if (currentColumn == numberOfColumns)
            System.out.println("|");
    }

    public void closeConnection() throws SQLException {
        System.out.println("Closing connection with DB: " + conn.getMetaData().getDatabaseProductName() + "...");
        conn.close();
        System.out.println("Successfully closed...");
    }

}
