package edu.rit.cs.dbc;

import edu.rit.cs.dbc.db.DatabaseConnection;

/**
 *
 * @author ptr5201
 */
public class DatabaseApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        if (args.length == 2) {
            db.connect(args[0], args[1]);
            System.out.println("Connection successful");
            db.close();
        }
    }
}
