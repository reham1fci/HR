package com.androidmax.max.hr.Admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidmax.max.hr.ChatPkg.Employees;
import com.androidmax.max.hr.ChatPkg.allChats;
import com.androidmax.max.hr.Employee.Api;
import com.androidmax.max.hr.Employee.RequestInterface;
import com.androidmax.max.hr.Employee.infoPkg.info_docs;
import com.androidmax.max.hr.R;
import com.github.mikephil.charting.charts.BarChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class notification extends Fragment {
    TextView expired_doc,expired_doc_num,messagesNums;
    Api api;
    SharedPreferences shared;
    String emp_code,org_id;
    RelativeLayout expired_doc_layout,tasksLayout,messageslayout ;
    ProgressBar progress;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.notification_statstic,null);
        expired_doc=(TextView)v.findViewById(R.id.expired_doc);
        expired_doc_num=(TextView)v.findViewById(R.id.expired_doc_num);
        BarChart   barChart=(BarChart) v.findViewById(R.id.barChart);
        progress=(ProgressBar) v.findViewById(R.id.progress);
        messagesNums=(TextView)v.findViewById(R.id.message_num);
        messageslayout=(RelativeLayout) v.findViewById(R.id.messages_layout);
        expired_doc_layout=(RelativeLayout) v.findViewById(R.id.expired_doc_layout);
        tasksLayout=(RelativeLayout) v.findViewById(R.id.tasks_layout);
        tasksLayout.setVisibility(View.GONE);
        barChart.setVisibility(View.GONE);
        intialize();
        getExpiredDocNum();
        messageslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new allChats();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,f,"allChats").addToBackStack("allChats").commit();
            }
        });
        expired_doc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Fragment f=new info_docs();
                Bundle b= new Bundle();
                 b.putString("fun_name","Get_All_ExpiredDocument");
                f.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,f,"info_docs").addToBackStack("info_docs").commit();

            }
        });
        return  v;
    }
    public void getExpiredDocNum(){
        api.getAllEmployeesData(org_id, "Get_All_ExpiredDocument", new RequestInterface() {
            @Override
            public void onResponse(String response) {
                getMessagesNums();
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                Log.d("responsegover", n);

                try {
                    JSONObject ob = new JSONObject(n);
                    JSONArray docs_arr = ob.getJSONArray("Emp_Documents");
                    int doc_num= docs_arr.length();
                    if(doc_num>0){
                        expired_doc_num.setVisibility(View.VISIBLE);
                    expired_doc_num.setText(String.valueOf(doc_num));}

                }catch (JSONException e) {
                    e.printStackTrace();
                }}

            @Override
            public void onError() {
                getExpiredDocNum();

            }
        });


    }
    public void intialize(){
        getActivity().setTitle(getString(R.string.notifications));
        api = new Api(getActivity());
        shared = getActivity().getSharedPreferences("user", 0);
        org_id = shared.getString("org_id", "");
        emp_code=shared.getString("admin_emp_code","");
    }
    public void getMessagesNums(){
        FirebaseDatabase firebase_instance = FirebaseDatabase.getInstance();
        final DatabaseReference sRef = firebase_instance.getReference("chat");
        final int[] count = {0};

//    Query q= sRef.orderByKey().orderByChild("member_child").orderByKey().equalTo(emp_code);
        sRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progress.setVisibility(View.GONE);
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    for(DataSnapshot dsp2 : dsp.child("member_child").getChildren()) {

                        if( dsp2.getKey().equals(emp_code)){
                            count[0] = count[0] +dsp2.getValue(Employees.class).getCountNotSeenMessages();
                            String register_key= dsp2.getValue(Employees.class).getRegister_key();

                            SharedPreferences shared= getActivity().getSharedPreferences("user",0);
                            String shared_register_key=  shared.getString("register_key","");
                            if(register_key.equals("null")||!register_key.equals(shared_register_key)){
                                sRef.child(dsp.getKey()).child("member_child").child(emp_code).child("register_key").setValue(shared_register_key);

                            }
                        }
                    }
                }
                if (count[0]>0){
                    messagesNums.setVisibility(View.VISIBLE);

                    messagesNums.setText(String.valueOf(count[0]));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
