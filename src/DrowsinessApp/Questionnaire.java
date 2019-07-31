/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DrowsinessApp;

import java.awt.Color;
import java.awt.MouseInfo;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 *
 * @author h4wk
 */
public class Questionnaire extends javax.swing.JFrame {
    
    private Point cursorLocations;
    /**
     * Creates new form Questionnaire
     */
    public Questionnaire() {
        initComponents();
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);       

        DocumentListener dl = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFieldState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFieldState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFieldState();
            }

            protected void updateFieldState() {
                try {
                    jSlider1.setValue(Integer.parseInt(jTextField1.getText()));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

        };

        jTextField1.getDocument().addDocumentListener(dl);

        jSlider1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                saveCursorLocation("Questionaire_Slider_value_" + String.valueOf(jSlider1.getValue()));
                jTextField1.setText(String.valueOf(jSlider1.getValue()));
            }
        });
        jTextArea1.setLineWrap(true);
        jTextArea1.setOpaque(false);
        jTextArea1.setBackground(new Color(255,255,255,0));
        jTextArea2.setLineWrap(true);
        jTextArea2.setOpaque(false);
        jTextArea2.setBackground(new Color(255,255,255,0));
        jTextArea3.setLineWrap(true);
        jTextArea3.setOpaque(false);
        jTextArea3.setBackground(new Color(255,255,255,0));
        jTextArea4.setLineWrap(true);
        jTextArea4.setOpaque(false);
        jTextArea4.setBackground(new Color(255,255,255,0));
        jTextArea5.setLineWrap(true);
        jTextArea5.setOpaque(false);
        jTextArea5.setBackground(new Color(255,255,255,0));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        confirmButton = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea5 = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Sleepy Level");

        jSlider1.setMaximum(9);
        jSlider1.setMinimum(1);
        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setSnapToTicks(true);
        jSlider1.setValue(5);

        confirmButton.setText("Confirm");
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });

        jTextField1.setText("5");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("1");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("5");

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("9");

        jScrollPane1.setBorder(null);
        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setFocusable(false);

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(240, 240, 240));
        jTextArea1.setColumns(20);
        jTextArea1.setRows(1);
        jTextArea1.setText("Very alert");
        jTextArea1.setAutoscrolls(false);
        jTextArea1.setBorder(null);
        jTextArea1.setFocusable(false);
        jScrollPane1.setViewportView(jTextArea1);

        jScrollPane2.setBorder(null);

        jTextArea2.setEditable(false);
        jTextArea2.setBackground(new java.awt.Color(240, 240, 240));
        jTextArea2.setColumns(20);
        jTextArea2.setRows(3);
        jTextArea2.setText("Alert\nnormal\nlevel");
        jTextArea2.setAutoscrolls(false);
        jTextArea2.setBorder(null);
        jTextArea2.setFocusable(false);
        jScrollPane2.setViewportView(jTextArea2);

        jScrollPane3.setBorder(null);
        jScrollPane3.setForeground(new java.awt.Color(240, 240, 240));

        jTextArea3.setEditable(false);
        jTextArea3.setBackground(new java.awt.Color(240, 240, 240));
        jTextArea3.setColumns(20);
        jTextArea3.setRows(4);
        jTextArea3.setText("Neither\nalert\nnor\nsleepy");
        jTextArea3.setAutoscrolls(false);
        jTextArea3.setBorder(null);
        jTextArea3.setFocusable(false);
        jScrollPane3.setViewportView(jTextArea3);

        jScrollPane4.setBorder(null);

        jTextArea4.setEditable(false);
        jTextArea4.setBackground(new java.awt.Color(240, 240, 240));
        jTextArea4.setColumns(20);
        jTextArea4.setRows(5);
        jTextArea4.setText("Very sleepy,\nfighting\nsleep");
        jTextArea4.setAutoscrolls(false);
        jTextArea4.setBorder(null);
        jTextArea4.setFocusable(false);
        jScrollPane4.setViewportView(jTextArea4);

        jScrollPane5.setBorder(null);

        jTextArea5.setEditable(false);
        jTextArea5.setBackground(new java.awt.Color(240, 240, 240));
        jTextArea5.setColumns(20);
        jTextArea5.setRows(4);
        jTextArea5.setText("Sleepy but\nno effort\nto keep\nawake");
        jTextArea5.setAutoscrolls(false);
        jTextArea5.setBorder(null);
        jTextArea5.setFocusable(false);
        jScrollPane5.setViewportView(jTextArea5);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("7");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(198, 198, 198))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(104, 104, 104)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(75, 75, 75)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(75, 75, 75)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(75, 75, 75)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(75, 75, 75)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(47, 47, 47)))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel9)
                    .addComponent(jLabel13)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonActionPerformed
        // TODO add your handling code here:
        saveCursorLocation("Submit_Questionaire");
        saveAnswer();
        this.setVisible(false);
    }//GEN-LAST:event_confirmButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Questionnaire.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Questionnaire.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Questionnaire.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Questionnaire.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

    }
    
    public void saveCursorLocation(String event) {
        java.awt.Point p = MouseInfo.getPointerInfo().getLocation();
        String tmp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new Date());
        cursorLocations = new Point((int) p.getX(), (int) p.getY(), tmp, event);
        PrintWriter pw;
        StringBuilder sb = new StringBuilder();
        String startTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new Date());
        String fileName = startTime.substring(0, 4) + startTime.substring(5, 7) + startTime.substring(8, 10) + "_" + GUIClass.staffID;
        try {
            File f = new File(System.getProperty("user.dir") + "/" + fileName + "_cursor.csv");
            if (!f.exists() || f.isDirectory()) {
                pw = new PrintWriter(new FileWriter(fileName + "_cursor.csv"));
                sb.append("Timestamp");
                sb.append(',');
                sb.append("X");
                sb.append(',');
                sb.append("Y");
                sb.append(',');
                sb.append("Event");
                sb.append('\n');
            } else {
                pw = new PrintWriter(new FileWriter(System.getProperty("user.dir") + "/" + fileName + "_cursor.csv", true));
            }
                sb.append(cursorLocations.getTimePoint());
                sb.append(',');
                sb.append(cursorLocations.getX());
                sb.append(',');
                sb.append(cursorLocations.getY());
                sb.append(',');
                sb.append(cursorLocations.getEvent());
                sb.append('\n');
            
            pw.write(sb.toString());
            pw.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
      
    public void saveAnswer() {
        String startTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new Date());
        String fileName = startTime.substring(0, 4) + startTime.substring(5, 7) + startTime.substring(8, 10) + "_" + GUIClass.staffID;
        PrintWriter pw;
        StringBuilder sb = new StringBuilder();
        try {
            File f = new File(System.getProperty("user.dir") + "/" + fileName + "_questionnaire.csv");
            if (!f.exists() || f.isDirectory()) {
                pw = new PrintWriter(new FileWriter(fileName + "_questionnaire.csv"));
                sb.append("Timestamp");
                sb.append(',');
                sb.append("Active");
                sb.append('\n');
                System.out.println("Create new file");
            } else {
                pw = new PrintWriter(new FileWriter(System.getProperty("user.dir") + "/" + fileName + "_questionnaire.csv", true));
            }
            String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new Date());
            sb.append(timestamp);
            sb.append(',');
            sb.append(jSlider1.getValue());
            sb.append('\n');
            System.out.println("saveAnswer");
            pw.write(sb.toString());
            pw.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        jSlider1.setValue(5);
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton confirmButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextArea jTextArea5;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}