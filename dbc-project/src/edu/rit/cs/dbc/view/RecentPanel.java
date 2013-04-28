/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.view;

import edu.rit.cs.dbc.controller.MovieTableController;
import edu.rit.cs.dbc.model.MovieTableModel;

/**
 *
 * @author owen
 */
public class RecentPanel extends javax.swing.JPanel {

    /**
     * Creates new form RecentPanel
     */
    public RecentPanel() {
        initComponents();
    }
    
    public void registerController(MovieTableController movieTableController) {
        this.movieTableController = movieTableController;
        this.movieTableController.registerRecentPanel(this);
        this.movieTableController.loadRecentMoviesTable();
    }

    public MovieTableModel getMovieTableModel() {
        return recentMoviesTableModel;
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
        recentScrollPane = new javax.swing.JScrollPane();
        recentTable = new javax.swing.JTable();
        buttonPanel = new javax.swing.JPanel();
        clearButton = new javax.swing.JButton();
        rewatchButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(495, 561));
        setLayout(new java.awt.BorderLayout());

        recentTable.setModel(recentMoviesTableModel);
        recentTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        recentScrollPane.setViewportView(recentTable);

        javax.swing.GroupLayout upperPanelLayout = new javax.swing.GroupLayout(upperPanel);
        upperPanel.setLayout(upperPanelLayout);
        upperPanelLayout.setHorizontalGroup(
            upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(upperPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(recentScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                .addContainerGap())
        );
        upperPanelLayout.setVerticalGroup(
            upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(upperPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(recentScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(upperPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setPreferredSize(new java.awt.Dimension(400, 58));

        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        rewatchButton.setText("Rewatch");
        rewatchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rewatchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clearButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rewatchButton)
                .addContainerGap(217, Short.MAX_VALUE))
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearButton)
                    .addComponent(rewatchButton))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        add(buttonPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearButtonActionPerformed

    private void rewatchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rewatchButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rewatchButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton clearButton;
    private javax.swing.JScrollPane recentScrollPane;
    private javax.swing.JTable recentTable;
    private javax.swing.JButton rewatchButton;
    private javax.swing.JPanel upperPanel;
    // End of variables declaration//GEN-END:variables
    
    private MovieTableModel recentMoviesTableModel = new MovieTableModel();
    private MovieTableController movieTableController;
}
