package com.androidmax.max.hr.Employee.payrollPkg;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.androidmax.max.hr.keys;

public class payrollPagerAdapter extends FragmentStatePagerAdapter {
 boolean allEmp;
    public payrollPagerAdapter(FragmentManager fm, boolean all_emp) {
        super(fm);
        allEmp=all_emp;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Fragment f1=new PayrollCurrentMonth();
                Bundle bundle1= new Bundle();
                bundle1.putBoolean(keys.allEmployee,allEmp);
                f1.setArguments(bundle1);
                return  f1;
            case 1:
                Fragment f2=new PayrollPrevMonth();
                Bundle bundle2= new Bundle();
                bundle2.putBoolean(keys.allEmployee,allEmp);
                f2.setArguments(bundle2);
                return  f2;
        }
        return null;    }

    @Override
    public int getCount() {
        return 2;
    }
}
