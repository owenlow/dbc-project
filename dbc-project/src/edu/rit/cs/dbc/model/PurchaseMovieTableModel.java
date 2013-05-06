/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.model;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.table.AbstractTableModel;

/**
 * Collection of purchased movies used by the table in the purchase screen
 * for representing information about purchases
 */
public class PurchaseMovieTableModel extends AbstractTableModel {
    
    // the columns to be displayed in the table of purchases
    public static final String[] PURCHASE_MOVIE_COLUMN_NAMES = {
        "Title", "Year", "Genre", "Rating", "Score", "Price"
    };
    
    // the collection of purchase data
    private Collection<Purchase> purchasedMovieData = new ArrayList<Purchase>();

    /**
     * Returns the number of purchased movies in the table model
     * @return the number of purchased movies in the table model
     */
    @Override
    public int getRowCount() {
        return purchasedMovieData.size();
    }

    /**
     * Returns the number of columns in the table model
     * @return the number of columns in the table model
     */
    @Override
    public int getColumnCount() {
        return PURCHASE_MOVIE_COLUMN_NAMES.length;
    }
    
    /**
     * Returns the name of the column at the specified position
     * @param col the index of the column being queried
     * @return a string containing the name of the column at
     *         the specified position
     */
    @Override
    public String getColumnName(int col) {
        return PURCHASE_MOVIE_COLUMN_NAMES[col];
    }

    /**
     * Returns the value for the cell at the specified coordinates
     * @param rowIndex the index of the row whose value is being queried
     * @param columnIndex the index of the column whose value is being queried
     * @return the value in the cell at the specified coordinates
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ArrayList<Purchase> list = new ArrayList<Purchase>(purchasedMovieData);
        Purchase currentPurchase = list.get(rowIndex);
        Object returnValue = null;
        
        if (columnIndex >= 0 && 
                columnIndex < PURCHASE_MOVIE_COLUMN_NAMES.length) {
            if(getColumnName(columnIndex).equals("Title"))
                    returnValue = currentPurchase.getMovie().getTitle();
            
            else if(getColumnName(columnIndex).equals("Year"))
                returnValue = currentPurchase.getMovie().getYear();
        
            else if(getColumnName(columnIndex).equals("Genre"))
                returnValue = currentPurchase.getMovie().getGenres();
        
            else if(getColumnName(columnIndex).equals("Rating"))
                returnValue = currentPurchase.getMovie().getRating();
        
            else if(getColumnName(columnIndex).equals("Score"))
                returnValue = currentPurchase.getMovie().getScore();
        
            else if(getColumnName(columnIndex).equals("Price"))
                returnValue = currentPurchase.getPrice();
            
            
        }
        
        return returnValue;
    }
    
    /**
     * Returns a purchased movie instance at the specified row
     * @param rowIndex the index of the row being queried
     * @return a purchased movie instance at the specified row
     */
    public Purchase getPurchaseAt(int rowIndex) {
        ArrayList<Purchase> list = new ArrayList<Purchase>(purchasedMovieData);
        Purchase purchaseAtRow = null;
        if (rowIndex >= 0 && rowIndex < purchasedMovieData.size()) {
            purchaseAtRow = list.get(rowIndex);
        }
        
        return purchaseAtRow;
    }
    
    /**
     * Returns the index of the specified purchased movie
     * @param p the purchased movie being queried
     * @return the index of the specified purchased movie
     */
    public int getIndexOfPurchase(Purchase p) {
        ArrayList<Purchase> list = new ArrayList<Purchase>(purchasedMovieData);
        return list.indexOf(p);
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
     * Updates the current collection of purchased movies and notifies
     * the table view that the purchased movie data has changed
     * @param purchasedMovieData the new collection of purchased movies to replace
     *                  the existing purchased movie data
     */
    public void setPurchasedMovieData(Collection<Purchase> purchasedMovieData) {
        this.purchasedMovieData = purchasedMovieData;
        fireTableChanged(null);
    }
    
}
