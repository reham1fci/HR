package com.androidmax.max.hr.Employee.requestpkg;

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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Req_Custody extends Fragment {
    EditText amount,description,note;
    Spinner custody_type;
    Button sendRequest;
    Api api;
    SharedPreferences shared;
    String emp_code,org_id,function_name,record_id, mobileModel;
    int job_dgr,custodyCode;
    ProgressBar progress;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.req_custody,null);
      amount=(EditText)v.findViewById(R.id.amount);
       description=(EditText)v.findViewById(R.id.desc);
        note=(EditText)v.findViewById(R.id.note);
        custody_type=(Spinner) v.findViewById(R.id.custody_type);
        sendRequest=(Button) v.findViewById(R.id.send);
        progress=(ProgressBar) v.findViewById(R.id.progress);

        Bundle bundle= getArguments();
        function_name= bundle.getString(keys.CLASS_NAME);
        if(function_name.equals("Edit_RQ_Custody")){
            vacationClass vactionObj=(vacationClass)bundle.getSerializable(keys.CUSTODY);
            amount.setText(vactionObj.getvAmount());
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
                sendCustodyRequest(record_id,function_name);
            }
        });



        init();
        getCustodyTypeList();
        return v;

    }
    public  void sendCustodyRequest(final String record_id, final String function_name){
        final String DescriptionTxt=description.getText().toString();
        String NoteTxt=note.getText().toString();
        final String AmountTxt=amount.getText().toString();
        if( DescriptionTxt.isEmpty()){
            description.setError(getString(R.string.enter)+getString(R.string.description));
        }
        else if(AmountTxt.isEmpty()){
            amount.setError(getString(R.string.enter)+getString(R.string.amount));
        }
       /*  else if(loan_code==0){
            normalWindow.window(getString(R.string.enter)+getString(R.string.loan_type),getActivity());
        }*/
        else {
            if (NoteTxt.isEmpty()) {
             NoteTxt = "null";
            }
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            final String currentDate = df.format(new Date());
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
                    api.addCustodyRequest(record_id, org_id, emp_code, function_name, String.valueOf(custodyCode), finalNoteTxt, currentDate,DescriptionTxt,AmountTxt, mobileModel,new RequestInterface() {
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

           // Log.d("loan_code", String.valueOf(loan_code));

        }

    }
    public void  getCustodyTypeList(){

        final ArrayList<importatnt> custodyTypeList= new ArrayList<>();
        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);
        api.getTypes(org_id, "Get_CustodyTypes", String.valueOf(job_dgr), "null","null","null","null",new RequestInterface() {
            @Override
            public void onResponse(String response) {
                String n = response.substring(1, response.length() - 1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                Log.d("custodyTypess",n);
                try {
                    JSONObject ob= new JSONObject(n);
                    JSONArray typesArray=ob.getJSONArray("DataTable");
                    for(int i=0; i<typesArray.length();i++){
                        JSONObject loanObject= typesArray.getJSONObject(i);
                     int custodyCode=loanObject.getInt("ITEM_CODE");
                     String custodyName=loanObject.getString("ITEM_NMAR");
                     custodyTypeList.add(new importatnt(custodyCode,custodyName));
                     adapter.add(custodyName);

                    }
                    progress.setVisibility(View.GONE);
               custody_type.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError() {

            }
        });
        custody_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                custodyCode=     custodyTypeList.get(i).getqTypeId();
                Log.d("custod", String.valueOf(custodyCode));
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
