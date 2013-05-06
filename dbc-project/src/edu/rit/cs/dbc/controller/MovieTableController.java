/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.controller;

import edu.rit.cs.dbc.db.DatabaseConnection;
import edu.rit.cs.dbc.model.Movie;
import edu.rit.cs.dbc.model.MovieTableModel;
import edu.rit.cs.dbc.model.Purchase;
import edu.rit.cs.dbc.model.Recent;
import edu.rit.cs.dbc.view.BrowseMoviesPanel;
import edu.rit.cs.dbc.view.MemberQueuePanel;
import edu.rit.cs.dbc.view.PurchasePanel;
import edu.rit.cs.dbc.view.RecentPanel;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

/**
 * Mediator that communicates events between views and 
 * submits requests from the views to update the model
 */
public class MovieTableController {
    
    // the views that this mediator maintains
    private BrowseMoviesPanel browseMoviesPanel;
    private MemberQueuePanel memberQueuePanel;
    private RecentPanel recentPanel;
    private PurchasePanel purchasePanel;
    
    /**
     * Register the "Browse Movies" screen with this mediator
     * @param browseMoviesPanel the "Browse Movies" screen
     */
    public void registerBrowseMoviesPanel(BrowseMoviesPanel browseMoviesPanel) {
        this.browseMoviesPanel = browseMoviesPanel;
    }
    
    /**
     * Register the "Member Queue" screen with this mediator
     * @param memberQueuePanel the "Member Queue" screen
     */
    public void registerMemberQueuePanel(MemberQueuePanel memberQueuePanel) {
        this.memberQueuePanel = memberQueuePanel;
    }
    
    /**
     * Register the "Recently Watched" screen with this mediator
     * @param recentPanel the "Recently Watched" screen
     */
    public void registerRecentPanel(RecentPanel recentPanel) {
        this.recentPanel = recentPanel;
    }
    
    /**
     * Register the "Purchased" screen with this mediator
     * @param purchasePanel the "Purchased" screen
     */
    public void registerPurchasedPanel(PurchasePanel purchasePanel) {
        this.purchasePanel = purchasePanel;
    }
    
    /**
     * Submit a request to load the table on the "Browse Movies" screen
     */
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
    
    /**
     * Submit a request to load the table on the "Member Queue" screen
     */
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
    
    /**
     * Submit a request to load the table on the "Recently Watched" screen
     */
    public void loadRecentMoviesTable() {
        SwingWorker loadRecentMoviesWorker = new SwingWorker<Collection<Recent>, Recent>() {

            @Override
            protected Collection<Recent> doInBackground() throws Exception {
                return DatabaseConnection.getInstance().getRecentMovies();
            }
            
            @Override
            protected void done() {
                try {
                    Collection<Recent> result = get();
                    recentPanel.getRecentMovieTableModel().setRecentMovieData(result);
                } catch (InterruptedException ex) {
                    System.err.println("Getting a member's recent movies was interrupted");
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    System.err.println("Getting a member's recent movies threw an exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
                
            }
        };
        
        loadRecentMoviesWorker.execute();
    }
    
    /**
     * Submit a request to load the table on the "Purchased" screen
     */
    public void loadPurchasedMoviesTable() {
        SwingWorker loadPurchasedMoviesWorker = new SwingWorker<Collection<Purchase>, Purchase>() {

            @Override
            protected Collection<Purchase> doInBackground() throws Exception {
                return DatabaseConnection.getInstance().getPurchasedMovies();
            }
            
            @Override
            protected void done() {
                try {
                    Collection<Purchase> result = get();
                    purchasePanel.getPurchaseMovieTableModel().setPurchasedMovieData(result);
                } catch (InterruptedException ex) {
                    System.err.println("Getting a member's purchased movies was interrupted");
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    System.err.println("Getting a member's purchased movies threw an exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
                
            }
        };
        
        loadPurchasedMoviesWorker.execute();
    }
    
    /**
     * Submit a request to add movies to a member's queue and then
     * reload the table on the "Member Queue" screen
     * @param moviesSelected movies selected from the table in the
     *                       "Browse Movies" screen
     */
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
    
    /**
     * Submit a request to add a movie to a member's list of 
     * purchased movies and then reload the table on the 
     * "Purchased" screen
     * @param movie movie selected from the table in 
     *              the "Member Queue" screen
     */
    public void addMovieToPurchased(final Movie movie) {
        SwingWorker addMovieToPurchasedWorker = new SwingWorker<Collection<Purchase>, Purchase>() {

            @Override
            protected Collection<Purchase> doInBackground() throws Exception {
                DatabaseConnection.getInstance().addMovieToPurchased(movie);
                return DatabaseConnection.getInstance().getPurchasedMovies();
            }
            
            @Override
            protected void done() {
                try {
                    Collection<Purchase> result = get();
                    purchasePanel.getPurchaseMovieTableModel().setPurchasedMovieData(result);
                } catch (InterruptedException ex) {
                    System.err.println("Adding movies to a member's purchased was interrupted");
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    System.err.println("Getting a member's purchased movies threw an exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
                
            }
        };
        
        addMovieToPurchasedWorker.execute();
    }

    /**
     * Move a movie one step towards the top of 
     * a member's queue
     * @param currentMovie a movie selected in the table
     *                     of the "Member Queue" screen
     */
    public void decreaseMovieRank(Movie currentMovie) {
        swapMovieRank(currentMovie, -1);
    }

    /**
     * Move a movie one step away from the top of 
     * a member's queue
     * @param currentMovie a movie selected in the table
     *                     of the "Member Queue" screen
     */
    public void increaseMovieRank(Movie currentMovie) {
        swapMovieRank(currentMovie, 1);
    }
    
    /**
     * Submit a request to swap the ranks/positions of two
     * movies in a member's queue
     * @param currentMovie a movie selected in the table
     *                     of the "Member Queue" screen
     * @param otherMovieOffset the number of positions before
     *                         or after the current movie
     */
    private void swapMovieRank(final Movie currentMovie, final int otherMovieOffset) {
        MovieTableModel queueMovieTableModel = memberQueuePanel.getMovieTableModel();
        final int currentMovieIndex = queueMovieTableModel.getIndexOfMovie(currentMovie);
        final Movie otherMovie = queueMovieTableModel.getMovieAt(currentMovieIndex + otherMovieOffset);
        
        SwingWorker swapMovieRankWorker = new SwingWorker<Collection<Movie>, Movie>() {

            @Override
            protected Collection<Movie> doInBackground() throws Exception {
                DatabaseConnection.getInstance().swapMovieRank(currentMovie, otherMovie);
                return DatabaseConnection.getInstance().getQueueMovies();
            }
            
            @Override
            protected void done() {
                try {
                    Collection<Movie> result = get();
                    memberQueuePanel.getMovieTableModel().setMovieData(result);
                    memberQueuePanel.setRowSelectionInterval(
                            currentMovieIndex + otherMovieOffset, 
                            currentMovieIndex + otherMovieOffset);
                } catch (InterruptedException ex) {
                    System.err.println("Updating a movie's rank in the queue was interrupted");
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    System.err.println("Updating a movie's rank in the queue threw an exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
                
            }
        };
        
        swapMovieRankWorker.execute();
    }

    /**
     * Submit a request to remove the selected movies
     * from a member's queue
     * @param moviesSelected movies selected from the table in the
     *                       "Member Queue" screen
     */
    public void removeMoviesFromQueue(final Collection<Movie> moviesSelected) {
        SwingWorker removeMoviesFromQueueWorker = new SwingWorker<Collection<Movie>, Movie>() {

            @Override
            protected Collection<Movie> doInBackground() throws Exception {
                DatabaseConnection.getInstance().removeMoviesFromQueue(moviesSelected);
                return DatabaseConnection.getInstance().getQueueMovies();
            }
            
            @Override
            protected void done() {
                try {
                    Collection<Movie> result = get();
                    memberQueuePanel.getMovieTableModel().setMovieData(result);
                } catch (InterruptedException ex) {
                    System.err.println("Removing movies from a member's queue was interrupted");
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    System.err.println("Removing movies from a member's queue threw an exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
                
            }
        };
        
        removeMoviesFromQueueWorker.execute();
    }

    /**
     * Submit a request to watch a movie
     * @param movieToWatch a movie selected from either the
     *                     table in the "Purchased" screen or the
     *                     table in the "Recently Watched" screen
     */
    public void watchMovie(final Movie movieToWatch) {
        SwingWorker watchMovieWorker = new SwingWorker<Collection<Recent>, Recent>() {

            @Override
            protected Collection<Recent> doInBackground() throws Exception {
                DatabaseConnection.getInstance().watchMovie(movieToWatch);
                return DatabaseConnection.getInstance().getRecentMovies();
            }
            
            @Override
            protected void done() {
                try {
                    Collection<Recent> result = get();
                    recentPanel.getRecentMovieTableModel().setRecentMovieData(result);
                } catch (InterruptedException ex) {
                    System.err.println("Watching a purchased movie was interrupted");
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    System.err.println("Watching a purchased movie threw an exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
                
            }
        };
        
        watchMovieWorker.execute();
    }
    
    /**
     * Submit a request to remove selected movies from a member's
     * collection of recently watched movies
     * @param moviesSelected movies selected from the table in the
     *                       "Recently Watched" screen
     */
    public void removeMoviesFromRecent(final Collection<Movie> moviesSelected) {
        SwingWorker removeMoviesFromRecentWorker = new SwingWorker<Collection<Recent>, Recent>() {

            @Override
            protected Collection<Recent> doInBackground() throws Exception {
                DatabaseConnection.getInstance().removeMoviesFromRecent(moviesSelected);
                return DatabaseConnection.getInstance().getRecentMovies();
            }

            @Override
            protected void done() {
                try {
                    Collection<Recent> result = get();
                    recentPanel.getRecentMovieTableModel().setRecentMovieData(result);
                } catch (InterruptedException ex) {
                    System.err.println("Removing movies from a member's recent was interrupted");
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    System.err.println("Removing movies from a member's recent threw an exception: " + ex.getMessage());
                    ex.printStackTrace();
                }

            }
        };

        removeMoviesFromRecentWorker.execute();
    }
}
