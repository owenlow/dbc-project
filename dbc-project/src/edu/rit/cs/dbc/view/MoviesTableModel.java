/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.view;

import edu.rit.cs.dbc.model.Movie;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author ptr5201
 */
public class MoviesTableModel extends AbstractTableModel {
    
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
        
        if (columnIndex >= 0 && columnIndex < MOVIE_COLUMN_NAMES.length) {
            switch (getColumnName(columnIndex)) {
                case "Title":
                    return currentMovie.getTitle();
                case "Year":
                    return currentMovie.getYear();
                case "Genre":
                    return currentMovie.getGenres();
                case "Rating":
                    return currentMovie.getRating();
                case "Score":
                    return currentMovie.getScore();
            }
        }
        
        return null;
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
