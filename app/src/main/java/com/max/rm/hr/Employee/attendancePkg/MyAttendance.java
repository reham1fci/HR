package com.max.rm.hr.Employee.attendancePkg;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.max.rm.hr.R;

public class MyAttendance extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.attendance,null);
        TabLayout tab= (TabLayout)v.findViewById(R.id.tab);
        getActivity().setTitle(getString(R.string.attendance));
        Bundle bundle= getArguments();
        String functionName=bundle.getString("fun_name");
        String functionDailyName=bundle.getString("fun_name_daily");

        final ViewPager pager= (ViewPager)v.findViewById(R.id.pager);
        tab.addTab(tab.newTab().setText(getString(R.string.attendance_daily)));
        tab.addTab(tab.newTab().setText(getString(R.string.attendance_monthly)));
        attendancePagerAdapter adapter= new attendancePagerAdapter(getFragmentManager(),functionName,functionDailyName);
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
