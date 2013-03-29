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
        System.out.println("main starting");
        String user = "p48501a";
        String pass = "uixohphieshiechu";
        
        DatabaseConnection db = new DatabaseConnection();
        
        db.connect(user, pass);
        System.out.println("Connection successful");
        db.close();
        
    }
}
