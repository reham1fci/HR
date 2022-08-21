package com.androidmax.max.hr.Employee.infoPkg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidmax.max.hr.R;

public class MyInformation extends Fragment {

    TextView personal,job,archive,qualification,experiance,docs,
            financial,family,government,edit_login;
    FragmentTransaction transaction;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.my_information,null);
        getActivity().setTitle(getString(R.string.info));

        personal=(TextView) v.findViewById(R.id.personalBtn);
                job=(TextView) v.findViewById(R.id.job);
        archive=(TextView) v.findViewById(R.id.archive);
        qualification=(TextView) v.findViewById(R.id.qualification);
        experiance=(TextView) v.findViewById(R.id.exp);
        docs=(TextView) v.findViewById(R.id.docs);
        financial=(TextView) v.findViewById(R.id.financial);
        family=(TextView) v.findViewById(R.id.family);
        government=(TextView) v.findViewById(R.id.government);
        edit_login=(TextView) v.findViewById(R.id.edit_login);
        transaction=getActivity().getSupportFragmentManager().beginTransaction();
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new info_personal();
                transaction.replace(R.id.frame,f,"info_personal").addToBackStack("info_personal").commit();
            }
        });
        job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new info_job();
                transaction.replace(R.id.frame,f,"info_job").addToBackStack("info_job").commit();
            }
        });
        archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new info_archive();
                transaction.replace(R.id.frame,f,"info_archive").addToBackStack("info_archive").commit();
            }
        });
        qualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new info_qualification();
                transaction.replace(R.id.frame,f,"info_qualification").addToBackStack("info_qualification").commit();
            }
        });
        experiance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new info_experiance();
                transaction.replace(R.id.frame,f,"info_experiance").addToBackStack("info_experiance").commit();
            }
        });
        family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new info_family();
                transaction.replace(R.id.frame,f,"info_family").addToBackStack("info_family").commit();
            }
        });
        docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new info_docs();
                Bundle b= new Bundle();
                b.putString("fun_name","Get_Emp_DocumentData");
                f.setArguments(b);
                transaction.replace(R.id.frame,f,"info_docs").addToBackStack("info_docs").commit();
            }
        });
        edit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new info_editLogin();
                transaction.replace(R.id.frame,f,"info_editLogin").addToBackStack("info_editLogin").commit();
            }
        });
        financial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new info_financial();
                transaction.replace(R.id.frame,f,"info_financial").addToBackStack("info_financial").commit();
            }
        });
        government.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new info_government();
                transaction.replace(R.id.frame,f,"info_government").addToBackStack("info_government").commit();
            }
        });

        return  v;
    }
}
