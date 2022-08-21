package com.androidmax.max.hr.Employee;

import java.io.Serializable;

public class employee_class  implements Serializable{
    String emp_code;
    int EMP_ID ;
    String BARCODE;
    String emp_name;
    String register_key;
    String nameEn;
    String nationality;
    String Gender;
    String Religion;
    String socialState;
    String BIRTHDATE_G;
    String BIRTHDATE_H;
    String birthCountry;
    String birthCity;
    String Email;
    String Phone;
    String job_name;
    String profileImage;
    String Note;

    public employee_class() {
    }

    public int getJob_degree() {
        return job_degree;
    }

    int job_degree;

    public employee_class(String EMP_CODE, String nameAr, String job_name ,int job_code) {
        this.emp_code = EMP_CODE;
        this.emp_name = nameAr;
        this.job_name = job_name;
        this.job_degree=job_code;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }

    public void setEMP_ID(int EMP_ID) {
        this.EMP_ID = EMP_ID;
    }

    public String getBARCODE() {
        return BARCODE;
    }

    public void setBARCODE(String BARCODE) {
        this.BARCODE = BARCODE;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getRegister_key() {
        return register_key;
    }

    public void setRegister_key(String register_key) {
        this.register_key = register_key;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public void setReligion(String religion) {
        Religion = religion;
    }

    public String getSocialState() {
        return socialState;
    }

    public void setSocialState(String socialState) {
        this.socialState = socialState;
    }

    public void setBIRTHDATE_G(String BIRTHDATE_G) {
        this.BIRTHDATE_G = BIRTHDATE_G;
    }

    public void setBIRTHDATE_H(String BIRTHDATE_H) {
        this.BIRTHDATE_H = BIRTHDATE_H;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setNote(String note) {
        Note = note;
    }

    public void setJob_degree(int job_degree) {
        this.job_degree = job_degree;
    }

    public employee_class(String emp_code, String emp_name, String register_key) {

        this.emp_code = emp_code;
        this.emp_name = emp_name;
        this.register_key = register_key;
    }

    public String getJob_name() {
        return job_name;
    }

    public String getEMP_CODE() {
        return emp_code;
    }

    public int getEMP_ID() {
        return EMP_ID;
    }

    public String getBARCOD() {
        return BARCODE;
    }

    public String getNameAr() {
        return emp_name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getNationality() {
        return nationality;
    }

    public String getGender() {
        return Gender;
    }

    public String getReligion() {
        return Religion;
    }

    public String getSOICALSTATE() {
        return socialState;
    }

    public String getBIRTHDATE_G() {
        return BIRTHDATE_G;
    }

    public String getBIRTHDATE_H() {
        return BIRTHDATE_H;
    }

    public String getBirthCountry() {
        return birthCountry;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhone() {
        return Phone;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getNote() {
        return Note;
    }

}
