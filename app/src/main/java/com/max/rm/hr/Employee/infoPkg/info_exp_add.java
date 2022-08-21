package com.androidmax.max.hr.Employee.infoPkg;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidmax.max.hr.Employee.Api;
import com.androidmax.max.hr.Employee.RequestInterface;
import com.androidmax.max.hr.R;

import org.json.JSONException;
import org.json.JSONObject;

public class info_exp_add extends Fragment {
    Button save;
    EditText experiance_years, job_title, job_details;
    String fun_name ,recordId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.info_exp_add,null);
        save=(Button)v.findViewById(R.id.save) ;
        experiance_years=(EditText)v.findViewById(R.id.exp_years) ;
        job_title=(EditText) v.findViewById(R.id.job_title) ;
        job_details=(EditText) v.findViewById(R.id.job_details) ;
        getActivity().setTitle(getString(R.string.add_experience));

        Bundle b=getArguments();
        fun_name=b.getString("fun");
        if(fun_name.equals("Edit_Expert")){
            recordId= b.getString("id");
            experiance_years.setText(b.getString("years"));
            job_title.setText(b.getString("job_title"));
            job_details.setText(b.getString("job_details"));
        }
        else {
            recordId="null";
        }
         save.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
              String exp_years_nums=experiance_years.getText().toString();
              String exp_job_title=job_title.getText().toString();
              String exp_job_details=job_details.getText().toString();
              if(exp_years_nums.isEmpty()){
                  experiance_years.setError(getString(R.string.enter)+getString(R.string.exp_years));
              }
              else if(exp_job_title.isEmpty()){
                  job_title.setError(getString(R.string.enter)+getString(R.string.exp_job_title));


              }
              else  if(exp_job_details.isEmpty()){
                  job_title.setError(getString(R.string.enter)+getString(R.string.exp_job_details));

              }
              else {
addExpert(exp_years_nums,exp_job_title,exp_job_details);
              }

             }
         });
        return v;

    }
     public void addExpert(String job_years, String job_title, String job_details){
         Api api= new Api(getActivity());
         SharedPreferences shared= getActivity().getSharedPreferences("user",0);
         String org_id=shared.getString("org_id","");
         String emp_code=shared.getString("emp_code","");
api.addExpert(recordId, org_id, emp_code, fun_name, job_years, job_title, job_details, new RequestInterface() {
    @Override
    public void onResponse(String response) {
        Log.d("responsen", response);
        //  progress.setVisibility(View.GONE);
        String n= response.substring(1,response.length()-1);
        n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
        try {
            JSONObject object= new JSONObject(n);
            String msg=object.getString("Msg");
            if(msg.equals("Success")){
                Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,new info_experiance()).commit();

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
