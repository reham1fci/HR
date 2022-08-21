package com.androidmax.max.hr.Employee.payrollPkg;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidmax.max.hr.R;
import com.androidmax.max.hr.keys;

public class MyPayroll extends Fragment {
    boolean all;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v=inflater.inflate(R.layout.payroll,null);
            TabLayout tab= (TabLayout)v.findViewById(R.id.tab);
            final ViewPager pager= (ViewPager)v.findViewById(R.id.pager);
            tab.addTab(tab.newTab().setText(getResources().getString(R.string.payroll_current_month)));
            tab.addTab(tab.newTab().setText(getResources().getString(R.string.payroll_prev_month)));
            Bundle b= getArguments();
            getActivity().setTitle(getString(R.string.payroll));

            all=b.getBoolean(keys.allEmployee);
            payrollPagerAdapter adapter= new payrollPagerAdapter(getFragmentManager(), all);
            pager.setAdapter(adapter);
            pager.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener(tab));
            tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    pager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            return  v;
        }
}
