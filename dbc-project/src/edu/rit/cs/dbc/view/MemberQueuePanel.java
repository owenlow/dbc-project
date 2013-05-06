/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.view;

import edu.rit.cs.dbc.model.MovieTableModel;
import edu.rit.cs.dbc.controller.MovieTableController;
import edu.rit.cs.dbc.model.Movie;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JOptionPane;

/**
 * The screen containing a member's queue of movies
 */
public class MemberQueuePanel extends javax.swing.JPanel {

    /**
     * Creates new form MemberQueuePanel
     */
    public MemberQueuePanel() {
        initComponents();
    }
    
    /**
     * Have the mediator register this view and load the 
     * movies in the table
     * @param movieTableController the mediator to relay communication
     *                             between views
     */
    public void registerController(MovieTableController movieTableController) {
        this.movieTableController = movieTableController;
        this.movieTableController.registerMemberQueuePanel(this);
        this.movieTableController.loadQueueMoviesTable();
    }

    /**
     * Returns the table model of this screen
     * @return the table model of the member's queue of movies
     */
    public MovieTableModel getMovieTableModel() {
        return queueMoviesTableModel;
    }

    /**
     * Sets rows in the table to be highlighted
     * @param i the starting row to be highlighted
     * @param i0 the ending row to be highlighted
     */
    public void setRowSelectionInterval(int i, int i0) {
        moviesQueueTable.setRowSelectionInterval(i, i0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        upperPanel = new javax.swing.JPanel();
        moviesQueueScrollPane = new javax.swing.JScrollPane();
        moviesQueueTable = new javax.swing.JTable();
        rankButtonPanel = new javax.swing.JPanel();
        moveMovieUpButton = new javax.swing.JButton();
        moveMovieDownButton = new javax.swing.JButton();
        buttonPanel = new javax.swing.JPanel();
        purchaseButton = new javax.swing.JButton();
        removeFromQueueButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(495, 561));
        setLayout(new java.awt.BorderLayout());

        moviesQueueTable.setModel(queueMoviesTableModel);
        moviesQueueTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        moviesQueueScrollPane.setViewportView(moviesQueueTable);

        moveMovieUpButton.setText("↑");
        moveMovieUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveMovieUpButtonActionPerformed(evt);
            }
        });

        moveMovieDownButton.setText("↓");
        moveMovieDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveMovieDownButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout rankButtonPanelLayout = new javax.swing.GroupLayout(rankButtonPanel);
        rankButtonPanel.setLayout(rankButtonPanelLayout);
        rankButtonPanelLayout.setHorizontalGroup(
            rankButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankButtonPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(rankButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(moveMovieDownButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(moveMovieUpButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        rankButtonPanelLayout.setVerticalGroup(
            rankButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(moveMovieUpButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(moveMovieDownButton)
                .addContainerGap(160, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout upperPanelLayout = new javax.swing.GroupLayout(upperPanel);
        upperPanel.setLayout(upperPanelLayout);
        upperPanelLayout.setHorizontalGroup(
            upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(upperPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(moviesQueueScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rankButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        upperPanelLayout.setVerticalGroup(
            upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(upperPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(moviesQueueScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                    .addComponent(rankButtonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        add(upperPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setPreferredSize(new java.awt.Dimension(400, 58));

        purchaseButton.setText("Purchase");
        purchaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseButtonActionPerformed(evt);
            }
        });

        removeFromQueueButton.setText("Remove from Queue");
        removeFromQueueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFromQueueButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(removeFromQueueButton)
                .addGap(18, 18, 18)
                .addComponent(purchaseButton)
                .addContainerGap(110, Short.MAX_VALUE))
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(removeFromQueueButton)
                    .addComponent(purchaseButton))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        add(buttonPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Move a movie in a member's queue up by decreasing the rank of the movie
     * @param evt the action event triggered by the "up arrow" button
     */
    private void moveMovieUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveMovieUpButtonActionPerformed
        // only allow updating the rank of one movie at a time
        if (moviesQueueTable.getSelectedRowCount() == 1) {
            int selectedRow = moviesQueueTable.getSelectedRow();
            if (selectedRow > -1) {
                selectedRow = moviesQueueTable.convertRowIndexToModel(selectedRow);
                if (selectedRow > 0) {
                    Movie m = queueMoviesTableModel.getMovieAt(selectedRow);
                    movieTableController.decreaseMovieRank(m);
                }
            }
        }
    }//GEN-LAST:event_moveMovieUpButtonActionPerformed

    /**
     * Move a movie in a member's queue down by increasing the rank of the movie
     * @param evt the action event triggered by the "down arrow" button
     */
    private void moveMovieDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveMovieDownButtonActionPerformed
        // only allow updating the rank of one movie at a time
        if (moviesQueueTable.getSelectedRowCount() == 1) {
            int selectedRow = moviesQueueTable.getSelectedRow();
            if (selectedRow > -1) {
                selectedRow = moviesQueueTable.convertRowIndexToModel(selectedRow);
                if (selectedRow < (queueMoviesTableModel.getRowCount() - 1)) {
                    Movie m = queueMoviesTableModel.getMovieAt(selectedRow);
                    movieTableController.increaseMovieRank(m);
                }
            }
        }
    }//GEN-LAST:event_moveMovieDownButtonActionPerformed

    /**
     * Removes the selected movies from a member's queue
     * @param evt the action event triggered by the "Remove From Queue" button
     */
    private void removeFromQueueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeFromQueueButtonActionPerformed
        int[] selectedRows = moviesQueueTable.getSelectedRows();
        for (int rowIndex = 0; rowIndex < selectedRows.length; rowIndex++) {
            selectedRows[rowIndex] = moviesQueueTable.convertRowIndexToModel(selectedRows[rowIndex]);
        }
        Collection<Movie> moviesSelected = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < selectedRows.length; rowIndex++) {
            Movie m = queueMoviesTableModel.getMovieAt(selectedRows[rowIndex]);
            if (m != null) {
                moviesSelected.add(m);
            }
        }
        if (!moviesSelected.isEmpty()) {
            movieTableController.removeMoviesFromQueue(moviesSelected);
        }
    }//GEN-LAST:event_removeFromQueueButtonActionPerformed

    /**
     * Purchases a movie by adding it to the member's collection of
     * purchased movies, and then removes it from the member's queue
     * @param evt 
     */
    private void purchaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseButtonActionPerformed
        int selection = moviesQueueTable.getSelectedRow();
        selection = moviesQueueTable.convertRowIndexToModel(selection);
        Movie m = queueMoviesTableModel.getMovieAt(selection);
        
        Object[] options = {"Yes", "No"};
        int n = JOptionPane.showOptionDialog(this,
            "Are you sure you want to purchase the movie \"" 
                + m.getTitle() 
                + "\"?\nThe cost will be "
                + "$4.99.",
                "Confirm purchase",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[1]
        );
        if ( n == 0 ) {
            movieTableController.addMovieToPurchased(m);
            Collection<Movie> temp = new ArrayList<Movie>();
            temp.add(m);
            movieTableController.removeMoviesFromQueue(temp);
        }
    }//GEN-LAST:event_purchaseButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton moveMovieDownButton;
    private javax.swing.JButton moveMovieUpButton;
    private javax.swing.JScrollPane moviesQueueScrollPane;
    private javax.swing.JTable moviesQueueTable;
    private javax.swing.JButton purchaseButton;
    private javax.swing.JPanel rankButtonPanel;
    private javax.swing.JButton removeFromQueueButton;
    private javax.swing.JPanel upperPanel;
    // End of variables declaration//GEN-END:variables

    // the table model of the member's queue of movies
    private MovieTableModel queueMoviesTableModel = new MovieTableModel();
    
    // the mediator for sending requests on background threads
    private MovieTableController movieTableController;
}
