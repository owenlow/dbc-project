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
public class RecentMovieTableModel extends AbstractTableModel {

    public static final String[] RECENT_MOVIE_COLUMN_NAMES = {
        "Title", "Year", "Genre", "Rating", "Score", "Watch Count"
    };
    private Collection<Recent> recentMovieData = new ArrayList<>();

    @Override
    public int getRowCount() {
        return recentMovieData.size();
    }

    @Override
    public int getColumnCount() {
        return RECENT_MOVIE_COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int col) {
        return RECENT_MOVIE_COLUMN_NAMES[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ArrayList<Recent> list = new ArrayList<>(recentMovieData);
        Recent currentRecentMovie = list.get(rowIndex);
        Object returnValue = null;

        if (columnIndex >= 0
                && columnIndex < RECENT_MOVIE_COLUMN_NAMES.length) {
            switch (getColumnName(columnIndex)) {
                case "Title":
                    returnValue = currentRecentMovie.getMovie().getTitle();
                    break;
                case "Year":
                    returnValue = currentRecentMovie.getMovie().getYear();
                    break;
                case "Genre":
                    returnValue = currentRecentMovie.getMovie().getGenres();
                    break;
                case "Rating":
                    returnValue = currentRecentMovie.getMovie().getRating();
                    break;
                case "Score":
                    returnValue = currentRecentMovie.getMovie().getScore();
                    break;
                case "Watch Count":
                    returnValue = currentRecentMovie.getWatchcount();
                    break;
            }
        }

        return returnValue;
    }

    public Recent getRecentMovieAt(int rowIndex) {
        ArrayList<Recent> list = new ArrayList<>(recentMovieData);
        Recent recentMovieAtRow = null;
        if (rowIndex >= 0 && rowIndex < recentMovieData.size()) {
            recentMovieAtRow = list.get(rowIndex);
        }

        return recentMovieAtRow;
    }

    public int getIndexOfRecent(Recent r) {
        ArrayList<Recent> list = new ArrayList<>(recentMovieData);
        return list.indexOf(r);
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public void setRecentMovieData(Collection<Recent> recentMovieData) {
        this.recentMovieData = recentMovieData;
        fireTableChanged(null);
    }
}
