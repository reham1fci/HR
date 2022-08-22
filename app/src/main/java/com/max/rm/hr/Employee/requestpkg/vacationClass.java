package com.max.rm.hr.Employee.requestpkg;

import java.io.Serializable;

public class vacationClass implements Serializable {
    String emp_code,emp_name,vStatues,vStartDate,vEndDate,vDescription,vRejectedDate,vRejectedNote,vSlice,vAmount,Note;
          int  vIsUsed,vIsRejected, recordId;
// for loan
    public vacationClass(String emp_code, String emp_name, String statues, String loan_date, String loanDesc, String reject_note, String loanAmount, int record_id) {
        this.emp_code = emp_code;
        this.emp_name = emp_name;
        this.vStatues = statues;
        this.vDescription = loanDesc;
        this.vRejectedNote = reject_note;
        this.vAmount = loanAmount;
        this.recordId = record_id;
        this.vStartDate=loan_date;

    }
    public vacationClass(String emp_code, String emp_name, String statues, String loan_date, String loanDesc, String reject_note, String loanAmount, int record_id, String Note,String type) {
        this.emp_code = emp_code;
        this.emp_name = emp_name;
        this.vStatues = statues;
        this.vDescription = loanDesc;
        this.vRejectedNote = reject_note;
        this.vAmount = loanAmount;
        this.recordId = record_id;
        this.vStartDate=loan_date;
        this.vSlice=type;
        this.Note=Note;


    }

    public String getNote() {
        return Note;
    }

    public int getRecordId() {
        return recordId;
    }
// for vacation
    public vacationClass(String emp_code, String emp_name, String vStatues, String vStartDate, String vEndDate, String vDescription, String vRejectedNote, String vAmount, int recordId,String vSlice) {
        this.emp_code = emp_code;
        this.emp_name = emp_name;
        this.vStatues = vStatues;
        this.vStartDate = vStartDate;
        this.vEndDate = vEndDate;
        this.vDescription = vDescription;
        this.vRejectedNote = vRejectedNote;
        this.vAmount = vAmount;
        this.recordId = recordId;
        this.vSlice = vSlice;
    }

    public String getvSlice() {
        return vSlice;
    }

    public int getvIsRejected() {
        return vIsRejected;
    }

    public int getvIsused() {
        return vIsUsed;
    }


    public String getEmp_code() {
        return emp_code;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public String getvAmount() {
        return vAmount;
    }

    public String getvStatues() {
        return vStatues;
    }

    public String getvStartDate() {
        return vStartDate;
    }

    public String getvEndDate() {
        return vEndDate;
    }

    public String getvDescription() {
        return vDescription;
    }

    public String getvRejectedDate() {
        return vRejectedDate;
    }

    public String getvRejectedNote() {
        return vRejectedNote;
    }
}
