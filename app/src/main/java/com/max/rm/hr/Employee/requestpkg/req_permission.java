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
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidmax.max.hr.Employee.Api;
import com.androidmax.max.hr.Employee.RequestInterface;
import com.androidmax.max.hr.Employee.infoPkg.importatnt;
import com.androidmax.max.hr.R;
import com.androidmax.max.hr.keys;
import com.androidmax.max.hr.normalWindow;
import com.jaredrummler.android.device.DeviceName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class req_permission extends Fragment {
    TextView period, date;
    String work_shift="اختر فترات الدوام";
    EditText description,note;
    Spinner permission_type;
    Button sendRequest;
    Api api;
    ProgressBar progress;
    SharedPreferences shared;
    String emp_code,org_id,function_name,record_id,mobileModel;
    int job_dgr,permission_code;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.req_permission,null);
        period=(TextView)v.findViewById(R.id.period);
        date=(TextView)v.findViewById(R.id.date);
        progress=(ProgressBar) v.findViewById(R.id.progress) ;

        permission_type=(Spinner) v.findViewById(R.id.permission_type);
       // loan_amount=(EditText)v.findViewById(R.id.loan_amount);
        description=(EditText)v.findViewById(R.id.desc);
        note=(EditText)v.findViewById(R.id.note);
        sendRequest=(Button) v.findViewById(R.id.send);
        Bundle bundle= getArguments();
        function_name= bundle.getString(keys.CLASS_NAME);
        if(function_name.equals("Edit_RQ_Permission")){
            vacationClass vactionObj=(vacationClass)bundle.getSerializable(keys.PERMISSION);
            date.setText(String.valueOf(vactionObj.getvStartDate()));
            description.setText(vactionObj.getvDescription());
            //  importatnt.check(vactionObj.get);
            //   v_note.setText(vactionObj.getvDescription());

            sendRequest.setText(getString(R.string.save));
            record_id=String.valueOf(vactionObj.getRecordId());

        }
        else{
            record_id="null";
        }
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPermissionRequest(record_id,function_name);
            }
        });



        init();
        getPermissionTypeList();
        period.setText(work_shift);
        period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWorkShift();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(date);
            }
        });
        return v;
    }
    public void getWorkShift(){
         final Dialog d = new Dialog(getActivity());
         d.setContentView(R.layout.work_shift);
         final ListView list= (ListView)d.findViewById(R.id.list);
         final ProgressBar progress2= (ProgressBar)d.findViewById(R.id.progress);
         final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_checked);
         Button done=(Button)d.findViewById(R.id.done);
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
                 work_shift=work;
                 Log.d("shifft",work);
                 Log.d("len", String.valueOf(work.length()));
                 period.setText(p);
             }
         });
         d.show();

     }
    public void  getPermissionTypeList(){

        final ArrayList<importatnt>PermissionTypeList= new ArrayList<>();
        final ArrayAdapter<String>adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);
        api.getTypes(org_id, "Get_PermissionTypes", String.valueOf(job_dgr), "null","null","null","null",new RequestInterface() {
            @Override
            public void onResponse(String response) {
                String n = response.substring(1, response.length() - 1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                Log.d("perm",n);
                try {
                    JSONObject ob= new JSONObject(n);
                    JSONArray typesArray=ob.getJSONArray("DataTable");
                    for(int i=0; i<typesArray.length();i++){
                        JSONObject loanObject= typesArray.getJSONObject(i);
                        int permissionCode=loanObject.getInt("ITEM_CODE");
                        String permissionName=loanObject.getString("ITEM_NMAR");
                        PermissionTypeList .add(new importatnt(permissionCode,permissionName));
                        adapter.add(permissionName);

                    }
                    progress.setVisibility(View.GONE);
                    permission_type.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError() {

            }
        });
        permission_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                permission_code=     PermissionTypeList.get(i).getqTypeId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    public  void init(){
        api = new Api(getActivity());
        shared = getActivity().getSharedPreferences("user", 0);
        org_id = shared.getString("org_id", "");
        emp_code=shared.getString("emp_code","");
        job_dgr=shared.getInt("jobDegree",0);


    }
    public  void sendPermissionRequest(final String record_id, final String function_name){
        final String DescriptionTxt=description.getText().toString();
        String NoteTxt=note.getText().toString();
        final String dateTxt=date.getText().toString();
        if( DescriptionTxt.isEmpty()){
            description.setError(getString(R.string.enter)+getString(R.string.description));
        }
        else if(dateTxt.isEmpty()){
            date.setError(getString(R.string.enter)+getString(R.string.date));
        }
        else if(work_shift.equals("اختر فترات الدوام")){
            period.setError(getString(R.string.enter)+getString(R.string.period));
        }
       /*  else if(loan_code==0){
            normalWindow.window(getString(R.string.enter)+getString(R.string.loan_type),getActivity());
        }*/
        else {
            if (NoteTxt.isEmpty()) {
                NoteTxt = "null";
            }

          //  Log.d("loan_code", String.valueOf(loan_code));
        final String finalNoteTxt1 = NoteTxt;
            DeviceName.with(getActivity()).request(new DeviceName.Callback() {
                @Override
                public void onFinished(DeviceName.DeviceInfo info, Exception error) {
                    String manufacturer = info.manufacturer;  // "Samsung"
                    String name = info.marketName;            // "Galaxy S8+"
                    String model = info.model;                // "SM-G955W"
                    String codename = info.codename;          // "dream2qltecan"
                    String deviceName = info.getName();       // "Galaxy S8+"
                    mobileModel="Mobile App:"+manufacturer+" "+deviceName;
                    api.addPermissionRequest(record_id, org_id, emp_code, function_name, String.valueOf(permission_code), work_shift, finalNoteTxt1, dateTxt, DescriptionTxt, mobileModel,new RequestInterface() {
                        @Override
                        public void onResponse(String response) {
                            String n = response.substring(1, response.length() - 1);
                            n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                            Log.d("loantype",n);
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
            });

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
     }}

