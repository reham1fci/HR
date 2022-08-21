package com.androidmax.max.hr.Employee.payrollPkg;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidmax.max.hr.Employee.Api;
import com.androidmax.max.hr.Employee.RequestInterface;
import com.androidmax.max.hr.R;
import com.androidmax.max.hr.keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PayrollCurrentMonth extends Fragment {
    RecyclerView list;
    SharedPreferences shared;
    TextView date;
    String org_id, emp_code;
  static boolean all;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.payroll_current_month,null);
        list=(RecyclerView) v.findViewById(R.id.payrollCurrentMonthList);
        date=(TextView) v.findViewById(R.id.date);
        shared=getActivity().getSharedPreferences("user",0);
        org_id = shared.getString("org_id", "");
        all= getArguments().getBoolean(keys.allEmployee);
        if(all){
            emp_code="";
        }
        else{
            emp_code = shared.getString("emp_code", "");
        }
         getPayroll("Get_Current_FinanialInfo",emp_code);
        return  v;
    }
    public void  getPayroll(String function_name, String emp_code){
        Api api= new Api(getActivity());
        DateFormat fMonth = new SimpleDateFormat("M", Locale.ENGLISH);
        DateFormat fYear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String  month= fMonth.format(new Date());
        String  year= fYear.format(new Date());
        date.setText(month+"/"+year);
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
                    list.setLayoutManager(lLayout);
                    list.setHasFixedSize(true);
                    list.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {

            }
        });

    }
}
