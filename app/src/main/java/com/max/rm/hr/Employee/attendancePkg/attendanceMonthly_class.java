package com.androidmax.max.hr.Employee.attendancePkg;

public class attendanceMonthly_class {
    int year;
    int month;
    String workHour;
    String requiredWorkHour;
    String absenceHour;
    String lateHour;
    String checkoutEarlyHour;
    String permissionHour;
    String overtimeHour;
    String empCode,empName;

    public attendanceMonthly_class(int year, int month, String workHour, String requiredWorkHour, String absenceHour, String lateHour, String checkoutEarlyHour, String permissionHour, String overtimeHour, String empCode, String empName) {
        this.year = year;
        this.month = month;
        this.workHour = workHour;
        this.requiredWorkHour = requiredWorkHour;
        this.absenceHour = absenceHour;
        this.lateHour = lateHour;
        this.checkoutEarlyHour = checkoutEarlyHour;
        this.permissionHour = permissionHour;
        this.overtimeHour = overtimeHour;
        this.empCode = empCode;
        this.empName = empName;
    }

    public String getEmpCode() {
        return empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public attendanceMonthly_class(int year, int month, String workHour, String requiredWorkHour, String absenceHour, String lateHour, String checkoutEarlyHour, String permissionHour, String overtimeHour) {
        this.year = year;
        this.month = month;
        this.workHour = workHour;
        this.requiredWorkHour = requiredWorkHour;
        this.absenceHour = absenceHour;
        this.lateHour = lateHour;
        this.checkoutEarlyHour = checkoutEarlyHour;
        this.permissionHour = permissionHour;
        this.overtimeHour = overtimeHour;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public String getWorkHour() {
        return workHour;
    }

    public String getRequiredWorkHour() {
        return requiredWorkHour;
    }

    public String getAbsenceHour() {
        return absenceHour;
    }

    public String getLateHour() {
        return lateHour;
    }

    public String getCheckoutEarlyHour() {
        return checkoutEarlyHour;
    }

    public String getPermissionHour() {
        return permissionHour;
    }

    public String getOvertimeHour() {
        return overtimeHour;
    }
}
