package com.androidmax.max.hr.Employee.attendancePkg;

public class attendanceDaily_class {
    String date;
    String day;
    String check_in;
    String check_out;
    String state;
    String period;
    String empCode,empName;

    public String getEmpCode() {
        return empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public attendanceDaily_class(String date, String day, String check_in, String check_out, String state, String period, String empCode, String empName) {
        this.date = date;
        this.day = day;
        this.check_in = check_in;
        this.check_out = check_out;
        this.state = state;
        this.period = period;
        this.empCode = empCode;
        this.empName = empName;
    }

    public attendanceDaily_class(String date, String day, String check_in, String check_out, String state, String period) {
        this.date = date;
        this.day = day;
        this.check_in = check_in;
        this.check_out = check_out;
        this.state = state;
        this.period = period;
    }


    @Override
    public String toString() {
        return "attendanceDaily_class{" +
                "date='" + date + '\'' +
                ", day='" + day + '\'' +
                ", check_in='" + check_in + '\'' +
                ", check_out='" + check_out + '\'' +
                ", state='" + state + '\'' +
                ", period='" + period + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getCheck_in() {
        return check_in;
    }

    public String getCheck_out() {
        return check_out;
    }

    public String getState() {
        return state;
    }

    public String getPeriod() {
        return period;
    }

}
