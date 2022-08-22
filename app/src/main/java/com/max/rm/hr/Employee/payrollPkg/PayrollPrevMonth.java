package com.max.rm.hr.Employee.payrollPkg;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.max.rm.hr.Employee.Api;
import com.max.rm.hr.Employee.RequestInterface;
import com.max.rm.hr.Employee.infoPkg.rec_interface;
import com.max.rm.hr.R;
import com.max.rm.hr.keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PayrollPrevMonth extends Fragment{
    RecyclerView list,list2;
    SharedPreferences shared;
    TextView date;
    String org_id, emp_code;
    static boolean all;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.payroll_prev_month,null);
        list=(RecyclerView) v.findViewById(R.id.payrollPrevMonthList);
        shared=getActivity().getSharedPreferences("user",0);
        org_id = shared.getString("org_id", "");
        all= getArguments().getBoolean(keys.allEmployee);
        if(all){
            emp_code="";
        }
        else{
            emp_code = shared.getString("emp_code", "");
        }
        if(getUserVisibleHint()){ // fragment is visible
            getPayroll("Get_Sum_FinanialInfo",emp_code);
        }

        return  v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) { // fragment is visible and have created
            getPayroll("Get_Sum_FinanialInfo",emp_code);
        }
    }

    public void  getPayroll(String function_name, String emp_code){
        Api api= new Api(getActivity());
        DateFormat fMonth = new SimpleDateFormat("M", Locale.ENGLISH);
        DateFormat fYear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        final String  month= fMonth.format(new Date());
        final String  year= fYear.format(new Date());
        Log.d("data",month+"year:"+ year+ "empcode"+emp_code+"org:"+org_id);
        api.PayRoll(function_name, org_id, emp_code, "", year, new RequestInterface() {
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
                    final ArrayList<payrollClass> payrollList= new ArrayList<>();

                    for(int i=0; i <arr.length(); i++){
                        JSONObject ob= arr.getJSONObject(i);
                        int deduction= ob.getInt("DR_AMOUNT");
                        int raise= ob.getInt("CR_AMOUNT");
                        int total= ob.getInt("TOTAL");
                        int years= ob.getInt("YEAR");
                        int months= ob.getInt("MNTH");
                        int emp_code= ob.getInt("EMP_CODE");
                        String emp_name= ob.getString("NM_AR");
                        String date= ob.getString("DT_G");
                    //    int fiscaltype= ob.getInt("FISCALTYPE");
                        payrollList.add( new payrollClass(String.valueOf(emp_code),emp_name,String.valueOf(deduction),String.valueOf(raise),String.valueOf(total),months+"/"+years,String.valueOf(months), String.valueOf(years)));


                    }
                    prevMonthAdapter adapter= new prevMonthAdapter(payrollList, getActivity(), new rec_interface() {
                        @Override
                        public void onRecItemSelected(int position, View view) {
                            details_window(payrollList.get(position).getEmp_code(),payrollList.get(position).getYear(),payrollList.get(position).getMonth(),"Get_Details_FinanialInfo");

                        }
                    });
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
}
