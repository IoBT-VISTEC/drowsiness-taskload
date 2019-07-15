 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DrowsinessApp;

import java.awt.CardLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import java.util.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.*;
import java.awt.event.*;

/**
 *
 * @author guygu
 */
public class GUIClass extends javax.swing.JFrame {

    private static CardLayout card;                                 //for swapping the pages
    private static HashMap<Integer, Transaction> transactionSet;    //store all transactions
    private static StaffAccount staff;                              //all staff accounts
    private List<Point> cursorLocations;                            //list of cursor's location (Point x, y)
    private List<KeyClass> keysPressed;                             //list of KeyClass
    private List<Integer> showingData;                              //indexes of transactions that showing on the table
    private List<Integer> stackData;                                //list of transactions that will be fetched when clicking update
    private String fileName;                                        //file name
    private boolean isTxShown = false;                              //status of transaction detail page
    //private int selectedId;                                         //currently searching id                       
    private int dataCheckingStage;                                  //for swapping between start and stop buttons
    private Timer coreTime;                                         //timer for saving cursor and key pressed
    private Timer questionnaireTime;
    private Runnable collectCursor;                                 //collect the cursor 
    private Runnable refreshData;                                   //automatically refresh data every 15 mins
    private ScheduledExecutorService executor;                      //for running the collectCursor and refreshData
    private DecimalFormat numberFormat = new DecimalFormat("#,###.##"); //format for printing number (1,234.56)
    private Transaction currentTx;                                  //currently search transaction                               
    private Questionnaire questionPanel;
    /**
     * Creates new form GUIClass
     */
    public GUIClass() {
        initComponents();
        txTable.setDefaultEditor(Object.class, null);
        card = (CardLayout) mainPanel.getLayout();
        setTableHeader();
        setTableData();
        cursorLocations = new ArrayList<>();
        keysPressed = new ArrayList<>();
        dataCheckingStage = 0;
        stackData = new ArrayList<>();

        //collect key pressed
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        if (e.getID() == KeyEvent.KEY_PRESSED) {
                            keysPressed.add(new KeyClass(e.getKeyChar(), new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new Date())));
                        }
                        return false;
                    }
                });

        //collect the cursor's location
        collectCursor = new Runnable() {
            @Override
            public void run() {
                addCursorLocation("");
            }
        };

        //refresh showing table every 15 mins
        refreshData = new Runnable() {
            @Override
            public void run() {
                autoSetTable(true);
            }
        };

        questionPanel = new Questionnaire();
    }

    //show all transactions to table (default)
    public void setTableData() {
        DefaultTableModel model = (DefaultTableModel) txTable.getModel();
        model.setRowCount(0);
        int i = 0;
        List<Transaction> vals = new ArrayList<>(transactionSet.values());
        Collections.shuffle(vals);
        for (Transaction val : vals) {
            model.insertRow(i++, new Object[]{val.getId(), val.getType(), val.getBank(), val.getAccount()});
        }
    }

    //show transactions with the number of transactions
    public void setTableData(int num) {
        DefaultTableModel model = (DefaultTableModel) txTable.getModel();
        model.setRowCount(0);
        List<Integer> keys = new ArrayList<>(transactionSet.keySet());
        Collections.shuffle(keys);
        Transaction val;
        showingData = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            showingData.add(keys.get(i));
            val = transactionSet.get(keys.get(i));
            model.insertRow(i, new Object[]{val.getId(), val.getType(), val.getBank(), val.getAccount()});
        }
    }

    //append the data from the stackData to the table
    public void autoSetTable(boolean update) {
        DefaultTableModel model = (DefaultTableModel) txTable.getModel();
        model.setRowCount(0);
        int i = 0;
        Transaction tmp;
        if (update) {
            for (Integer idx : stackData) {
                showingData.add(idx);
            }
            stackData = new ArrayList<>();
        }
        for (Integer idx : showingData) {
            tmp = transactionSet.get(idx);
            model.insertRow(i++, new Object[]{tmp.getId(), tmp.getType(), tmp.getBank(), tmp.getAccount()});
        }
    }

    //set the column name
    public void setTableHeader() {
        Object[] columnNames = {"Transaction ID", "Type", "Bank", "Bank Account"};
        txTable.getTableHeader().setResizingAllowed(false);
        JTableHeader th = txTable.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        for (int i = 0; i < columnNames.length; i++) {
            tcm.getColumn(i).setHeaderValue(columnNames[i]);
        }
        th.repaint();
    }

    //check for the duplicate of transaction idx in the showing table
    public boolean isDuplicate(int num) {
        for (int idx : showingData) {
            if (idx == num) {
                return true;
            }
        }
        return false;
    }

    //check if the transaction id exists
    public boolean isTxidCorrect(int txid) {
        if (isDuplicate(txid) && transactionSet.get(txid) != null) {
            currentTx = transactionSet.get(txid);
            accountTextField.setText(currentTx.getAccount());
            ownerTextField.setText(currentTx.getOwner());
            amountTextField.setText(numberFormat.format(currentTx.getAmountDue()));
            transferTextField.setText(numberFormat.format(currentTx.getAmountTransfer()));
            //selectedId = currentTx.getId();
            isTxShown = true;

            return true;
        }
        return false;
    }

    //add the cursor location
    public void addCursorLocation(String event) {
        java.awt.Point p = MouseInfo.getPointerInfo().getLocation();
        String tmp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new Date());
        cursorLocations.add(new Point((int) p.getX(), (int) p.getY(), tmp, event));
    }

    //clear all the textfields in transaction page
    public void clearTransactionPage() {
        accountTextField.setText("");
        ownerTextField.setText("");
        amountTextField.setText("");
        transferTextField.setText("");
        enterTxidTextField.setText("");
    }

    //clear all the textfiles in staff page
    public void clearStaffPage() {
        staffIdTextField.setText("");
        staffPwdField.setText("");
    }

    //save the all key pressed to the file
    public void saveKeyPressed() {
        PrintWriter pw;
        StringBuilder sb = new StringBuilder();
        try {
            File f = new File(System.getProperty("user.dir") + "/" + fileName + "_key.csv");
            if (!f.exists() || f.isDirectory()) {
                pw = new PrintWriter(new FileWriter(fileName + "_key.csv"));
                sb.append("Timestamp");
                sb.append(',');
                sb.append("Key");
                sb.append('\n');
            } else {
                pw = new PrintWriter(new FileWriter(System.getProperty("user.dir") + "/" + fileName + "_key.csv", true));
            }
            for (KeyClass k : keysPressed) {
                sb.append(k.getTimePoint());
                sb.append(',');
                sb.append(k.getKey());
                sb.append('\n');
            }
            pw.write(sb.toString());
            pw.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    //save all the collected cursor to the file
    public void saveCursorLocation() {
        PrintWriter pw;
        StringBuilder sb = new StringBuilder();
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
            for (Point p : cursorLocations) {
                sb.append(p.getTimePoint());
                sb.append(',');
                sb.append(p.getX());
                sb.append(',');
                sb.append(p.getY());
                sb.append(',');
                sb.append(p.getEvent());
                sb.append('\n');
            }
            pw.write(sb.toString());
            pw.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    //save the result of confirmation
    public void saveResult(boolean confirm) {
        String result = currentTx.getAmountDue() == currentTx.getAmountTransfer() ^ confirm ? "FALSE" : "TRUE";
        String event = confirm ? "Confirm" : "Report";
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new Date());
        String amountDue = numberFormat.format(currentTx.getAmountDue());
        String transfer = numberFormat.format(currentTx.getAmountTransfer());

        PrintWriter pw;
        StringBuilder sb = new StringBuilder();
        try {
            File f = new File(System.getProperty("user.dir") + "/" + fileName + "_result.csv");
            if (!f.exists() || f.isDirectory()) {
                pw = new PrintWriter(new FileWriter(fileName + "_result.csv"));
                sb.append("Timestamp");
                sb.append(',');
                sb.append("Transaction ID");
                sb.append(',');
                sb.append("Bank account");
                sb.append(',');
                sb.append("Bank account transaction");
                sb.append(',');
                sb.append("Amount due");
                sb.append(',');
                sb.append("Amount transferred");
                sb.append(',');
                sb.append("Event");
                sb.append(',');
                sb.append("Result");
                sb.append('\n');
            } else {
                pw = new PrintWriter(new FileWriter(System.getProperty("user.dir") + "/" + fileName + "_result.csv", true));
            }
            sb.append(timestamp);
            sb.append(',');
            sb.append(currentTx.getId());
            sb.append(',');
            sb.append(currentTx.getAccount());
            sb.append(',');
            sb.append(currentTx.getAccount());
            sb.append(',');
            sb.append("\"" + amountDue + "\"");
            sb.append(',');
            sb.append("\"" + transfer + "\"");
            sb.append(',');
            sb.append(event);
            sb.append(',');
            sb.append(result);
            sb.append('\n');
            pw.write(sb.toString());
            pw.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        companyPanel = new javax.swing.JPanel();
        refreshButton = new javax.swing.JButton();
        startButton = new javax.swing.JButton();
        staffNoField = new javax.swing.JTextField();
        staffNoLabel = new javax.swing.JLabel();
        tableScroll = new javax.swing.JScrollPane();
        txTable = new javax.swing.JTable();
        companyLabel = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        txPanel = new javax.swing.JPanel();
        enterTxIdLabel = new javax.swing.JLabel();
        enterTxidTextField = new javax.swing.JTextField();
        go2Button = new javax.swing.JButton();
        txDetailPanel = new javax.swing.JPanel();
        accountLabel = new javax.swing.JLabel();
        ownerLabel = new javax.swing.JLabel();
        accountTextField = new javax.swing.JTextField();
        ownerTextField = new javax.swing.JTextField();
        amountLabel = new javax.swing.JLabel();
        amountTextField = new javax.swing.JTextField();
        transferLabel = new javax.swing.JLabel();
        transferTextField = new javax.swing.JTextField();
        confirmButton = new javax.swing.JButton();
        reportButton = new javax.swing.JButton();
        txLabel = new javax.swing.JLabel();
        staffPanel = new javax.swing.JPanel();
        staffIdLabel = new javax.swing.JLabel();
        staffIdTextField = new javax.swing.JTextField();
        staffPwdLabel = new javax.swing.JLabel();
        confirm2Button = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        staffPwdField = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Drowsiness Application");

        refreshButton.setBackground(new java.awt.Color(204, 204, 255));
        refreshButton.setText("Update query");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        startButton.setBackground(new java.awt.Color(0, 102, 255));
        startButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        startButton.setForeground(new java.awt.Color(255, 255, 255));
        startButton.setText("Start data checking");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        staffNoField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        staffNoLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        staffNoLabel.setText("Staff no.");

        txTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableScroll.setViewportView(txTable);

        companyLabel.setBackground(new java.awt.Color(255, 102, 51));
        companyLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        companyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        companyLabel.setText("X Company");
        companyLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        companyLabel.setOpaque(true);

        javax.swing.GroupLayout companyPanelLayout = new javax.swing.GroupLayout(companyPanel);
        companyPanel.setLayout(companyPanelLayout);
        companyPanelLayout.setHorizontalGroup(
            companyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(companyPanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(companyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                    .addGroup(companyPanelLayout.createSequentialGroup()
                        .addComponent(companyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(companyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(refreshButton)
                            .addGroup(companyPanelLayout.createSequentialGroup()
                                .addComponent(staffNoLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(staffNoField, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(29, 29, 29))
        );
        companyPanelLayout.setVerticalGroup(
            companyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(companyPanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(companyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(companyPanelLayout.createSequentialGroup()
                        .addGroup(companyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(staffNoLabel)
                            .addComponent(staffNoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(startButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshButton))
                    .addComponent(companyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(tableScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainPanel.setLayout(new java.awt.CardLayout());

        enterTxIdLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        enterTxIdLabel.setText("Please enter transaction ID");

        enterTxidTextField.setBackground(new java.awt.Color(240, 219, 248));
        enterTxidTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                enterTxidTextFieldMouseClicked(evt);
            }
        });
        enterTxidTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterTxidTextFieldActionPerformed(evt);
            }
        });

        go2Button.setBackground(new java.awt.Color(153, 204, 255));
        go2Button.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        go2Button.setText("Go");
        go2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                go2ButtonActionPerformed(evt);
            }
        });

        txDetailPanel.setBackground(new java.awt.Color(255, 243, 227));
        txDetailPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        accountLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        accountLabel.setText("Bank Account");

        ownerLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        ownerLabel.setText("Owner");

        accountTextField.setEditable(false);
        accountTextField.setBackground(new java.awt.Color(227, 227, 255));
        accountTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        ownerTextField.setEditable(false);
        ownerTextField.setBackground(new java.awt.Color(227, 227, 255));
        ownerTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        amountLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        amountLabel.setText("Amount Due");

        amountTextField.setEditable(false);
        amountTextField.setBackground(new java.awt.Color(227, 227, 255));
        amountTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        transferLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        transferLabel.setText("Amount Transferred");

        transferTextField.setEditable(false);
        transferTextField.setBackground(new java.awt.Color(227, 227, 255));
        transferTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        confirmButton.setBackground(new java.awt.Color(0, 204, 51));
        confirmButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        confirmButton.setText("Confirm");
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });

        reportButton.setBackground(new java.awt.Color(255, 51, 0));
        reportButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        reportButton.setText("Report");
        reportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout txDetailPanelLayout = new javax.swing.GroupLayout(txDetailPanel);
        txDetailPanel.setLayout(txDetailPanelLayout);
        txDetailPanelLayout.setHorizontalGroup(
            txDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, txDetailPanelLayout.createSequentialGroup()
                .addGroup(txDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, txDetailPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41))
                    .addGroup(txDetailPanelLayout.createSequentialGroup()
                        .addGroup(txDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(txDetailPanelLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(txDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(transferTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                                    .addComponent(amountTextField, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(accountTextField, javax.swing.GroupLayout.Alignment.LEADING)))
                            .addGroup(txDetailPanelLayout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(accountLabel)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)))
                .addGroup(txDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, txDetailPanelLayout.createSequentialGroup()
                        .addComponent(reportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63))
                    .addGroup(txDetailPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(ownerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, txDetailPanelLayout.createSequentialGroup()
                        .addComponent(ownerLabel)
                        .addGap(84, 84, 84))))
            .addGroup(txDetailPanelLayout.createSequentialGroup()
                .addGroup(txDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(txDetailPanelLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(transferLabel))
                    .addGroup(txDetailPanelLayout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(amountLabel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        txDetailPanelLayout.setVerticalGroup(
            txDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(txDetailPanelLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(txDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(accountLabel)
                    .addComponent(ownerLabel))
                .addGap(18, 18, 18)
                .addGroup(txDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(accountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ownerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(amountLabel)
                .addGap(18, 18, 18)
                .addComponent(amountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(transferLabel)
                .addGap(18, 18, 18)
                .addComponent(transferTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addGroup(txDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52))
        );

        txLabel.setBackground(new java.awt.Color(158, 182, 131));
        txLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txLabel.setText("Transaction");
        txLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txLabel.setOpaque(true);

        javax.swing.GroupLayout txPanelLayout = new javax.swing.GroupLayout(txPanel);
        txPanel.setLayout(txPanelLayout);
        txPanelLayout.setHorizontalGroup(
            txPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(txPanelLayout.createSequentialGroup()
                .addGroup(txPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(txPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(txPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(enterTxIdLabel)
                            .addComponent(go2Button)))
                    .addComponent(enterTxidTextField))
                .addGap(18, 18, 18)
                .addComponent(txDetailPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, txPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(txLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(142, 142, 142))
        );
        txPanelLayout.setVerticalGroup(
            txPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(txPanelLayout.createSequentialGroup()
                .addGroup(txPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(txPanelLayout.createSequentialGroup()
                        .addGap(191, 191, 191)
                        .addComponent(enterTxIdLabel)
                        .addGap(18, 18, 18)
                        .addComponent(enterTxidTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(go2Button))
                    .addGroup(txPanelLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(txLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txDetailPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        mainPanel.add(txPanel, "txPanel");

        staffIdLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        staffIdLabel.setText("Staff ID");

        staffIdTextField.setBackground(new java.awt.Color(251, 233, 255));

        staffPwdLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        staffPwdLabel.setText("Staff Password");

        confirm2Button.setBackground(new java.awt.Color(0, 255, 51));
        confirm2Button.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        confirm2Button.setText("Confirm");
        confirm2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirm2ButtonActionPerformed(evt);
            }
        });

        cancelButton.setBackground(new java.awt.Color(255, 51, 0));
        cancelButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        staffPwdField.setBackground(new java.awt.Color(251, 233, 255));
        staffPwdField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffPwdFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout staffPanelLayout = new javax.swing.GroupLayout(staffPanel);
        staffPanel.setLayout(staffPanelLayout);
        staffPanelLayout.setHorizontalGroup(
            staffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, staffPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(staffIdLabel)
                .addGap(266, 266, 266))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, staffPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(staffPwdLabel)
                .addGap(239, 239, 239))
            .addGroup(staffPanelLayout.createSequentialGroup()
                .addGap(201, 201, 201)
                .addGroup(staffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(staffPwdField)
                    .addComponent(staffIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(staffPanelLayout.createSequentialGroup()
                        .addComponent(confirm2Button)
                        .addGap(18, 18, 18)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(194, Short.MAX_VALUE))
        );
        staffPanelLayout.setVerticalGroup(
            staffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(staffPanelLayout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(staffIdLabel)
                .addGap(18, 18, 18)
                .addComponent(staffIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(staffPwdLabel)
                .addGap(18, 18, 18)
                .addComponent(staffPwdField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addGroup(staffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirm2Button)
                    .addComponent(cancelButton))
                .addContainerGap(170, Short.MAX_VALUE))
        );

        mainPanel.add(staffPanel, "staffPanel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(companyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(companyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
   
    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonActionPerformed
        // TODO add your handling code here:   
        addCursorLocation("Confirm_button");

        if (dataCheckingStage == 0) {   //if user doesn't click start button yet
            JOptionPane.showMessageDialog(rootPane, "Please press \"start checking data\" first!", "Error", ERROR_MESSAGE);
            return;
        }
        if (isTxShown) {                //make sure there is a transactions that is showing
            saveResult(true);
            clearStaffPage();
            card.show(mainPanel, "staffPanel");
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please enter Transaction ID!", "Error", ERROR_MESSAGE);
        }

    }//GEN-LAST:event_confirmButtonActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        // TODO add your handling code here:
        autoSetTable(true);
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void reportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportButtonActionPerformed
        // TODO add your handling code here:
        addCursorLocation("Report_button");

        if (dataCheckingStage == 0) {
            JOptionPane.showMessageDialog(rootPane, "Please press \"start checking data\" first!", "Error", ERROR_MESSAGE);
            return;
        }
        if (isTxShown) {
            saveResult(false);
            clearStaffPage();
            card.show(mainPanel, "staffPanel");
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please enter Transaction ID!", "Error", ERROR_MESSAGE);
        }
    }//GEN-LAST:event_reportButtonActionPerformed
    
    private void confirm2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirm2ButtonActionPerformed
        //check for empty username and password
        if (staffIdTextField.getText().isBlank() || staffPwdField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(rootPane, "Please enter both username and password!", "Error", ERROR_MESSAGE);
        } else if (staff.isAuthen(staffIdTextField.getText(), staffPwdField.getPassword())) {   //authenticate the username and password
            //showingData.remove((Integer) selectedId);
            showingData.remove((Integer) currentTx.getId());                    //remove the confirmed transaction from the showingData
            transactionSet.remove(currentTx.getId());                           //also from the transaction set
            autoSetTable(false);                                                
            card.show(mainPanel, "txPanel");
            enterTxidTextField.setText("");
            isTxShown = false;
            clearStaffPage();
            clearTransactionPage();
        } else {
            JOptionPane.showMessageDialog(rootPane, "Username or password is not correct!", "Error", ERROR_MESSAGE);
        }
    }//GEN-LAST:event_confirm2ButtonActionPerformed

    private void staffPwdFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffPwdFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_staffPwdFieldActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // TODO add your handling code here:
        clearStaffPage();
        card.show(mainPanel, "txPanel");
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void go2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_go2ButtonActionPerformed
        // TODO add your handling code here:
        addCursorLocation("Go_button");
        if (dataCheckingStage == 0) {
            JOptionPane.showMessageDialog(rootPane, "Please press \"start checking data\" first!", "Error", ERROR_MESSAGE);
            return;
        }
        if (enterTxidTextField.getText().isBlank()) {
            JOptionPane.showMessageDialog(rootPane, "Please enter Transaction ID!", "Error", ERROR_MESSAGE);
        } else {
            try {
                int number = Integer.parseInt(enterTxidTextField.getText());
                if (!isTxidCorrect(number)) {
                    JOptionPane.showMessageDialog(rootPane, "Transaction ID is not found!", "Error", ERROR_MESSAGE);
                }
            } catch (NumberFormatException ne) {
                JOptionPane.showMessageDialog(rootPane, "Transaction ID must be a number!", "Error", ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_go2ButtonActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        // TODO add your handling code here:
        if (dataCheckingStage == 0) {
            if (staffNoField.getText().isBlank()) {
                JOptionPane.showMessageDialog(rootPane, "Please enter Staff no.!", "Error", ERROR_MESSAGE);
            } else {
                try {
                    int staffID = Integer.parseInt(staffNoField.getText());
                    GUIClass.staffID = staffID;
                    startButton.setText("Stop data checking");
                    String startTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new Date());
                    fileName = startTime.substring(0, 4) + startTime.substring(5, 7) + startTime.substring(8, 10) + "_" + staffID;
                    dataCheckingStage = 1;
                    setTableData(15);
                    
                    coreTime = new java.util.Timer();
                    coreTime.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            saveKeyPressed();
                            saveCursorLocation();
                            cursorLocations = new ArrayList<>();
                            keysPressed = new ArrayList<>();
                        }
                    }, 1000, 1000);
                    
                    questionnaireTime = new java.util.Timer();
                    questionnaireTime.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            questionPanel.setVisible(true);
                        }
                    }, 5000, 300000);
                    
                    Timer randomTime = new Timer();
                    
                    randomTime.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            List<Integer> keys = new ArrayList<>(transactionSet.keySet());
                            int randIdx = new Random().nextInt(keys.size());
                            while (isDuplicate(keys.get(randIdx))) {
                                randIdx = new Random().nextInt(keys.size());
                            }
                            stackData.add(keys.get(randIdx));
                        }
                    }, (15 + new Random().nextInt(16)) * 1000, (15 + new Random().nextInt(16)) * 1000);
                    executor = Executors.newScheduledThreadPool(2);
                    executor.scheduleAtFixedRate(collectCursor, 0, 20, TimeUnit.MILLISECONDS);
                    executor.scheduleAtFixedRate(refreshData, 15, 15, TimeUnit.MINUTES);

                } catch (NumberFormatException ne) {
                    staffNoField.setText("");
                    JOptionPane.showMessageDialog(rootPane, "Staff no. must be a number!", "Error", ERROR_MESSAGE);
                }
            }
            staffNoField.setText("");
        } else if (dataCheckingStage == 1) {
            startButton.setText("Start data checking");

            coreTime.cancel();
            questionnaireTime.cancel();
            executor.shutdownNow();

            dataCheckingStage = 0;
            //selectedId = -1;
            currentTx = null;
            setTableData();
        }

    }//GEN-LAST:event_startButtonActionPerformed

    private void enterTxidTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterTxidTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_enterTxidTextFieldActionPerformed

    private void enterTxidTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_enterTxidTextFieldMouseClicked
        // TODO add your handling code here:
        addCursorLocation("Transaction_box");
    }//GEN-LAST:event_enterTxidTextFieldMouseClicked

    /**
     * @param args the command line arguments
     */
    public static int staffID = 0;
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
            java.util.logging.Logger.getLogger(GUIClass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUIClass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUIClass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUIClass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        Random rd = new Random();

        long startId = 11132334800l;
        double amount;
        double transfer;
        staff = new StaffAccount();
        staff.addAccount("sky", "skypwd");

        int txID;
        transactionSet = new HashMap<>();
        transactionSet.put(1134, new Transaction(1134, "Transaction", "SCB", "11111111112", "Luke Skywalker", 65535, 56636));
        transactionSet.put(1335, new Transaction(1335, "Credit", "KTB", "11131313111", "Someone", 99.99, 9.99));
        transactionSet.put(1136, new Transaction(1136, "Transaction", "KBank", "11132332121", "Thayakorn", 32745.75, 32285.5));
        for (int i = 0; i < 300; i++) {
            String type, bank;
            if (i % 5 == 0) {
                bank = "SCB";
            } else if (i % 3 == 0) {
                bank = "TMB";
            } else if (i % 2 == 0) {
                bank = "KBANK";
            } else {
                bank = "KTB";
            }
            if (i % 2 == 0) {
                type = "Transaction";
            } else {
                type = "Credit";
            }

            amount = rd.nextDouble() * 50000;
            transfer = rd.nextDouble() * (amount / 2);

            if (rd.nextInt(2) == 1) {
                transfer = 0;
            }
            txID = 1137 + i;
            transactionSet.put(txID, new Transaction(txID, type, bank, startId + (3 * i) + "", "Dummy " + i, amount, amount - transfer));

        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUIClass().setVisible(true);
            }
        });
        // ************** END POPUP
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel accountLabel;
    private javax.swing.JTextField accountTextField;
    private javax.swing.JLabel amountLabel;
    private javax.swing.JTextField amountTextField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel companyLabel;
    private javax.swing.JPanel companyPanel;
    private javax.swing.JButton confirm2Button;
    private javax.swing.JButton confirmButton;
    private javax.swing.JLabel enterTxIdLabel;
    private javax.swing.JTextField enterTxidTextField;
    private javax.swing.JButton go2Button;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel ownerLabel;
    private javax.swing.JTextField ownerTextField;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton reportButton;
    private javax.swing.JLabel staffIdLabel;
    private javax.swing.JTextField staffIdTextField;
    private javax.swing.JTextField staffNoField;
    private javax.swing.JLabel staffNoLabel;
    private javax.swing.JPanel staffPanel;
    private javax.swing.JPasswordField staffPwdField;
    private javax.swing.JLabel staffPwdLabel;
    private javax.swing.JButton startButton;
    private javax.swing.JScrollPane tableScroll;
    private javax.swing.JLabel transferLabel;
    private javax.swing.JTextField transferTextField;
    private javax.swing.JPanel txDetailPanel;
    private javax.swing.JLabel txLabel;
    private javax.swing.JPanel txPanel;
    private javax.swing.JTable txTable;
    // End of variables declaration//GEN-END:variables
}
