package com.androidmax.max.hr.Employee.attendancePkg;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class attendancePagerAdapter extends FragmentStatePagerAdapter {
    String functionName,functionDailyName;
    public attendancePagerAdapter(FragmentManager fm, String functionName,String functionDailyName) {
        super(fm);
        this.functionName=functionName;
        this.functionDailyName=functionDailyName;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Fragment f1=new AttendanceDaily();
                Bundle bundle1= new Bundle();
                bundle1.putString("fun_name_daily",functionDailyName);
                f1.setArguments(bundle1);
                return  f1;
                case 1:
                Fragment f=new AttendanceMonthly();
                Bundle bundle= new Bundle();
                bundle.putString("fun_name",functionName);
                f.setArguments(bundle);
                return  f;

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
