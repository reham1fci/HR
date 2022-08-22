package com.max.rm.hr.Employee.attendancePkg;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.max.rm.hr.Employee.Api;
import com.max.rm.hr.Employee.RequestInterface;
import com.max.rm.hr.Employee.infoPkg.importatnt;
import com.max.rm.hr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AttendanceDaily extends Fragment {
    RecyclerView attendRc;
    TextView from, to,period;
    Button search;
    String fromDate,toDate,  work_shift;
    ProgressBar progress;
    String functionName;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.attendance_daily,null);
        attendRc=(RecyclerView)v.findViewById(R.id.attendanceDailyList);
         progress = (ProgressBar)v. findViewById(R.id.progress);
        Bundle bundle= getArguments();
         functionName=bundle.getString("fun_name_daily");
        search=(Button) v.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                getAttendanceDaily(fromDate, toDate,work_shift,functionName );

            }
        });
        from=(TextView)v.findViewById(R.id.from);
        to=(TextView)v.findViewById(R.id.to);
        period=(TextView)v.findViewById(R.id.period);
       // Locale locale = new Locale("ar", "AE");

       // getAttendanceMonthly(functionName);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
         toDate= df.format(new Date());
         if(functionName.equals("Get_All_DailyAttend")){
             fromDate=toDate;
         }
         else{
             fromDate="01"+toDate.substring(2);
         }
       to.setText(toDate);
       from.setText(fromDate);
       period.setText("الكل");
        work_shift="null";
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d = new Dialog(getActivity());
                d.setContentView(R.layout.date);
                CalendarView calendarView = (CalendarView) d.findViewById(R.id.calendarView);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                        fromDate = String.valueOf(dayOfMonth) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year);
                        //Log.d("date",date_str);
                        from.setText(fromDate);
                    }

                });
                Button done= (Button)d.findViewById(R.id.done);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
                d.show();

            } });
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d = new Dialog(getActivity());
                d.setContentView(R.layout.date);
                CalendarView calendarView = (CalendarView) d.findViewById(R.id.calendarView);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                        toDate = String.valueOf(dayOfMonth) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year);
                        //Log.d("date",date_str);
                        to.setText(toDate);
                    }

                });
                Button done= (Button)d.findViewById(R.id.done);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
                d.show();

            } });
        period.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                             getWorkShift();

                                              }
                                          });
     //   getworkperiod();
        getAttendanceDaily(fromDate, toDate,work_shift, functionName);
        return  v;
    }
    public void getAttendanceDaily(String startDate, String endDate, String workPeriod,String functionName){
        Api api= new Api(getActivity());
        SharedPreferences shared= getActivity().getSharedPreferences("user",0);
        String org_id=shared.getString("org_id","");
        final String emp_code=shared.getString("emp_code","");
        api.getDailyAttendance(startDate, endDate, workPeriod, emp_code, org_id,  functionName,new RequestInterface() {
            @Override
            public void onResponse(String response) {
                progress.setVisibility(View.GONE);
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                //  n = n.replaceAll("\\s+","");
                //longInfo(n);
                try {
                    JSONObject object= new JSONObject(n);
                    JSONArray  arr= object.getJSONArray("VW_DAILYATTEND");
                    ArrayList<attendanceDaily_class>attend= new ArrayList<>();
                    if(arr.length()<=0){
                        Toast.makeText(getActivity(), "No Data Recorded ", Toast.LENGTH_LONG).show();

                    }
                    for(int i=0;i<arr.length();i++){
                        JSONObject attend_object= arr.getJSONObject(i);
                        String day=attend_object.getString("DAYAR");
                        String date=attend_object.getString("DT_G");
                        String state=attend_object.getString("STATE_ARTXT");
                        String checkIn=attend_object.getString("CHECK_IN");
                        String checkOut=attend_object.getString("CHECK_OUT");
                        String period=attend_object.getString("W_TITLE");
                        int empCode= attend_object.getInt("EMP_CODE");
                        String empName= attend_object.getString("NM_AR");

                        attend.add( new attendanceDaily_class(date,day,checkIn,checkOut,state,period,String.valueOf(empCode),empName));
                    }
                    //longInfo(attend.toString());
                    attendanceDailyAdapter adapter= new attendanceDailyAdapter(attend,getActivity());
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
                progress.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "loading error please try again", Toast.LENGTH_LONG).show();

            }
        });




    }

    public static void longInfo(String str) {
        if(str.length() > 4000) {
            Log.i("taggs", str.substring(0, 4000));
            //longInfo(str.substring(4000));
        } else
            Log.i("taggs", str);
    }
    public void getWorkShift(){
        Api api= new Api(getActivity());
        SharedPreferences shared= getActivity().getSharedPreferences("user",0);
        String org_id=shared.getString("org_id","");
        final String emp_code=shared.getString("emp_code","");
        final Dialog d = new Dialog(getActivity());
        d.setContentView(R.layout.work_shift);
        final ListView list= (ListView)d.findViewById(R.id.list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_checked);
        Button done=(Button)d.findViewById(R.id.done);
        final ProgressBar progress2= (ProgressBar)d.findViewById(R.id.progress);

        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // final importatnt importatnt= new importatnt();
        final ArrayList<importatnt>wList= new ArrayList<>();
        api.getWorkPeriod(org_id, "Get_All_WorkPeriods","null",new RequestInterface() {
            @Override
            public void onResponse(String response) {
                String n = response.substring(1, response.length() - 1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                Log.d("periods",n);
                try {
                    JSONObject ob= new JSONObject(n);
                    JSONArray typesArray=ob.getJSONArray("AT_WORKSHIFTS");
                    for(int i=0; i<typesArray.length();i++){
                        JSONObject loanObject= typesArray.getJSONObject(i);
                        int permissionCode=loanObject.getInt("W_ID");
                        String permissionName=loanObject.getString("W_TITLE");
                        wList .add(new importatnt(permissionCode,permissionName));
                        adapter.add(permissionName);

                    }
                     progress2.setVisibility(View.GONE);
                    list.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError() {
                // getWorkShift();
            }
        });
        //   final ArrayList<importatnt> wList=importatnt.workShift();
       /*  for(int i = 0; i<wList.size();i++){
             importatnt im=   wList.get(i);
             String shiftName =im.getqName();
             adapter.add(shiftName);
         }
         list.setAdapter(adapter);*/
        final ArrayList<Integer> arr=new ArrayList<>();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView item = (CheckedTextView) view;
                if (item.isChecked()){
                    arr.add(wList.get(i).getqTypeId());

                }else {
                    arr.remove(Integer.valueOf(wList.get(i).getqTypeId()));

                }

            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
                String p="";
                String work =arr.toString();
                for(int i=0;i<arr.size();i++){
                    p=p+wList.get(arr.get(i)-1).getqName()+",";
                }
                work = work.replaceAll("\\s+","");
                work=work.substring(1,work.length()-1);
                work_shift="("+work+")";
                Log.d("shifft",work);
                Log.d("len", String.valueOf(work.length()));
                period.setText(p);
            }
        });
        d.show();

    }

}
