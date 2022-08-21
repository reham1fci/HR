package com.androidmax.max.hr.Employee;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmax.max.hr.Admin.reminderList;
import com.androidmax.max.hr.ChatPkg.Employees;
import com.androidmax.max.hr.ChatPkg.allChats;
import com.androidmax.max.hr.Employee.infoPkg.info_docs;
import com.androidmax.max.hr.Employee.payrollPkg.currentMonthAdapter;
import com.androidmax.max.hr.Employee.payrollPkg.payrollClass;
import com.androidmax.max.hr.R;
import com.androidmax.max.hr.keys;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;



public class NotificationStatsics  extends Fragment {
     TextView expired_doc,expired_doc_num, tasks,tasksNums;
     TextView messagesNums;
    Api api;
    SharedPreferences shared;
    RelativeLayout expired_doc_layout,tasksLayout, messageslayout;
     String emp_code,org_id;
    ProgressBar progress;
    BarChart barChart;
    TextView date;
    RecyclerView list2;
    int month, year;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.notification_statstic,null);
        expired_doc=(TextView)v.findViewById(R.id.expired_doc);
        expired_doc_num=(TextView)v.findViewById(R.id.expired_doc_num);
        tasks=(TextView)v.findViewById(R.id.tasks);
        tasksNums=(TextView)v.findViewById(R.id.tasks_nums);
        messagesNums=(TextView)v.findViewById(R.id.message_num);
        progress=(ProgressBar) v.findViewById(R.id.progress);
        barChart=(BarChart) v.findViewById(R.id.barChart);
        expired_doc_layout=(RelativeLayout) v.findViewById(R.id.expired_doc_layout);
        tasksLayout=(RelativeLayout) v.findViewById(R.id.tasks_layout);
        messageslayout=(RelativeLayout) v.findViewById(R.id.messages_layout);

        intialize();
        getMessagesNums();
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setDescription("");
        barChart.getLegend().setEnabled(false);
        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.getXAxis().setTextSize(15);
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis2.setEnabled(false);
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setGridColor(Color.BLACK);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setTextSize(15);
        getExpiredDocNum();
        tasksLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new reminderList();
                Bundle bundle= new Bundle();
                bundle.putBoolean(keys.allEmployee,false);
                bundle.putString(keys.CLASS_NAME,"Get_Avilable_Reminders");// function name
                bundle.putBoolean(keys.addButton,false);
                f.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.frame,f,"reminderList").addToBackStack("reminderList").commit();

            }
        });
        expired_doc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new info_docs();
                Bundle b= new Bundle();
                b.putString("fun_name","Get_Emp_ExpiredDocument");
                f.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,f,"info_docs").addToBackStack("info_docs").commit();
            }
        });
        messageslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new allChats();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,f,"allChats").addToBackStack("allChats").commit();
            }
        });
        barChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             details_window(emp_code,String.valueOf(year),String.valueOf(month),"Get_Details_FinanialInfo");
            }
        });
        return  v;
    }
     public void get_tasks_numbers(){
         api.getTasks("Get_Avilable_Reminders", org_id, emp_code, new RequestInterface() {
             @Override
             public void onResponse(String response) {
                 getPayroll();
                 String n= response.substring(1,response.length()-1);
                 n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                 Log.d("responseTasks", n);
                 Log.d("responsen", response);
                 //  progress.setVisibility(View.GONE);

                 try {
                     JSONObject ob= new JSONObject(n);
                     JSONArray exp_arr= ob.getJSONArray("DataTable");
                    int task_numbers= exp_arr.length();
                    if(task_numbers>0){

                        tasksNums.setVisibility(View.VISIBLE);
                    tasksNums.setText(String .valueOf(task_numbers));}

                 }
                 catch (JSONException e) {
                     e.printStackTrace();
                 }}

             @Override
             public void onError() {
get_tasks_numbers();
             }
         });

     }

    public void getExpiredDocNum(){
        api.getEmpData(org_id, emp_code, "Get_Emp_ExpiredDocument", new RequestInterface() {
            @Override
            public void onResponse(String response) {
                get_tasks_numbers();
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
                }
            }

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
        emp_code = shared.getString("emp_code", "");

    }

    public  void chartView(){
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getXAxis().setDrawLabels(false);
        // https://stackoverflow.com/questions/31263097/mpandroidchart-hide-background-grid
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setEnabled(false);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setEnabled(false);

        YAxis yAxis2 = barChart.getAxisRight();
        yAxis2.setEnabled(false);

        barChart.setDrawBorders(false);
        barChart.setDrawGridBackground(false);

        ///barChart.getLegend().setEnabled(false);
        // no description text

        // enable touch gestures
        barChart.setTouchEnabled(true);

        // enable scaling and dragging
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        barChart.setPinchZoom(false);
        barChart.setAutoScaleMinMaxEnabled(true);
        // hide legend

    }
    public void  getPayroll(){
        Api api= new Api(getActivity());
        DateFormat fMonth = new SimpleDateFormat("M", Locale.ENGLISH);
        DateFormat fYear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        final String  currentMonth= fMonth.format(new Date());
        final String  CurrentYear= fYear.format(new Date());
       // Log.d("data",month+"year:"+ year+ "empcode"+emp_code+"org:"+org_id);
        api.PayRoll("Get_Sum_FinanialInfo", org_id, emp_code, currentMonth, CurrentYear, new RequestInterface() {
            @Override
            public void onResponse(String response) {
                //  n = n.replaceAll("\\s+","");
                progress.setVisibility(View.GONE);
                Log.d("response",response);
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n              |\\\\|)", "");
                //  n = n.replaceAll("\\s+","");
                // longInfo(n);
                try {
                    JSONObject object= new JSONObject(n);
                    JSONArray arr= object.getJSONArray("DataTable");
                    final ArrayList<payrollClass> payrollList= new ArrayList<>();

                    for(int i=0; i <arr.length(); i++) {
                        JSONObject ob = arr.getJSONObject(i);
                        int deduction = ob.getInt("DR_AMOUNT");
                        int raise = ob.getInt("CR_AMOUNT");
                        int total = ob.getInt("TOTAL");
                         year = ob.getInt("YEAR");
                         month = ob.getInt("MNTH");
                        int emp_code = ob.getInt("EMP_CODE");
                        String emp_name = ob.getString("NM_AR");

                        String date = ob.getString("DT_G");
                        //    int fiscaltype= ob.getInt("FISCALTYPE");
                        ArrayList <BarEntry> list= new ArrayList<>();


                     /* NumberFormat nf=NumberFormat.getInstance(Locale.ENGLISH);
                        String num1=  nf.format(6000);
                        String num2= nf.format(deduction);
                        float num11=Float.parseFloat(num1);
                        float num12=Float.parseFloat(num2);*/
                        list.add(new BarEntry(raise,0));
                        list.add(new BarEntry(deduction,1));
                        BarDataSet dataSet= new BarDataSet(list,"data");
                        dataSet.setBarSpacePercent(50f);
                        int greenColorValue = Color.parseColor("#43bf1d");
                        int redColor = Color.parseColor("#a30924");
                        int[]x= {greenColorValue,redColor};
                        dataSet.setColors(x);
                        ArrayList<String>names= new ArrayList<>();
                        names.add(getString(R.string.Receivables));
                        names.add(getString(R.string.Deduction));
                        BarData barData= new BarData(names,dataSet);
                        barData.setValueTextColor(Color.WHITE);

                        barData.setValueTextSize(15);

                        barChart.setData(barData);
                        barChart.setVisibility(View.VISIBLE);

                    }






                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {
                getPayroll();

            }
        });

    }
    public void details_window(String selected_emp_code ,String year ,String month,String function_name){
        Dialog dialog= new Dialog(getContext());
        dialog.setContentView(R.layout.payroll_current_month);
        list2=(RecyclerView) dialog.findViewById(R.id.payrollCurrentMonthList);
        date=(TextView) dialog.findViewById(R.id.date);
        getPayrollDetails(selected_emp_code,year, month,function_name, dialog);

    }
    public void  getPayrollDetails(String emp_code , final String year , final String month, String function_name, final Dialog d){
        Api api= new Api(getActivity());
        Log.d("data",month+"year:"+ year+ "empcode"+emp_code+"org:"+org_id);
        api.PayRoll(function_name, org_id, emp_code, month, year, new RequestInterface() {
            @Override
            public void onResponse(String response) {
                //  n = n.replaceAll("\\s+","");

                Log.d("response",response);
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                //  n = n.replaceAll("\\s+","");
                // longInfo(n);
                try {
                    JSONObject object= new JSONObject(n);
                    JSONArray arr= object.getJSONArray("DataTable");
                    ArrayList<payrollClass>payrollList= new ArrayList<>();
                    if(arr.length()<=0){
                        Toast.makeText(getActivity(), "no data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        date.setText(month+"/"+year);
                        d.show();

                    }

                    for(int i=0; i <arr.length(); i++){

                        JSONObject ob= arr.getJSONObject(i);
                        int itemAmount= ob.getInt("AMOUNT");
                        String itemName= ob.getString("NMAR");
                        int emp_code= ob.getInt("EMP_CODE");
                        String emp_name= ob.getString("NM_AR");
                        int fiscaltype= ob.getInt("FISCALTYPE");
                        payrollList.add( new payrollClass(String.valueOf(emp_code),emp_name,itemName, String.valueOf(itemAmount),fiscaltype));


                    }
                    currentMonthAdapter adapter= new currentMonthAdapter(payrollList,getActivity());
                    GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 1);
                    list2.setLayoutManager(lLayout);
                    list2.setHasFixedSize(true);
                    list2.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {

            }
        });

    }
    public void getMessagesNums(){
        FirebaseDatabase firebase_instance = FirebaseDatabase.getInstance();
        final DatabaseReference sRef = firebase_instance.getReference("chat");
        final int[] count = {0};

//    Query q= sRef.orderByKey().orderByChild("member_child").orderByKey().equalTo(emp_code);
    sRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
    public  void save_register_key(final DatabaseReference sRef, final String chat_key, final String emp_code){
        sRef.child(chat_key).child("member_child").child(emp_code).child("register_key").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String register_key= dataSnapshot.getValue(String.class);
                SharedPreferences shared= getActivity().getSharedPreferences("user",0);
                String shared_register_key=  shared.getString("register_key","");
                if(register_key.equals("null")||!register_key.equals(shared_register_key)){
                    sRef.child(chat_key).child("member_child").child(emp_code).child("register_key").setValue(shared_register_key);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
