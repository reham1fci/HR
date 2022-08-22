package com.max.rm.hr.Employee.infoPkg;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class importatnt {
    int qTypeId;
            String qName;

    public importatnt(int qType, String qName) {
        this.qTypeId = qType;
        this.qName = qName;
    }

    public int getqTypeId() {
        return qTypeId;
    }

    public String getqName() {
        return qName;
    }

    public importatnt() {
    }

    public ArrayList<importatnt> qTypes(){
        ArrayList<importatnt> qList= new ArrayList<>();
        qList.add(new importatnt(1,"بكالوريوس 4"));
        qList.add(new importatnt(2,"بكالوريوس 5 سنوات"));
        qList.add(new importatnt(3,"دبلوم"));
        qList.add(new importatnt(4,"ماجستير"));
        qList.add(new importatnt(5,"دكتوراه"));
        qList.add(new importatnt(6,"ثانوية عامة"));
        return qList;
    }
    public static ArrayList<importatnt> workShift(){
        ArrayList<importatnt> qList= new ArrayList<>();
        qList.add(new importatnt(1,"الفترة الصباحية"));
        qList.add(new importatnt(2,"الفترة المسائية"));
        qList.add(new importatnt(3,"الفترة المزدوجة1"));
        qList.add(new importatnt(4,"فترة الجمعة"));
        qList.add(new importatnt(5,"شفت مزدوج مسائي"));
        qList.add(new importatnt(6,"دوام المشرفين ش1"));
        qList.add(new importatnt(7,"دوام المشرفين ش2"));
        return qList;
    }
    public ArrayList<importatnt> getSocialState(){
        ArrayList<importatnt> qList= new ArrayList<>();
        qList.add(new importatnt(1,"عازب"));
        qList.add(new importatnt(2,"متزوج"));
        return qList;
    }
    public ArrayList<importatnt> getCountries(){
        ArrayList<importatnt> qList= new ArrayList<>();
        qList.add(new importatnt(1,"المملكة العربية السعودبة"));
        qList.add(new importatnt(2,"الجمهورية اليمنية"));
        qList.add(new importatnt(3,"جمهورية مصر العربية"));
        qList.add(new importatnt(4,"جمهورية السودان"));
        return qList;
    }
  public static HashMap<String,String> Get_QualificationTypes(){
      HashMap<String,String> qMap= new HashMap<>();
      qMap.put("1","بكالوريوس 4");
      qMap.put("2","بكالوريوس 5 سنوات");
      qMap.put("3","دبلوم");
      qMap.put("4","ماجستير");
      qMap.put("5","دكتوراه");
      qMap.put("6","ثانوية عامة");
      return qMap;
  }
    public static   void check( String text, TextView v){
        if (text.equals("null")){
            v.setText("");

        }
        else {
            v.setText(text);

        }
    }
}
