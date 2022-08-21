package com.androidmax.max.hr.Admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidmax.max.hr.Employee.Api;
import com.androidmax.max.hr.Employee.RequestInterface;
import com.androidmax.max.hr.R;
import com.androidmax.max.hr.keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class reminderDetails extends Fragment {
    TextView time, date, description, title, employees, date_txt, time_txt;
    CheckBox repeat_box;
    ProgressBar progress;
    Api api;
    int  isRepeat=0;
    SharedPreferences shared;
    String admin_emp_code,org_id,empCodesStr;
    LinearLayout repeat_layout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.task_details,null);
        getActivity().setTitle(getString(R.string.tasks));
        time=(TextView)v.findViewById(R.id.time) ;
        date=(TextView)v.findViewById(R.id.date) ;
        date_txt=(TextView)v.findViewById(R.id.date_txt) ;
        time_txt=(TextView)v.findViewById(R.id.time_txt) ;
        description=(TextView)v.findViewById(R.id.desc) ;
        title=(TextView)v.findViewById(R.id.title) ;
        employees=(TextView)v.findViewById(R.id.employees) ;
        repeat_box=(CheckBox)v.findViewById(R.id.repeat);
        progress=(ProgressBar) v.findViewById(R.id.progress);
        repeat_layout=(LinearLayout)v.findViewById(R.id.repeatLayout);

        api = new Api(getActivity());
        shared = getActivity().getSharedPreferences("user", 0);
        org_id = shared.getString("org_id", "");

        Bundle bundle= getArguments();
        reminderClass taskObject=(reminderClass) bundle.getSerializable(keys.TASK);
        title.setText(taskObject.getTaskTitle());
        description.setText(taskObject.getTaskDescription());
    String Date=taskObject.getDate();

        if(Date.equals("null")){
            time.setText(taskObject.getTime());
date_txt.setVisibility(View.GONE);
            date.setVisibility(View.GONE);
time_txt.setVisibility(View.VISIBLE);

            time.setVisibility(View.VISIBLE);
            repeat_layout.setVisibility(View.VISIBLE);
        }
        else{
            time_txt.setVisibility(View.GONE);
            date_txt.setVisibility(View.VISIBLE);
            date.setText(taskObject.getDate());
            date.setVisibility(View.VISIBLE);
            time.setVisibility(View.GONE);
            repeat_layout.setVisibility(View.GONE);
        }
     String  record_id=String.valueOf(taskObject.getTask_id());
        getTaskDetails(record_id);
        int repeat= taskObject.getIsRepeat();
        if(repeat==1){
            repeat_box.setChecked(true);
            isRepeat=repeat;
        }




        return v;
    }
    public void getTaskDetails(String record_id){
        progress.setVisibility(View.VISIBLE);
        employees.setVisibility(View.GONE);
        api.getTasks("Get_Reminders_Details", org_id,record_id, new RequestInterface() {
            @Override
            public void onResponse(String response) {
                progress.setVisibility(View.GONE);
                employees.setVisibility(View.VISIBLE);
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                Log.d("details", n);
                //  progress.setVisibility(View.GONE);

                try {
                    empCodesStr="";

                    JSONObject ob= new JSONObject(n);
                    JSONArray exp_arr= ob.getJSONArray("DataTable");
                    for( int i =0; i <exp_arr.length();i++){
                        JSONObject object= exp_arr.getJSONObject(i);
                        int emp_code=object.getInt("EMP_CODE");
                        if(i==exp_arr.length()-1){
                            empCodesStr=empCodesStr+emp_code;

                        }
                        else {
                            empCodesStr=empCodesStr+emp_code+",";
                        }
                    }
                    employees.setText(empCodesStr);


                }
                catch (JSONException e) {
                    e.printStackTrace();
                }}


            @Override
            public void onError() {

            }
        });


    }
}
