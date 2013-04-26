package edu.rit.cs.dbc;

import edu.rit.cs.dbc.view.LoginScreen;
import edu.rit.cs.dbc.view.NewUserCreationFrame;
import javax.swing.JFrame;

/**
 *
 * @author ptr5201
 */
public class DatabaseApp {

    static LoginScreen login;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                login = new LoginScreen();
                login.setVisible(true);
            }
        });
    }
    
    public static void createAndShowNewUserFrame() {
        NewUserCreationFrame frame = new NewUserCreationFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void displayLogin() {
        login.setVisible(true);
    }
    
}
