package com.max.rm.hr.Employee.requestpkg;

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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.max.rm.hr.Employee.Api;
import com.max.rm.hr.Employee.RequestInterface;
import com.max.rm.hr.Employee.infoPkg.importatnt;
import com.max.rm.hr.R;
import com.max.rm.hr.keys;
import com.max.rm.hr.normalWindow;
import com.jaredrummler.android.device.DeviceName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class req_another extends Fragment {
    EditText title, body,note, refrence;
    Spinner other_type;
    Button sendRequest;
    Api api;
    String text="اختر نوع الطلب ";
    SharedPreferences shared;
    ProgressBar progress;
    String emp_code,org_id,function_name,record_id,mobileModel;
    int job_dgr;
            String otherType_code;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.req_another,null);
        title=(EditText)v.findViewById(R.id.other_title);
        body=(EditText)v.findViewById(R.id.other_body);
        progress=(ProgressBar) v.findViewById(R.id.progress);
        note=(EditText)v.findViewById(R.id.other_note);
        refrence=(EditText)v.findViewById(R.id.other_reference);
        other_type=(Spinner) v.findViewById(R.id.other_types);
        sendRequest=(Button) v.findViewById(R.id.send);
        Bundle bundle= getArguments();
        function_name= bundle.getString(keys.CLASS_NAME);
        if(function_name.equals("Edit_RQ_Other")){
            vacationClass vactionObj=(vacationClass)bundle.getSerializable(keys.LOAN);
            title.setText(vactionObj.getvAmount());
            body.setText(vactionObj.getvDescription());
            refrence.setText(vactionObj.getvDescription());
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
                sendLoanRequest(record_id,function_name);
            }
        });



        init();
        getOtherTypeList();
        return v;

    }
    public  void sendLoanRequest(final String record_id, final String function_name){
        final String DescriptionTxt=body.getText().toString();
        String NoteTxt=note.getText().toString();
        final String titleTxt=title.getText().toString();
        final String referenceTxt=refrence.getText().toString();
          if(titleTxt.isEmpty()){
            title.setError(getString(R.string.enter)+getString(R.string.loan_amount));
        }
      else  if( DescriptionTxt.isEmpty()){
            body.setError(getString(R.string.enter)+getString(R.string.description));
        }


       /*  else if(loan_code==0){
            normalWindow.window(getString(R.string.enter)+getString(R.string.loan_type),getActivity());
        }*/
        else {
            if (NoteTxt.isEmpty()) {
                NoteTxt = "null";
            }
            final String finalNoteTxt = NoteTxt;
            DeviceName.with(getActivity()).request(new DeviceName.Callback() {
                @Override
                public void onFinished(DeviceName.DeviceInfo info, Exception error) {
                    String manufacturer = info.manufacturer;  // "Samsung"
                    String name = info.marketName;            // "Galaxy S8+"
                    String model = info.model;                // "SM-G955W"
                    String codename = info.codename;          // "dream2qltecan"
                    String deviceName = info.getName();       // "Galaxy S8+"
                    mobileModel="Mobile App:"+manufacturer+" "+deviceName;
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    String currentDate = df.format(new Date());
                  String macAddress=  getMacAddr();
                    Log.d("loan_code", String.valueOf(otherType_code));
                    Log.d("macAddress", String.valueOf(macAddress));
                    api.addOtherRequest(record_id, org_id, emp_code, function_name,otherType_code, finalNoteTxt, currentDate, DescriptionTxt, titleTxt, referenceTxt, mobileModel,macAddress,new RequestInterface() {
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
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }
    public void  getOtherTypeList(){

        final ArrayList<importatnt>TypeList= new ArrayList<>();
        final ArrayAdapter<String>adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);
        api.getTypes(org_id, "Get_OtherTypes","null", "null","null","null","null",new RequestInterface() {
            @Override
            public void onResponse(String response) {
                String n = response.substring(1, response.length() - 1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                Log.d("othertype",n);

                Log.d("othertype",n);
                try {
                    JSONObject ob= new JSONObject(n);
                    JSONArray typesArray=ob.getJSONArray("DataTable");
                    //adapter.add("اختر نوع الطلب ");
                    for(int i=0; i<typesArray.length();i++){
                        JSONObject loanObject= typesArray.getJSONObject(i);
                        int code=loanObject.getInt("ITEM_CODE");
                       String Name=loanObject.getString("ITEM_NMAR");
                        TypeList.add(new importatnt(code,Name));
                        adapter.add(Name);

                    }
                    progress.setVisibility(View.GONE);
                   other_type.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError() {

            }
        });
        other_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                otherType_code=    TypeList.get(i).getqName();
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
}
