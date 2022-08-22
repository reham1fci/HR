package com.max.rm.hr.Employee.infoPkg;

public class info_family_class {
    String familyRelation, ExpireDate, ReleaseDate ;

    public info_family_class(String familyRelation, String expireDate, String releaseDate) {
        this.familyRelation = familyRelation;
        ExpireDate = expireDate;
        ReleaseDate = releaseDate;
    }

    public String getFamilyRelation() {
        return familyRelation;
    }

    public String getExpireDate() {
        return ExpireDate;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }
}
