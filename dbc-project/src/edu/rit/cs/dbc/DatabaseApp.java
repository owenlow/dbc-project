package edu.rit.cs.dbc;

import edu.rit.cs.dbc.view.LoginScreen;

/**
 * Main entry point for the application.
 */
public class DatabaseApp {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginScreen login = new LoginScreen();
                login.setVisible(true);
            }
        });
    }
    
}
