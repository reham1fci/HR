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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Req_loan extends Fragment {
    EditText loan_amount, loan_description,loan_note;
    Spinner loan_type;
    Button sendRequest;
    Api api;
    ProgressBar progress;
    SharedPreferences shared;
    String emp_code,org_id,function_name,record_id;
    int job_dgr,loan_code;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.req_loan,null);
         loan_amount=(EditText)v.findViewById(R.id.loan_amount);
         loan_description=(EditText)v.findViewById(R.id.loan_des);
         loan_note=(EditText)v.findViewById(R.id.loan_note);
         loan_type=(Spinner) v.findViewById(R.id.loan_type);
         sendRequest=(Button) v.findViewById(R.id.send);
        progress=(ProgressBar) v.findViewById(R.id.progress) ;

        Bundle bundle= getArguments();
        function_name= bundle.getString(keys.CLASS_NAME);
        if(function_name.equals("Edit_RQ_Loan")){
            vacationClass vactionObj=(vacationClass)bundle.getSerializable(keys.LOAN);
            loan_amount.setText(vactionObj.getvAmount());
            loan_description.setText(vactionObj.getvDescription());
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
        getLoanTypeList();
        return v;

    }
     public  void sendLoanRequest(String record_id,String function_name){
        String loanDescriptionTxt=loan_description.getText().toString();
        String loanNoteTxt=loan_note.getText().toString();
        String loanAmountTxt=loan_amount.getText().toString();
        if( loanDescriptionTxt.isEmpty()){
            loan_description.setError(getString(R.string.enter)+getString(R.string.description));
        }
        else if(loanAmountTxt.isEmpty()){
            loan_amount.setError(getString(R.string.enter)+getString(R.string.loan_amount));
        }
       /*  else if(loan_code==0){
            normalWindow.window(getString(R.string.enter)+getString(R.string.loan_type),getActivity());
        }*/
        else {
            if (loanNoteTxt.isEmpty()) {
                loanNoteTxt = "null";
            }
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            String currentDate = df.format(new Date());
            Log.d("loan_code", String.valueOf(loan_code));
            api.addLoanRequest(record_id, org_id, emp_code, function_name, String.valueOf(loan_code), loanNoteTxt, currentDate, loanDescriptionTxt, loanAmountTxt, new RequestInterface() {
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

     }
     public void  getLoanTypeList(){

         final ArrayList<importatnt>loanTypeList= new ArrayList<>();
         final ArrayAdapter<String>adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);
        api.getTypes(org_id, "Get_LoanTypes", String.valueOf(job_dgr), "null","null","null","null",new RequestInterface() {
            @Override
            public void onResponse(String response) {
                String n = response.substring(1, response.length() - 1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                Log.d("loantype",n);
                try {
                    JSONObject ob= new JSONObject(n);
                    JSONArray typesArray=ob.getJSONArray("DataTable");
                    for(int i=0; i<typesArray.length();i++){
                        JSONObject loanObject= typesArray.getJSONObject(i);
                        int loan_code=loanObject.getInt("LOAN_CODE");
                        String loan_Name=loanObject.getString("NMAR");
                        loanTypeList.add(new importatnt(loan_code,loan_Name));
                        adapter.add(loan_Name);

                    }
                    progress.setVisibility(View.GONE);
                    loan_type.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError() {

            }
        });
        loan_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
           loan_code=     loanTypeList.get(i).getqTypeId();
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
