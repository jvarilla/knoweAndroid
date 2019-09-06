package it372.finalprojectvarilla;
/*
Name: Joseph Varilla
        Date: 6/10/2019
        Final Project
 */
public class Loan {


    private boolean isOwed;
    private float amount;
    private String fname;
    private String lname;
    private String phone;
    private String borrowedDate;
    private String dueDate;
    private boolean receiveAlerts;

    public boolean isOwed() {
        return isOwed;
    }

    public void setOwed(boolean owed) {
        isOwed = owed;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isReceiveAlerts() {
        return receiveAlerts;
    }

    public void setReceiveAlerts(boolean receiveAlerts) {
        this.receiveAlerts = receiveAlerts;
    }

    public boolean isAffirmed() {
        return affirmed;
    }

    public void setAffirmed(boolean affirmed) {
        this.affirmed = affirmed;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    private boolean affirmed;
    private boolean isPaid;

    public Loan(boolean isOwed, float amount, String fname, String lname, String phone, String borrowedDate, String dueDate, boolean receiveAlerts, boolean affirmed) {
        this.isOwed = isOwed;
        this.amount = amount;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.receiveAlerts = receiveAlerts;
        this.affirmed = affirmed;
        this.isPaid = false;
    }




}
