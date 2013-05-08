/*
 * Contributors: Owen Royall-Kahin, Philip Rodriguez, Patrick Duffy
 */
package edu.rit.cs.dbc.model;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.table.AbstractTableModel;

/**
 * Collection of recent movies used by the table in the recent screen
 * for representing information about recently watched movies
 */
public class RecentMovieTableModel extends AbstractTableModel {

    // the columns to be displayed in the table of recently watched movies
    public static final String[] RECENT_MOVIE_COLUMN_NAMES = {
        "Title", "Year", "Genre", "Rating", "Score", "Watch Count"
    };
    
    // the collection of recently watched movie data
    private Collection<Recent> recentMovieData = new ArrayList<Recent>();

    /**
     * Returns the number of recently watched movies in the table model
     * @return the number of recently watched movies in the table model
     */
    @Override
    public int getRowCount() {
        return recentMovieData.size();
    }

    /**
     * Returns the number of columns in the table model
     * @return the number of columns in the table model
     */
    @Override
    public int getColumnCount() {
        return RECENT_MOVIE_COLUMN_NAMES.length;
    }

    /**
     * Returns the name of the column at the specified position
     * @param col the index of the column being queried
     * @return a string containing the name of the column at
     *         the specified position
     */
    @Override
    public String getColumnName(int col) {
        return RECENT_MOVIE_COLUMN_NAMES[col];
    }

    /**
     * Returns the value for the cell at the specified coordinates
     * @param rowIndex the index of the row whose value is being queried
     * @param columnIndex the index of the column whose value is being queried
     * @return the value in the cell at the specified coordinates
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ArrayList<Recent> list = new ArrayList<Recent>(recentMovieData);
        Recent currentRecentMovie = list.get(rowIndex);
        Object returnValue = null;

        if (columnIndex >= 0
                && columnIndex < RECENT_MOVIE_COLUMN_NAMES.length) {
            
            if(getColumnName(columnIndex).equals("Title"))
                returnValue = currentRecentMovie.getMovie().getTitle();
                
            else if(getColumnName(columnIndex).equals("Year"))
                returnValue = currentRecentMovie.getMovie().getYear();
        
            else if(getColumnName(columnIndex).equals("Genre"))
                returnValue = currentRecentMovie.getMovie().getGenres();
                
            else if(getColumnName(columnIndex).equals("Rating"))
                returnValue = currentRecentMovie.getMovie().getRating();
        
            else if(getColumnName(columnIndex).equals("Score"))
                returnValue = currentRecentMovie.getMovie().getScore();
        
            else if(getColumnName(columnIndex).equals("Watch Count"))
                returnValue = currentRecentMovie.getWatchcount();
            
            
        }

        return returnValue;
    }

    /**
     * Returns a recently watched movie instance at the specified row
     * @param rowIndex the index of the row being queried
     * @return a recently watched movie instance at the specified row
     */
    public Recent getRecentMovieAt(int rowIndex) {
        ArrayList<Recent> list = new ArrayList<Recent>(recentMovieData);
        Recent recentMovieAtRow = null;
        if (rowIndex >= 0 && rowIndex < recentMovieData.size()) {
            recentMovieAtRow = list.get(rowIndex);
        }

        return recentMovieAtRow;
    }

    /**
     * Returns the index of the specified recently watched movie
     * @param r the recently watched movie being queried
     * @return the index of the specified recently watched movie
     */
    public int getIndexOfRecent(Recent r) {
        ArrayList<Recent> list = new ArrayList<Recent>(recentMovieData);
        return list.indexOf(r);
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
     * Updates the current collection of recently watched movies and notifies
     * the table view that the recently watched movie data has changed
     * @param recentMovieData the new collection of recently watched movies
     *                           to replace the existing recently watched 
     *                           movie data
     */
    public void setRecentMovieData(Collection<Recent> recentMovieData) {
        this.recentMovieData = recentMovieData;
        fireTableChanged(null);
    }
}
