/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.controller;

import edu.rit.cs.dbc.db.DatabaseConnection;
import edu.rit.cs.dbc.model.Movie;
import edu.rit.cs.dbc.view.MoviesTableModel;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

/**
 *
 * @author ptr5201
 */
public class MovieTableController {
    
    private MoviesTableModel moviesTableModel;
    
    public MovieTableController(MoviesTableModel moviesTableModel) {
        this.moviesTableModel = moviesTableModel;
    }
    
    public void loadBrowseMoviesTable() {
        SwingWorker loadBrowseMoviesWorker = new SwingWorker<Collection<Movie>, Movie>() {

            @Override
            protected Collection<Movie> doInBackground() {
                return DatabaseConnection.getInstance().getAllMovies();
            }
            
            @Override
            protected void done() {
                try {
                    Collection<Movie> result = get();
                    moviesTableModel.setMovieData(result);
                } catch (InterruptedException ex) {
                    System.err.println("Getting all movies thread interrupted");
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    System.err.println("Getting all movies threw an exception");
                    ex.printStackTrace();
                }
                
            }
            
        };
        
        loadBrowseMoviesWorker.execute();
    }
    
    public void loadQueueMoviesTable() {
        SwingWorker loadQueueMoviesWorker = new SwingWorker<Collection<Movie>, Movie>() {

            @Override
            protected Collection<Movie> doInBackground() throws Exception {
                return DatabaseConnection.getInstance().getQueueMovies();
            }
            
            @Override
            protected void done() {
                try {
                    Collection<Movie> result = get();
                    moviesTableModel.setMovieData(result);
                } catch (InterruptedException ex) {
                    System.err.println("Getting a member's queue of movies thread interrupted");
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    System.err.println("Getting a member's queue of movies threw an exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
                
            }
        };
        
        loadQueueMoviesWorker.execute();
    }
}
