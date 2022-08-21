package com.androidmax.max.hr.Employee.requestpkg;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmax.max.hr.Employee.Api;
import com.androidmax.max.hr.Employee.RequestInterface;
import com.androidmax.max.hr.Employee.infoPkg.importatnt;
import com.androidmax.max.hr.R;
import com.androidmax.max.hr.keys;
import com.androidmax.max.hr.normalWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Req_Vacation extends Fragment {
    EditText v_dayNums,v_description,v_note;
    TextView startDay,endDay;
    Spinner v_sliceSp;
    ProgressBar progress;
    Button sendRequest;
    Api api;
    SharedPreferences shared;
    String emp_code,org_id;
    int job_dgr,vacation_code;
    String function_name, record_id;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.req_vacation,null);
        v_dayNums=(EditText)v.findViewById(R.id.v_nums) ;
        v_description=(EditText)v.findViewById(R.id.v_desc) ;
        v_note=(EditText)v.findViewById(R.id.v_note) ;
        endDay=(TextView) v.findViewById(R.id.v_end_date) ;
        startDay=(TextView) v.findViewById(R.id.v_start_date) ;
        v_sliceSp=(Spinner) v.findViewById(R.id.v_slice) ;
        sendRequest=(Button) v.findViewById(R.id.send) ;
        progress=(ProgressBar) v.findViewById(R.id.progress) ;
        Bundle bundle= getArguments();
        function_name= bundle.getString(keys.CLASS_NAME);
        if(function_name.equals("Edit_RQ_Vacation")){
      vacationClass vactionObj=(vacationClass)bundle.getSerializable(keys.VACACTION);
            v_dayNums.setText(vactionObj.getvAmount());
            v_description.setText(vactionObj.getvDescription());
          //      importatnt.check(vactionObj.get);
            //   v_note.setText(vactionObj.getvDescription());
            startDay.setText(vactionObj.getvStartDate());
            endDay.setText(vactionObj.getvEndDate());
            sendRequest.setText(getString(R.string.save));
            record_id=String.valueOf(vactionObj.getRecordId());

        }
        else{
            record_id="null";
        }

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVacationRequest(record_id,function_name);
            }
        });
        init();
        getVacationSliceList();
        return v;
    }
    public void  getVacationSliceList(){

        final ArrayList<importatnt> loanTypeList= new ArrayList<>();
        Log.d("emp",emp_code);
        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);
        api.getTypes(org_id, "Get_VacationTypes", String.valueOf(job_dgr),emp_code, "null","null","null",new RequestInterface() {
            @Override
            public void onResponse(String response) {
                String n = response.substring(1, response.length() - 1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                Log.d("responsev",response);

                try {
                    JSONObject ob= new JSONObject(n);
                    JSONArray typesArray=ob.getJSONArray("DataTable");
                    for(int i=0; i<typesArray.length();i++){
                        JSONObject loanObject= typesArray.getJSONObject(i);
                       int loan_code=loanObject.getInt("CODE");
                        String loan_Name=loanObject.getString("NMAR");
                        loanTypeList.add(new importatnt(loan_code,loan_Name));
                        adapter.add(loan_Name);

                    }
                    progress.setVisibility(View.GONE);
                    v_sliceSp.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError() {
                Log.d("responsev","error");

            }
        });
        v_sliceSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vacation_code=     loanTypeList.get(i).getqTypeId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    public  void sendVacationRequest(String record_id,String function_name){
        String v_day_nums_txt=v_dayNums.getText().toString();
        String v_description_txt=v_description.getText().toString();
        String v_note_txt=v_note.getText().toString();
        String v_startDate_txt=startDay.getText().toString();
        String v_endDate_txt=endDay.getText().toString();
        if( v_day_nums_txt.isEmpty()){
            v_dayNums.setError(getString(R.string.enter)+getString(R.string.vacation_day));
        }
        else if(v_description_txt.isEmpty()){
            v_description.setError(getString(R.string.enter)+getString(R.string.description));
        }
        else if(v_endDate_txt.isEmpty()){
            endDay.setError(getString(R.string.enter)+getString(R.string.end_v_date));
        }
        else if(v_startDate_txt.isEmpty()){
            startDay.setError(getString(R.string.enter)+getString(R.string.start_date));
        }
       /*  else if(loan_code==0){
            normalWindow.window(getString(R.string.enter)+getString(R.string.loan_type),getActivity());
        }*/
        else {
            if (v_note_txt.isEmpty()) {
                v_note_txt = "null";
            }
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            String currentDate = df.format(new Date());
            api.addVacationRequest(record_id, org_id, emp_code, function_name, String.valueOf(vacation_code), v_day_nums_txt
                    , currentDate, v_startDate_txt, v_endDate_txt, v_description_txt, v_note_txt, new RequestInterface() {
                        @Override
                        public void onResponse(String response) {
                            String n = response.substring(1, response.length() - 1);
                            n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                            try {
                                JSONObject object= new JSONObject(n);
                                String msg=object.getString("Msg");

                                if(msg.equals("Success")){
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
    }

    public  void init(){
        api = new Api(getActivity());
        shared = getActivity().getSharedPreferences("user", 0);
        org_id = shared.getString("org_id", "");
        emp_code=shared.getString("emp_code","");
        job_dgr=shared.getInt("jobDegree",0);

        startDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String v_day_nums_txt=v_dayNums.getText().toString();
                if(v_day_nums_txt.isEmpty()){
                    Toast.makeText(getActivity(), getString(R.string.enter)+getString(R.string.vacation_day), Toast.LENGTH_SHORT).show();
                }
                else {
            setDate(startDay);
                }
            }
        });
      /*  endDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(endDay);
            }
        });*/
    }
    public String getEndDate(String day, String month, String Year){
        String v_day_nums_txt=v_dayNums.getText().toString();
        int day_numbers= Integer.parseInt(v_day_nums_txt);

        int start_day=Integer.parseInt(day);
        int sum = start_day+day_numbers;
        if(sum<=30){
            return sum+"/"+ month+"/"+Year ;
        }
        else{
            int reminder=sum-30;
            int Month=Integer.parseInt(month)+1;
            return reminder+"/"+ Month+"/"+Year ;


        }

    }
    public void setDate(final TextView tv){
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
                    sMonth = "0" + sMonth;

                String  toDate = sDay + "/" +sMonth+ "/" + String.valueOf(year);

//                String selectedDate = sdf.format(toDate);
                Log.d("date",toDate);
              tv.setText(toDate);
            String end_date=    getEndDate(sDay,sMonth,String.valueOf(year));
            endDay.setText(end_date);
               // to.setText(toDate);
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
    }
}
