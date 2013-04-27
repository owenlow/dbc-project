/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.controller;

import edu.rit.cs.dbc.db.DatabaseConnection;
import edu.rit.cs.dbc.model.Movie;
import edu.rit.cs.dbc.view.BrowseMoviesPanel;
import edu.rit.cs.dbc.view.MemberQueuePanel;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

/**
 *
 * @author ptr5201
 */
public class MovieTableController {
    
    private BrowseMoviesPanel browseMoviesPanel;
    private MemberQueuePanel memberQueuePanel;
    
    public void registerBrowseMoviesPanel(BrowseMoviesPanel browseMoviesPanel) {
        this.browseMoviesPanel = browseMoviesPanel;
    }
    
    public void registerMemberQueuePanel(MemberQueuePanel memberQueuePanel) {
        this.memberQueuePanel = memberQueuePanel;
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
                    browseMoviesPanel.getMovieTableModel().setMovieData(result);
                } catch (InterruptedException ex) {
                    System.err.println("Getting all movies was interrupted");
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
                    memberQueuePanel.getMovieTableModel().setMovieData(result);
                } catch (InterruptedException ex) {
                    System.err.println("Getting a member's queue of movies was interrupted");
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    System.err.println("Getting a member's queue of movies threw an exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
                
            }
        };
        
        loadQueueMoviesWorker.execute();
    }
    
    public void addMoviesToQueue(final Collection<Movie> moviesSelected) {
        SwingWorker addMoviesToQueueWorker = new SwingWorker<Collection<Movie>, Movie>() {

            @Override
            protected Collection<Movie> doInBackground() throws Exception {
                DatabaseConnection.getInstance().addMoviesToQueue(moviesSelected);
                return DatabaseConnection.getInstance().getQueueMovies();
            }
            
            @Override
            protected void done() {
                try {
                    Collection<Movie> result = get();
                    memberQueuePanel.getMovieTableModel().setMovieData(result);
                } catch (InterruptedException ex) {
                    System.err.println("Adding movies to a member's queue was interrupted");
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    System.err.println("Getting a member's queue of movies threw an exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
                
            }
        };
        
        addMoviesToQueueWorker.execute();
    }
    
    /*public void loadRecentMoviesTable() {
        SwingWorker loadRecentMoviesWorker = new SwingWorker<Collection<Movie>, Movie>() {

            @Override
            protected Collection<Movie> doInBackground() throws Exception {
                return DatabaseConnection.getInstance().getRecentMovies();
            }
            
            @Override
            protected void done() {
                try {
                    Collection<Movie> result = get();
                    moviesTableModel.setMovieData(result);
                } catch (InterruptedException ex) {
                    System.err.println("Getting a member's collection of recent movies was interrupted");
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    System.err.println("Getting a member's collection of recent movies threw an exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
                
            }
        };
        
        loadRecentMoviesWorker.execute();
    }*/
}
