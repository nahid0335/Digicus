package com.example.digicus.model;

public class Details {
    private int detailsId;
    private String detailsDetails;
    private String detailstype;
    private Double detailsOperand1;
    private Double detailsOperand2;
    private String detailsOpcode;
    private Double detailsBalance;
    private String detailsCreatedAt;
    private int financeId;

    public Details() {
        //code
    }

    public void setDetailsId(int detailsId) {
        this.detailsId = detailsId;
    }

    public void setDetailsDetails(String detailsDetails) {
        this.detailsDetails = detailsDetails;
    }

    public void setDetailstype(String detailstype) {
        this.detailstype = detailstype;
    }

    public void setDetailsOperand1(Double detailsOperand1) {
        this.detailsOperand1 = detailsOperand1;
    }

    public void setDetailsOperand2(Double detailsOperand2) {
        this.detailsOperand2 = detailsOperand2;
    }

    public void setDetailsOpcode(String detailsOpcode) {
        this.detailsOpcode = detailsOpcode;
    }

    public void setDetailsBalance(Double detailsBalance) {
        this.detailsBalance = detailsBalance;
    }

    public void setDetailsCreatedAt(String detailsCreatedAt) {
        this.detailsCreatedAt = detailsCreatedAt;
    }

    public void setFinanceId(int financeId) {
        this.financeId = financeId;
    }

    public int getDetailsId() {
        return detailsId;
    }

    public String getDetailsDetails() {
        return detailsDetails;
    }

    public String getDetailstype() {
        return detailstype;
    }

    public Double getDetailsOperand1() {
        return detailsOperand1;
    }

    public Double getDetailsOperand2() {
        return detailsOperand2;
    }

    public String getDetailsOpcode() {
        return detailsOpcode;
    }

    public Double getDetailsBalance() {
        return detailsBalance;
    }

    public String getDetailsCreatedAt() {
        return detailsCreatedAt;
    }

    public int getFinanceId() {
        return financeId;
    }
}
