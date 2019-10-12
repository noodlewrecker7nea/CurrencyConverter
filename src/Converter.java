/*This program is to convert between currencies. The conversion rates can be edited in a text file*/

// todo have a command all one line eg, CONVERT GBP EURO 3.20 - just split on space and grab each chunk

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Scanner;

public class Converter {

    private HashMap<String, Double> bread = new HashMap<String, Double>();
    private String file;
    private void updateRatesFromFile(String file) { // pulls rates from file
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) { // for each line
                String[] split = line.split(",");
                bread.put(split[0], Double.parseDouble(split[1])); // 0 is name, 1 is rate
            }
        } catch (Exception e) {
            System.out.println("Error reading file");
        }
    }

    public Converter(String file) {
        this.file = file;
        updateRatesFromFile(this.file);
    }


    public double convertTo(double amt, double startRate, double endRate) { // converts the currency
        return Math.round(((amt / startRate) * endRate) * 100d) / 100d; // to force 2d.p.
    }


    public void beginConvertLoop() { // will continue in a loop of asking to convert a double0
        updateRatesFromFile(this.file);
        Scanner sc = new Scanner(System.in);
        String startCurrency; // contains the key for the rates hash
        do {
            System.out.print("Enter currency to convert from. For a list type LIST :> ");
            startCurrency = sc.nextLine().toUpperCase().trim(); // user choice of currency
            if (startCurrency.toUpperCase().equals("LIST")) { // if the user asked for a list
                System.out.println(bread.keySet()); // prints possible currencies
            }
        } while (!bread.containsKey(startCurrency)); // checks the currency exists, wont if they entered list
        double startRate = bread.get(startCurrency); // gets the rate of their chosen currency

        // is same as above but for end
        String endCurrency;
        do {
            System.out.print("Enter currency to convert to. For a list type LIST :>");
            endCurrency = sc.nextLine().toUpperCase().trim();
            if (startCurrency.toUpperCase().equals("LIST")) {
                System.out.println(bread.keySet());
            }
        } while (!bread.containsKey(endCurrency));
        double endRate = bread.get(endCurrency);

        System.out.println("Conversion rate between " + startCurrency + " --> " + endCurrency + " is " + (((double) 1 / startRate) * endRate)); // reminder of the rates chosen
        while (true) { // repeats until quit
            System.out.print("\nEnter amount to convert or QUIT to quit :> ");
            String in = sc.nextLine().trim();
            if (in.toUpperCase().equals("QUIT")) return; // exit clause
            double amt = Double.parseDouble(in); // converts to be used
            System.out.println(amt + " " + startCurrency + " in " + endCurrency + " is " + convertTo(amt, startRate, endRate)); // converts and prints
        }

    }

    public void changeRates() { // used to view and change rates
        updateRatesFromFile(this.file); // updates file rates
        for (String key : bread.keySet()) { // prints current rates
            System.out.println(key + " : " + bread.get(key));
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("If you want to change a rate enter its name, else type QUIT :>");
        String input = sc.nextLine().toUpperCase().trim();
        if (input.equals("QUIT")) return;
        if (!bread.containsKey(input)) {
            System.out.println("Currency not found.");
            changeRates();
            return;
        }
        String rateKey = input;
        System.out.print("What is the rate of " + rateKey + " from GBP to " + rateKey + "? :>");
        double value = sc.nextDouble();
        bread.replace(rateKey, value);
    }

    public static void main(String args[]) {
        Converter converter = new Converter("exchangeRates.txt"); // uses rates found in file
        Scanner sc = new Scanner(System.in);
        do { // repeats until quit
            System.out.print("\nEnter command. For help type HELP. Or type QUIT to quit :> ");
            String input = sc.nextLine().toUpperCase().trim();
            switch (input) {
                case "QUIT":
                    return;
                case "CONVERT": // all conversion done here
                    converter.beginConvertLoop();
                    break;
                case "RATES":
                    converter.changeRates();
                    break;
                case "HELP":
                    System.out.println("\nEnter CONVERT to convert between currencies.\nEnter RATES to view and change rates.\nEnter QUIT to quit.\nEnter HELP to view this screen");
                    break;
                default:
                    System.out.println("Command not found");
                    break;
            }

        } while (true);

    }
}


