package com.androidmax.max.hr.Employee.infoPkg;

public class info_exp_class {
    String job_name, job_details, job_years,record_id;

    public info_exp_class(String job_name, String job_details, String job_years, String record_id) {
        this.job_name = job_name;
        this.job_details = job_details;
        this.job_years = job_years;
        this.record_id = record_id;
    }

    public String getRecord_id() {
        return record_id;
    }

    public String getJob_name() {
        return job_name;
    }

    public String getJob_details() {
        return job_details;
    }

    public String getJob_years() {
        return job_years;
    }

    public info_exp_class(String job_name, String job_details, String job_years) {

        this.job_name = job_name;
        this.job_details = job_details;
        this.job_years = job_years;
    }
}
