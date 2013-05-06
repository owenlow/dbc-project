/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.model;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.table.AbstractTableModel;

/**
 * Collection of movies used by tables for representing
 * movie information
 */
public class MovieTableModel extends AbstractTableModel {
    
    // the columns to be displayed in the table of movies
    public static final String[] MOVIE_COLUMN_NAMES = {
        "Title", "Year", "Genre", "Rating", "Score"
    };
    
    // the collection of movie data
    private Collection<Movie> movieData = new ArrayList<Movie>();

    /**
     * Returns the number of movies in the table model
     * @return the number of movies in the table model
     */
    @Override
    public int getRowCount() {
        return movieData.size();
    }

    /**
     * Returns the number of columns in the table model
     * @return the number of columns in the table model
     */
    @Override
    public int getColumnCount() {
        return MOVIE_COLUMN_NAMES.length;
    }
    
    /**
     * Returns the name of the column at the specified position
     * @param col the index of the column being queried
     * @return a string containing the name of the column at
     *         the specified position
     */
    @Override
    public String getColumnName(int col) {
        return MOVIE_COLUMN_NAMES[col];
    }

    /**
     * Returns the value for the cell at the specified coordinates
     * @param rowIndex the index of the row whose value is being queried
     * @param columnIndex the index of the column whose value is being queried
     * @return the value in the cell at the specified coordinates
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ArrayList<Movie> list = new ArrayList<Movie>(movieData);
        Movie currentMovie = list.get(rowIndex);
        Object movieReturnValue = null;
        
        if (columnIndex >= 0 && 
                columnIndex < MOVIE_COLUMN_NAMES.length) {
            if(getColumnName(columnIndex).equals("Title"))
                    movieReturnValue = currentMovie.getTitle();
            
            else if(getColumnName(columnIndex).equals("Year"))
                movieReturnValue = currentMovie.getYear();
        
            else if(getColumnName(columnIndex).equals("Genre"))
                movieReturnValue = currentMovie.getGenres();
        
            else if(getColumnName(columnIndex).equals("Rating"))
                movieReturnValue = currentMovie.getRating();
        
            else if(getColumnName(columnIndex).equals("Score"))
                movieReturnValue = currentMovie.getScore();
            
            
        }
        
        return movieReturnValue;
    }
    
    /**
     * Returns a movie instance at the specified row
     * @param rowIndex the index of the row being queried
     * @return a movie instance at the specified row
     */
    public Movie getMovieAt(int rowIndex) {
        ArrayList<Movie> list = new ArrayList<Movie>(movieData);
        Movie movieAtRow = null;
        if (rowIndex >= 0 && rowIndex < movieData.size()) {
            movieAtRow = list.get(rowIndex);
        }
        
        return movieAtRow;
    }
    
    /**
     * Returns the index of the specified movie
     * @param m the movie being queried
     * @return the index of the specified movie
     */
    public int getIndexOfMovie(Movie m) {
        ArrayList<Movie> list = new ArrayList<Movie>(movieData);
        return list.indexOf(m);
    }
    
    /**
     * Returns the class of the specified column
     * @param c the index of the column being queried
     * @return the class of the specified column
     */
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
    /**
     * Updates the current collection of movies and notifies
     * the table view that the movie data has changed
     * @param movieData the new collection of movies to replace
     *                  the existing movie data
     */
    public void setMovieData(Collection<Movie> movieData) {
        this.movieData = movieData;
        fireTableChanged(null);
    }
    
}
