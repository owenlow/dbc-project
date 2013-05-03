/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.controller;

import edu.rit.cs.dbc.db.DatabaseConnection;
import edu.rit.cs.dbc.model.Movie;
import edu.rit.cs.dbc.model.MovieTableModel;
import edu.rit.cs.dbc.model.Purchase;
import edu.rit.cs.dbc.view.BrowseMoviesPanel;
import edu.rit.cs.dbc.view.MemberQueuePanel;
import edu.rit.cs.dbc.view.PurchasePanel;
import edu.rit.cs.dbc.view.RecentPanel;
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
    private RecentPanel recentPanel;
    private PurchasePanel purchasePanel;
    
    public void registerBrowseMoviesPanel(BrowseMoviesPanel browseMoviesPanel) {
        this.browseMoviesPanel = browseMoviesPanel;
    }
    
    public void registerMemberQueuePanel(MemberQueuePanel memberQueuePanel) {
        this.memberQueuePanel = memberQueuePanel;
    }
    
    public void registerRecentPanel(RecentPanel recentPanel) {
        this.recentPanel = recentPanel;
    }
    
    public void registerPurchasedPanel(PurchasePanel purchasePanel) {
        this.purchasePanel = purchasePanel;
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
    
    public void loadRecentMoviesTable() {
        SwingWorker loadRecentMoviesWorker = new SwingWorker<Collection<Movie>, Movie>() {

            @Override
            protected Collection<Movie> doInBackground() throws Exception {
                return DatabaseConnection.getInstance().getRecentMovies();
            }
            
            @Override
            protected void done() {
                try {
                    Collection<Movie> result = get();
                    recentPanel.getMovieTableModel().setMovieData(result);
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
    
    public void loadPurchasedMoviesTable() {
        SwingWorker loadPurchasedMoviesWorker = new SwingWorker<Collection<Purchase>, Movie>() {

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
    
    public void addMovieToPurchased(final Movie movie) {
        SwingWorker addMovieToPurchasedWorker = new SwingWorker<Collection<Purchase>, Movie>() {

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

    public void decreaseMovieRank(Movie currentMovie) {
        swapMovieRank(currentMovie, -1);
    }

    public void increaseMovieRank(Movie currentMovie) {
        swapMovieRank(currentMovie, 1);
    }
    
    private void swapMovieRank(final Movie currentMovie, final int otherMovieOffsetIndex) {
        MovieTableModel queueMovieTableModel = memberQueuePanel.getMovieTableModel();
        final int currentMovieIndex = queueMovieTableModel.getIndexOfMovie(currentMovie);
        final Movie otherMovie = queueMovieTableModel.getMovieAt(currentMovieIndex + otherMovieOffsetIndex);
        
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
                            currentMovieIndex + otherMovieOffsetIndex, 
                            currentMovieIndex + otherMovieOffsetIndex);
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

    public void watchMovie(final Movie movieToWatch) {
        SwingWorker watchMovieWorker = new SwingWorker<Collection<Movie>, Movie>() {

            @Override
            protected Collection<Movie> doInBackground() throws Exception {
                DatabaseConnection.getInstance().watchMovie(movieToWatch);
                return DatabaseConnection.getInstance().getRecentMovies();
            }
            
            @Override
            protected void done() {
                try {
                    Collection<Movie> result = get();
                    recentPanel.getMovieTableModel().setMovieData(result);
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
    
    public void removeMoviesFromRecent(final Collection<Movie> moviesSelected) {
        SwingWorker removeMoviesFromRecentWorker = new SwingWorker<Collection<Movie>, Movie>() {

            @Override
            protected Collection<Movie> doInBackground() throws Exception {
                DatabaseConnection.getInstance().removeMoviesFromRecent(moviesSelected);
                return DatabaseConnection.getInstance().getRecentMovies();
            }

            @Override
            protected void done() {
                try {
                    Collection<Movie> result = get();
                    recentPanel.getMovieTableModel().setMovieData(result);
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
