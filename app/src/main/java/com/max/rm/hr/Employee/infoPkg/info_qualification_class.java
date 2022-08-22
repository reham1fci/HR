package com.max.rm.hr.Employee.infoPkg;

public class info_qualification_class  {
 String   qualification_name,qualification_date,qualification_major,qualification_note,q_id;

    public info_qualification_class(String q_id,String qualification_name, String qualification_date, String qualification_major, String qualification_note) {
        this.qualification_name = qualification_name;
        this.qualification_date = qualification_date;
        this.qualification_major = qualification_major;
        this.qualification_note = qualification_note;
        this.q_id=q_id;
    }

    public String getQ_id() {
        return q_id;
    }

    public String getQualification_name() {
        return qualification_name;
    }


    public String getQualification_date() {
        return qualification_date;
    }

    public String getQualification_major() {
        return qualification_major;
    }

    public String getQualification_note() {
        return qualification_note;
    }
}
