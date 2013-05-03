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
public class PurchaseMovieTableModel extends AbstractTableModel {
    
    public static final String[] PURCHASE_MOVIE_COLUMN_NAMES = {
        "Title", "Year", "Genre", "Rating", "Score", "Price"
    };
    
    private Collection<Purchase> purchasedMovieData = new ArrayList<>();

    @Override
    public int getRowCount() {
        return purchasedMovieData.size();
    }

    @Override
    public int getColumnCount() {
        return PURCHASE_MOVIE_COLUMN_NAMES.length;
    }
    
    @Override
    public String getColumnName(int col) {
        return PURCHASE_MOVIE_COLUMN_NAMES[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ArrayList<Purchase> list = new ArrayList<>(purchasedMovieData);
        Purchase currentPurchase = list.get(rowIndex);
        Object returnValue = null;
        
        if (columnIndex >= 0 && 
                columnIndex < PURCHASE_MOVIE_COLUMN_NAMES.length) {
            switch (getColumnName(columnIndex)) {
                case "Title":
                    returnValue = currentPurchase.getMovie().getTitle();
                    break;
                case "Year":
                    returnValue = currentPurchase.getMovie().getYear();
                    break;
                case "Genre":
                    returnValue = currentPurchase.getMovie().getGenres();
                    break;
                case "Rating":
                    returnValue = currentPurchase.getMovie().getRating();
                    break;
                case "Score":
                    returnValue = currentPurchase.getMovie().getScore();
                    break;
                case "Price":
                    returnValue = currentPurchase.getPrice();
                    break;
            }
        }
        
        return returnValue;
    }
    
    public Purchase getPurchaseAt(int rowIndex) {
        ArrayList<Purchase> list = new ArrayList<>(purchasedMovieData);
        Purchase purchaseAtRow = null;
        if (rowIndex >= 0 && rowIndex < purchasedMovieData.size()) {
            purchaseAtRow = list.get(rowIndex);
        }
        
        return purchaseAtRow;
    }
    
    public int getIndexOfPurchase(Purchase p) {
        ArrayList<Purchase> list = new ArrayList<>(purchasedMovieData);
        return list.indexOf(p);
    }
    
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
    public void setPurchasedMovieData(Collection<Purchase> purchasedMovieData) {
        this.purchasedMovieData = purchasedMovieData;
        fireTableChanged(null);
    }
    
}
