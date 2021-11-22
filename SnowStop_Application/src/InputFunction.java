import java.io.*;
import java.nio.charset.Charset;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Author: Samuel Dalvai
 * 
 * Methods devoted to getting input from the user
 */
public class InputFunction {

    /**
     * Get int from user with value range
     * 
     * @param min The minimum int value to be accepted
     * @param max The maximum int value to be accepted
     * @return The int value inserted by the user in the range specified
     */
    public static int getIntFromUser(int min, int max){
        Scanner scan = new Scanner(new InputStreamReader(
            System.in, Charset.defaultCharset()));

        return getIntFromUser(min, max, scan);
    }

    /**
     * Get int from user with value range
     * 
     * @param min The minimum int value to be accepted
     * @param max The maximum int value to be accepted
     * @param scan The Scanner element to be passed
     * @return The int value inserted by the user in the range specified
     */
    private static int getIntFromUser(int min, int max, Scanner scan) {

        int input;

        try {
            while (true) {
                input = scan.nextInt();
                scan.nextLine();

                if (input >= min && input <= max) {
                    break;
                } else
                    System.err.println("Error, input out of range, please choose a value between " + min + " and " + max);
            }
        } catch (InputMismatchException e) {
            System.err.println("Error, wrong type of input, please retry");
            return getIntFromUser(min, max);
        }
        
        return input;
    }

    /**
     * Get int from user with no value limitation
     * 
     * @return The int value inserted by the user in the range specified
     */
    public static int getIntFromUser(){
        Scanner scan = new Scanner(new InputStreamReader(
            System.in, Charset.defaultCharset()));

        return getIntFromUser(scan);
    }

    /**
     * Get int from user with no value limitation
     * 
     * @param scan The Scanner element to be passed
     * @return The int value inserted by the user in the range specified
     */
    private static int getIntFromUser(Scanner scan) {

        int input;

        try {
            input = scan.nextInt();
            scan.nextLine();

        } catch (InputMismatchException e) {
            System.err.println("Error, wrong type of input, please retry");
            return getIntFromUser();
        }

        return input;
    }

    /**
     * Get int from user with value range
     * 
     * @param min The minimum int value to be accepted
     * @param max The maximum int value to be accepted
     * @return The int value inserted by the user in the range specified
     */
    public static double getDoubleFromUser(int min, int max){
        Scanner scan = new Scanner(new InputStreamReader(
            System.in, Charset.defaultCharset()));

        return getDoubleFromUser(min, max, scan);
    }

    /**
     * Get int from user with value range
     * 
     * @param min The minimum int value to be accepted
     * @return The int value inserted by the user in the range specified
     */
    public static double getDoubleFromUser(int min){
        Scanner scan = new Scanner(new InputStreamReader(
            System.in, Charset.defaultCharset()));

        return getDoubleFromUser(min, Integer.MAX_VALUE, scan);
    }

    /**
     * Get int from user with value range
     * 
     * @param min The minimum int value to be accepted
     * @param max The maximum int value to be accepted
     * @param scan The Scanner element to be passed
     * @return The int value inserted by the user in the range specified
     */
    private static double getDoubleFromUser(int min, int max, Scanner scan) {

        double input;

        try {
            while (true) {
                input = scan.nextDouble();
                scan.nextLine();

                if (input > min && input <= max) {
                    break;
                } else
                    System.err.println("Error, input out of range, please choose a value greater than " + min + " and smaller than " + max);
            }
        } catch (InputMismatchException e) {
            System.err.println("Error, wrong type of input, please retry");
            return getIntFromUser(min, max);
        }
        
        return input;
    }

    /**
     * Get string from user with length constraints
     * 
     * @param min the minimum length of the string
     * @param max the maximum length of the string
     * @return The string inserted from the user
     */
    public static String getStringFromUser(int min, int max){
        Scanner scan = new Scanner(System.in,"ISO-8859-1");

        return getStringFromUser(min, max, scan);
    }


    /**
     * Get string from user with length constraints
     * 
     * @param min the minimum length of the string
     * @param max the minimum length of the string
     * @param scan The Scanner element to be passed
     * @return The string inserted from the user
     */
    private static String getStringFromUser(int min, int max, Scanner scan) {

        String input;

        try {
            while (true) {
                input = scan.nextLine();

                if (input.length() >= min && input.length() <= max) {
                    break;
                } else
                    System.err.println("Error, string of incorrect length " + "(" + input.length() +") , please insert a string with length between " + min + " and " + max);
            }

        } catch (NoSuchElementException e) {
            System.err.println("Error, no line found, please retry");
            return getStringFromUser(min,max,scan);
        }
        
        return cleanString(input);
    }

    /**
     * Get string from user matching a regex
     * 
     * @param regex The regex that checks the string
     * @return The string inserted from the user
     */
    public static String getStringFromUser(String regex){
        Scanner scan = new Scanner(new InputStreamReader(
            System.in, Charset.defaultCharset()));

        return getStringFromUser(regex,scan);
    }


    /**
     * Get string from user with length constraints
     * 
     * @param regex The regex that checks the string
     * @param scan The Scanner element to be passed
     * @return The string inserted from the user
     */
    private static String getStringFromUser(String regex, Scanner scan) {

        String input;

        try {
            while (true) {
                input = scan.nextLine();//.strip();

                if (input.matches(regex)) {
                    break;
                } else
                    System.err.println("Error, string does not match the required form: " + regex);
            }

        } catch (NoSuchElementException e) {
            System.err.println("Error, no line found, please retry");
            return getStringFromUser(regex,scan);
        }
        
        return cleanString(input);
    }

    /**
     * Get character from user
     * 
     * @return The character inserted from the user
     */
    public static char getCharFromUser(String validChars){
        Scanner scan = new Scanner(new InputStreamReader(
            System.in, Charset.defaultCharset()));

        return getCharFromUser(scan, validChars);
    }


    /**
     * Get character from user
     * 
     * @param scan The Scanner element to be passed
     * @return The character inserted from the user
     */
    private static char getCharFromUser(Scanner scan, String validChars) {

        char input = ' ';
        boolean valid = false;


        try {
            
            while (!valid) {
                input = scan.next().charAt(0);
                scan.nextLine();
                
                for (int i = 0; i < validChars.length(); i++)
                    if (input == validChars.charAt(i)){
                        valid = true;
                        break;
                    }
                
                if (!valid){
                    System.err.print("Error, invalid character... List of valid characters: ");

                    for (int i = 0; i < validChars.length(); i++){
                        System.out.print(validChars.charAt(i));
                        if (i < validChars.length() - 1)
                            System.out.print("/");
                    }
                    System.out.println();
                }
            }            

        } catch (NoSuchElementException e) {
            System.err.println("Error, no character found, please retry");
            return getCharFromUser(scan, validChars);
        }
        
        return input;
    }

    public static String readFromFile(String filePath){
        StringBuilder text = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            
            String line;

            while ((line = reader.readLine()) != null) 
            {
                text.append(line).append("\n");
            }

        } catch (IOException e) {
            System.out.println("Problem reading " + filePath);
            e.printStackTrace();
        }

        return text.toString();
    }

    public static String getChoicesFromUser(String message, String[] choices){
        System.out.println(message);

        for (int i = 1; i <= choices.length; i++)
            System.out.println("(" + i + ")" + " " + choices[i-1]);

        int choice = InputFunction.getIntFromUser(1, choices.length);

        return choices[choice-1];
    }

    public static boolean retry (){
        System.out.print("Do you want to retry? (y/n) ");
            char choice = InputFunction.getCharFromUser("yYnN");
            return (choice == 'y' || choice == 'Y');          
    }

    private static String cleanString(String input){
        return input.replaceAll("'","").replaceAll("à", "a").replaceAll("ò", "o").replaceAll("à", "a").replaceAll("è", "e").replaceAll("ì", "i");
    }

}
