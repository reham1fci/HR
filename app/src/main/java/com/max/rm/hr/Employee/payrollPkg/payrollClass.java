package com.max.rm.hr.Employee.payrollPkg;

public class payrollClass  {
    String emp_code,emp_name;
    String itemName,itemAmount;
    String deduction,raise, total;
    String year,month;

    String Date;
    int type;

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public payrollClass(String emp_code, String emp_name, String deduction, String raise, String total, String date , String month, String year) {
        this.emp_code = emp_code;
        this.emp_name = emp_name;
        this.deduction = deduction;
        this.raise = raise;
        this.total = total;
        this.year = year;
        this.month = month;
        Date = date;
    }

    public String getDeduction() {
        return deduction;
    }

    public String getRaise() {
        return raise;
    }

    public String getTotal() {
        return total;
    }

    public String getDate() {
        return Date;
    }

    public payrollClass(String emp_code, String emp_name, String itemName, String itemAmount, int type) {
        this.emp_code = emp_code;
        this.emp_name = emp_name;
        this.itemName = itemName;
        this.itemAmount = itemAmount;
        this.type=type;
    }

    public int getType() {
        return type;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemAmount() {
        return itemAmount;
    }
}
