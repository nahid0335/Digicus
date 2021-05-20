package com.example.digicus.model;

public class Finance {
    private int financeId;
    private String financeTitle;
    private float currentBalance;

    public Finance() {

    }

    public int getFinanceId() {
        return financeId;
    }

    public String getFinanceTitle() {
        return financeTitle;
    }

    public float getCurrentBalance() {
        return currentBalance;
    }

    public void setFinanceId(int financeId) {
        this.financeId = financeId;
    }

    public void setFinanceTitle(String financeTitle) {
        this.financeTitle = financeTitle;
    }

    public void setCurrentBalance(float currentBalance) {
        this.currentBalance = currentBalance;
    }
}
