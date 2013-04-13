/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.controller;

import edu.rit.cs.dbc.db.DatabaseConnection;
import edu.rit.cs.dbc.model.Movie;
import edu.rit.cs.dbc.view.BrowseMoviesScreen;
import edu.rit.cs.dbc.view.MovieTableModel;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author ptr5201
 */
public class MovieTableController {
    
    private BrowseMoviesScreen browseMoviesScreen;
    
    public MovieTableController(BrowseMoviesScreen browseMoviesScreen) {
        this.browseMoviesScreen = browseMoviesScreen;
    }
    
    public void loadMoviesTable() {
        SwingWorker loadMoviesWorker = new SwingWorker<Collection<Movie>, Movie>() {

            @Override
            protected Collection<Movie> doInBackground() {
                return DatabaseConnection.getInstance().getAllMovies();
            }
            
            @Override
            protected void done() {
                try {
                    Collection<Movie> result = get();
                    MovieTableModel movieTableModel = browseMoviesScreen.getMovieTableModel();
                    movieTableModel.setMovieData(result);
                } catch (InterruptedException ex) {
                    System.err.println("Getting all movies thread interrupted");
                } catch (ExecutionException ex) {
                    System.err.println("Getting all movies threw an exception: " + ex.getMessage());
                }
                
            }
            
        };
        
        loadMoviesWorker.execute();
    }
}
