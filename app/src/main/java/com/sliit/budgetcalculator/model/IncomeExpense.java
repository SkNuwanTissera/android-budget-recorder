package com.sliit.budgetcalculator.model;

public class IncomeExpense {

    private long id;
    private String description;
    private String date;
    private String ie_type;
    private Double amount;

    public IncomeExpense() {

    }

    public IncomeExpense(String description, String date, Double amount,String ie_type) {
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.ie_type = ie_type;
    }

    public IncomeExpense(String description, String date, Double amount) {
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.ie_type = ie_type;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getIe_type() {
        return ie_type;
    }

    public void setIe_type(String ie_type) {
        this.ie_type = ie_type;
    }

}
