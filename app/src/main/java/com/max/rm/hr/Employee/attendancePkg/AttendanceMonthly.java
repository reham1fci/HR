package com.max.rm.hr.Employee.attendancePkg;

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

import com.max.rm.hr.Employee.Api;
import com.max.rm.hr.Employee.RequestInterface;
import com.max.rm.hr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AttendanceMonthly extends Fragment{
    RecyclerView attendRc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.attendance_monthly,null);
        attendRc=(RecyclerView)v.findViewById(R.id.attendanceMonthlyList);
        Bundle bundle= getArguments();
        String functionName=bundle.getString("fun_name");
        getAttendanceMonthly(functionName);
        return  v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void getAttendanceMonthly(String fun_name){
        Api api= new Api(getActivity());
        SharedPreferences shared= getActivity().getSharedPreferences("user",0);
        String org_id=shared.getString("org_id","");
        String emp_code=shared.getString("emp_code","");
        api.getMonthlyAttendance( emp_code,org_id,fun_name,  new RequestInterface() {
            @Override
            public void onResponse(String response) {
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                //  n = n.replaceAll("\\s+","");
                Log.d("response",n);
                try {
                    JSONObject object= new JSONObject(n);
                    JSONArray arr= object.getJSONArray("AT_MONTHLYATTEND");
                    ArrayList<attendanceMonthly_class> attend= new ArrayList<>();
                    for(int i=0;i<arr.length();i++){
                        JSONObject attend_object= arr.getJSONObject(i);
                        int year=attend_object.getInt("YEAR");
                        int month=attend_object.getInt("MNTH");
                        String workHour=attend_object.getString("WORKEDH");
                        String requiredWorkHour=attend_object.getString("REQUESTEDH");
                        String absenceHour=attend_object.getString("ABSENCEH");
                        String lateHour=attend_object.getString("LATEH");
                        String checkoutEarlyHour=attend_object.getString("EARLYH");
                        String permissionHour=attend_object.getString("PERMISSIONH");
                        String overtimeHour=attend_object.getString("OVERTIMEH");
                        int empCode= attend_object.getInt("EMP_CODE");
                        String empName= attend_object.getString("NM_AR");
                        attend.add( new attendanceMonthly_class(year,month,workHour,requiredWorkHour,absenceHour,lateHour,checkoutEarlyHour,permissionHour,overtimeHour, String.valueOf(empCode), empName));
                    }
                    attendancemonthlyAdapter adapter= new attendancemonthlyAdapter(attend,getActivity());
                    GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 1);
                    attendRc.setLayoutManager(lLayout);
                    attendRc.setHasFixedSize(true);
                    attendRc.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("attendance",n);
            }

            @Override
            public void onError() {
                Log.d("attendance","eror");

            }
        });




    }
}
