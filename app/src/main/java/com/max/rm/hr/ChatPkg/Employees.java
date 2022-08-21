package com.androidmax.max.hr.ChatPkg;

import java.io.Serializable;

public class Employees  implements Serializable{
    String emp_code;
    String emp_name;
    String register_key;
    String start_at;
    String end_at;
     int countNotSeenMessages;

    public int getCountNotSeenMessages() {
        return countNotSeenMessages;
    }

    public void setCountNotSeenMessages(int countNotSeenMessages) {
        this.countNotSeenMessages = countNotSeenMessages;
    }

    public Employees() {
    }

    public Employees(String emp_code, String emp_name, String register_key, String start_at, String end_at, int countNotSeenMessages) {
        this.emp_code = emp_code;
        this.emp_name = emp_name;
        this.register_key = register_key;
        this.start_at = start_at;
        this.end_at = end_at;
        this.countNotSeenMessages = countNotSeenMessages;
    }

    public Employees(String emp_code, String emp_name, String register_key, String start_at, String end_at) {
        this.emp_code = emp_code;
        this.emp_name = emp_name;
        this.register_key = register_key;
        this.start_at = start_at;
        this.end_at = end_at;
    }

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    @Override
    public String toString() {
        return "Employees{" +
                "emp_code='" + emp_code + '\'' +
                ", emp_name='" + emp_name + '\'' +
                ", register_key='" + register_key + '\'' +
                '}';
    }

    public Employees(String emp_code, String emp_name, String register_key) {
        this.emp_code = emp_code;
        this.emp_name = emp_name;
        this.register_key = register_key;
    }

    public String getRegister_key() {
        return register_key;
    }

    public void setRegister_key(String register_key) {
        this.register_key = register_key;
    }

    public Employees(String emp_code, String emp_name) {
        this.emp_code = emp_code;
        this.emp_name = emp_name;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }
}
