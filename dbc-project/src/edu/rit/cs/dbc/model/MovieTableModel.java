/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.model;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author ptr5201
 */
public class MovieTableModel extends AbstractTableModel {
    
    public static final String[] MOVIE_COLUMN_NAMES = {
        "Title", "Year", "Genre", "Rating", "Score"
    };
    
    private Collection<Movie> movieData = new ArrayList<>();

    @Override
    public int getRowCount() {
        return movieData.size();
    }

    @Override
    public int getColumnCount() {
        return MOVIE_COLUMN_NAMES.length;
    }
    
    @Override
    public String getColumnName(int col) {
        return MOVIE_COLUMN_NAMES[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ArrayList<Movie> list = new ArrayList<>(movieData);
        Movie currentMovie = list.get(rowIndex);
        Object movieReturnValue = null;
        
        if (columnIndex >= 0 && 
                columnIndex < MOVIE_COLUMN_NAMES.length) {
            switch (getColumnName(columnIndex)) {
                case "Title":
                    movieReturnValue = currentMovie.getTitle();
                    break;
                case "Year":
                    movieReturnValue = currentMovie.getYear();
                    break;
                case "Genre":
                    movieReturnValue = currentMovie.getGenres();
                    break;
                case "Rating":
                    movieReturnValue = currentMovie.getRating();
                    break;
                case "Score":
                    movieReturnValue = currentMovie.getScore();
                    break;
            }
        }
        
        return movieReturnValue;
    }
    
    public Movie getMovieAt(int rowIndex) {
        ArrayList<Movie> list = new ArrayList<>(movieData);
        Movie movieAtRow = null;
        if (rowIndex >= 0 && rowIndex < movieData.size()) {
            movieAtRow = list.get(rowIndex);
        }
        
        return movieAtRow;
    }
    
    public int getIndexOfMovie(Movie m) {
        ArrayList<Movie> list = new ArrayList<>(movieData);
        return list.indexOf(m);
    }
    
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
    public void setMovieData(Collection<Movie> movieData) {
        this.movieData = movieData;
        fireTableChanged(null);
    }
    
}
