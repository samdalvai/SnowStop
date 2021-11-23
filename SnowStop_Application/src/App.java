/**
 * Author: Samuel Dalvai
 * 
 * **** SNOWSTOP DB APPLICATION ****
 * 
 * This application interacts with a database of snowstop products and lets the user
 * interact with it by inserting and retrieving data
 * 
 */
public class App {
    public static void main(String[] args) throws Exception {

        String db = "snowstop";
        String username = "postgres";
        String password = "postgres88";

        if (args.length > 0){
            if (args.length != 3)
                throw new Exception("Wrong number of parameters, please provide db name, username and password");
            else{
                db = args[0];
                username = args[1];
                password = args[2];
            }
        }

        SnowStopTerminal terminal = new SnowStopTerminal(db, username, password);
        terminal.run();
    }
}
