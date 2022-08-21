package com.androidmax.max.hr.Employee.infoPkg;

public class info_docs_class {
    String doc_Name, docExpireDate, docReleaseDate ; int exp_after;
    String empCode,empName;

    public String getEmpCode() {
        return empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public info_docs_class(String doc_Name, String docExpireDate, String docReleaseDate, int exp_after, String empCode, String empName) {
        this.doc_Name = doc_Name;
        this.docExpireDate = docExpireDate;
        this.docReleaseDate = docReleaseDate;
        this.exp_after = exp_after;
        this.empCode = empCode;
        this.empName = empName;
    }

    public info_docs_class(String doc_Name, String docExpireDate, String docReleaseDate, int exp_after) {
        this.doc_Name = doc_Name;
        this.docExpireDate = docExpireDate;
        this.docReleaseDate = docReleaseDate;
        this.exp_after=exp_after;
    }

    public int getExp_after() {
        return exp_after;
    }

    public String getDoc_Name() {
        return doc_Name;
    }

    public String getDocExpireDate() {
        return docExpireDate;
    }

    public String getDocReleaseDate() {
        return docReleaseDate;
    }
}
