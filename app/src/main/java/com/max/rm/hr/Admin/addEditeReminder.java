package com.max.rm.hr.Admin;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
 import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import com.max.rm.hr.Employee.Api;
import com.max.rm.hr.Employee.RequestInterface;
import com.max.rm.hr.R;
import com.max.rm.hr.keys;
import com.max.rm.hr.normalWindow;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class addEditeReminder  extends Fragment implements AdapterView.OnItemClickListener {
    EditText title, description;
    TextView employees,date, time;
    CheckBox repeat_box;
    Api api;
    int  isRepeat=0;
    SharedPreferences shared;
    String admin_emp_code,org_id, taskTitleStr,taskDescriptionStr,empCodesStr;
    String Time="",Date="";
    String function_name,record_id;
    ListView list;
    int inTime=0,inDate=0;
    Button save;
    ProgressBar progress;
    ArrayList<String>emp_codes;
    ArrayList<String>selectEmp_codes;
    RadioGroup radio_group;
    RadioButton timeRadio, dateRadio;
    LinearLayout repeat_layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.task_add,null);
        title=(EditText) v.findViewById(R.id.title);
        description=(EditText) v.findViewById(R.id.desc);
        progress=(ProgressBar) v.findViewById(R.id.progress);
        date=(TextView) v.findViewById(R.id.date);
        employees=(TextView) v.findViewById(R.id.employess);
        time=(TextView) v.findViewById(R.id.time);
        repeat_box=(CheckBox)v.findViewById(R.id.repeat);
        save=(Button)v.findViewById(R.id.save);
        repeat_layout=(LinearLayout)v.findViewById(R.id.repeatLayout);
        radio_group=( RadioGroup) v.findViewById(R.id.radioGroup);
        dateRadio=( RadioButton) v.findViewById(R.id.radioDate);
        timeRadio=( RadioButton) v.findViewById(R.id.radioTime);
        radio_group=( RadioGroup) v.findViewById(R.id.radioGroup);
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.radioDate){
                    date.setVisibility(View.VISIBLE);
                    time.setVisibility(View.GONE);
                    repeat_layout.setVisibility(View.GONE);
                    Time="";
                }
                else{
                    Date="";
                    date.setVisibility(View.GONE);

                    time.setVisibility(View.VISIBLE);
                   repeat_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        api = new Api(getActivity());
        shared = getActivity().getSharedPreferences("user", 0);
        org_id = shared.getString("org_id", "");
        admin_emp_code=shared.getString("admin_emp_code","");
        Bundle bundle= getArguments();
        function_name= bundle.getString(keys.CLASS_NAME);
        if(function_name.equals("Edit_Reminder")){
           reminderClass taskObject=(reminderClass) bundle.getSerializable(keys.TASK);
            title.setText(taskObject.getTaskTitle());
            description.setText(taskObject.getTaskDescription());


            if(Date.equals("null")){
                time.setText(taskObject.getTime());
                Time=taskObject.getTime();
                Date="";
                date.setVisibility(View.GONE);
                timeRadio.setChecked(true);
                time.setVisibility(View.VISIBLE);
                repeat_layout.setVisibility(View.VISIBLE);
            }
            else{
                date.setText(taskObject.getDate());
                Date=taskObject.getDate();
                date.setVisibility(View.VISIBLE);
                time.setVisibility(View.GONE);
                dateRadio.setChecked(true);

                repeat_layout.setVisibility(View.GONE);
                Time="";
            }
            record_id=String.valueOf(taskObject.getTask_id());
            getTaskDetails(record_id);
            int repeat= taskObject.getIsRepeat();
            if(repeat==1){
                repeat_box.setChecked(true);
                isRepeat=repeat;
            }
        }
        else{
            record_id="null";
        }
        employees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEmpList();

            }
        });
        repeat_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isRepeat=1;
                }
                else {
                    isRepeat=0;
                }

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             boolean check=   checkData();
             if(check){
                 addEditTask(function_name,record_id);
             }
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getTime();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDate();

            }
        });
        return  v;

    }
    public void getTime() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.time);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        Button dialogButton = (Button) dialog.findViewById(R.id.done);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                Time=   getTime(hour,minute);
             //  Time =  String.valueOf(hour) + ":" + String.valueOf(minute);
                dialog.dismiss();
                time.setText(Time);
            }

        });
        dialog.show();

    }
    public void getDate(){
            final Dialog d = new Dialog(getActivity());
            d.setContentView(R.layout.date);
            final CalendarView calendarView = (CalendarView) d.findViewById(R.id.calendarView);
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String sDay=String.valueOf(dayOfMonth);
                    String sMonth=String.valueOf(month+1);
                    if (sDay.length() == 1)
                        sDay = "0" + sDay;

                    if (sMonth.length() == 1)
                        sMonth = "0" +  sMonth;
                  Date= sDay + "/" +sMonth+ "/" + String.valueOf(year);
                  date.setText(Date);
                  d.dismiss();
                }

            });
        d.show();
    }
    public void addEditTask(String function_name,String record_ird){
Log.d("intime", String.valueOf(inTime));
Log.d("indate", String.valueOf(inDate));
        api.addEditTask(function_name, org_id, record_ird, taskTitleStr, taskDescriptionStr,Date, Time,String.valueOf(inTime), String.valueOf(inDate),isRepeat, empCodesStr, admin_emp_code, new RequestInterface() {
          @Override
          public void onResponse(String response) {
              String n= response.substring(1,response.length()-1);
              n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
              Log.d("addresponse", n);
              try {
                  JSONObject object= new JSONObject(n);
                  String msg=object.getString("Msg");

                  if(msg.equals("S00  uccess")){
                      normalWindow.window(getString(R.string.req_done),getActivity());
                      getActivity().getSupportFragmentManager().popBackStackImmediate();
                  }
              } catch (JSONException e) {
                  e.printStackTrace();
              }

          }

          @Override
          public void onError() {

          }
      });



    }
    public void getEmpList(){
        emp_codes= new ArrayList<>();
        selectEmp_codes= new ArrayList<>();
        progress.setVisibility(View.VISIBLE);
        employees.setVisibility(View.GONE);
        final Dialog d = new Dialog(getActivity());
        d.setContentView(R.layout.work_shift);
        Button ok= (Button)d.findViewById(R.id.done);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                empCodesStr="";
                for(int i=0; i<selectEmp_codes.size();i++){
                    if(i==selectEmp_codes.size()-1){
                        empCodesStr=empCodesStr+selectEmp_codes.get(i);

                    }
                    else {
                        empCodesStr=empCodesStr+selectEmp_codes.get(i)+",";
                    }
                }
                Log.d("employee", empCodesStr);
                employees.setText(empCodesStr);
                d.dismiss();
            }
        });
        list= (ListView)d.findViewById(R.id.list);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list.setOnItemClickListener(this);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_checked);

        api.getAllEmployeesData(org_id, "Get_Emp_UseWebGate", new RequestInterface() {
            @Override
            public void onResponse(String response) {
                progress.setVisibility(View.GONE);
                employees.setVisibility(View.VISIBLE);

                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                Log.d("employees", n);

                try {
                    JSONObject ob = new JSONObject(n);
                    JSONArray docs_arr = ob.getJSONArray("PersonalData");
                    for( int i=0; i<docs_arr.length(); i++){
                        JSONObject empObj=docs_arr.getJSONObject(i);
                        String empName=empObj.getString("NM_AR");
                        int empCode=empObj.getInt("EMP_CODE");
                        adapter.add(empName+" "+empCode);
                        emp_codes.add(String.valueOf(empCode));

                    }
                     list.setAdapter(adapter);
                     d.show();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CheckedTextView item = (CheckedTextView) view;
        if (item.isChecked()){
            selectEmp_codes.add(emp_codes.get(i));

        }else {
            selectEmp_codes.remove(emp_codes.get(i));

        }
    }
    public boolean checkData(){
taskTitleStr=title.getText().toString();
taskDescriptionStr =description.getText().toString();
if(taskTitleStr.isEmpty()){
    title.setError(getString(R.string.enter)+getString(R.string.title));
    return false;
}
else  if(taskDescriptionStr.isEmpty()){
    description.setError(getString(R.string.enter)+getString(R.string.description));
    return false;

}

else if(empCodesStr.equals("")){
    employees.setError(getString(R.string.enter)+getString(R.string.employees));
    return false;
}
else {
    if(!Date.equals("")){
        inDate=1;
    }
    if(!Time.equals("")){
        inTime=1;
    }
    return true;
}
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
    private String getTime(int hr,int min) {
        java.sql.Time tme = new  java.sql.Time(hr,min,0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(tme);
    }
}
