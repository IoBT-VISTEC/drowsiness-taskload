/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DrowsinessApp;

/**
 *
 * @author guygu
 */
public class Transaction {
    private int id;
    private String type;
    private String bank;
    private String account;
    private String accountTx;
    private String owner;
    private double amountDue;
    private double amountTransfer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    public double getAmountTransfer() {
        return amountTransfer;
    }

    public void setAmountTransfer(double amountTransfer) {
        this.amountTransfer = amountTransfer;
    }

    public String getAccountTx() {
        return accountTx;
    }

    public void setAccountTx(String showAccount) {
        this.accountTx = showAccount;
    }

    public Transaction(int id, String type, String bank, String account) {
        this.id = id;
        this.type = type;
        this.bank = bank;
        this.account = account;
    }

    public Transaction(int id, String type, String bank, String account, String owner, double amountDue, double amountTransfer) {
        this.id = id;
        this.type = type;
        this.bank = bank;
        this.account = account;
        this.owner = owner;
        this.amountDue = amountDue;
        this.amountTransfer = amountTransfer;
    }

    public Transaction(int id, String type, String bank, String account, String accountTx, String owner, double amountDue, double amountTransfer) {
        this.id = id;
        this.type = type;
        this.bank = bank;
        this.account = account;
        this.accountTx = accountTx;
        this.owner = owner;
        this.amountDue = amountDue;
        this.amountTransfer = amountTransfer;
    }
    
}
