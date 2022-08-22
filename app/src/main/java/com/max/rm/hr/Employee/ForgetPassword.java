package com.max.rm.hr.Employee;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.max.rm.hr.R;
import com.max.rm.hr.normalWindow;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPassword extends AppCompatActivity {
EditText nameEd, phoneEd, emailEd, empCodeEd,fingerprintEd,org_code;
Button sendBtn;
Bundle bundle;
ProgressBar progress;
String emp_code_txt,emp_id_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        nameEd=(EditText)findViewById(R.id.nameEd);
        phoneEd=(EditText)findViewById(R.id.phoneEd);
        emailEd=(EditText)findViewById(R.id.emailEd);
        empCodeEd=(EditText)findViewById(R.id.empCodeEd);
        org_code=(EditText)findViewById(R.id.org_code);
        fingerprintEd=(EditText)findViewById(R.id.empFingerprintEd);
        sendBtn=(Button)findViewById(R.id.sendBtn) ;
        progress=(ProgressBar)findViewById(R.id.progress);
        bundle=getIntent().getExtras();
        final String type=bundle.getString("type");
     /*   boolean isEmpCode= bundle.getBoolean("emp_code");
        boolean isFingerprint=bundle.getBoolean("fingerprint");
        if(!isEmpCode){
            empCodeEd.setVisibility(View.GONE);
            emp_code_txt="";
        }
        if (!isFingerprint){
            fingerprintEd.setVisibility(View.GONE);
            emp_id_txt="";
        }*/
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_txt= nameEd.getText().toString();
                String phone_txt= phoneEd.getText().toString();
                String email_txt= emailEd.getText().toString();
                String emp_code_txt= empCodeEd.getText().toString();
                String emp_id_txt= fingerprintEd.getText().toString();
                String org_code_txt= org_code.getText().toString();
                if(org_code_txt.isEmpty()){
                    Toast.makeText(ForgetPassword.this,getString(R.string.enter)+ getString(R.string.org_code), Toast.LENGTH_SHORT).show();


                }
              else  if(name_txt.isEmpty()&&phone_txt.isEmpty()&&email_txt.isEmpty()&&emp_code_txt.isEmpty()&&
                        emp_id_txt.isEmpty()){
                    Toast.makeText(ForgetPassword.this, getString(R.string.fill_data), Toast.LENGTH_SHORT).show();


                }
                else{
                    if(name_txt.isEmpty()){
                        name_txt="null";
                    }
                    else if(phone_txt.isEmpty()){
                        phone_txt="null";
                    }
                    else if(email_txt.isEmpty()){
                        email_txt="null";
                    }
                    else if(emp_code_txt.isEmpty()){
                        emp_code_txt="null";


                    }
                         else if(emp_id_txt.isEmpty()){
                        emp_id_txt="null";

                    }

progress.setVisibility(View.VISIBLE);
                    forget_pass(name_txt, phone_txt,email_txt, emp_code_txt, emp_id_txt,type, org_code_txt);
                }

            }
        });
    }
    public void  forget_pass(String name, String phone, String email, String emp_code,String emp_id, String type,String org_code){

        Api api= new Api(ForgetPassword.this);
api.forgetSetAccount(org_code, name, email, phone, emp_code, emp_id, type, new RequestInterface() {
    @Override
    public void onResponse(String response) {
        progress.setVisibility(View.GONE);

        String n= response.substring(1,response.length()-1);
        n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
        n = n.replaceAll("\\s+","");

        Log.d("reslogin", n);
        try {
            JSONObject object= new JSONObject(n);
            String msg= object.getString("Msg");
            normalWindow.window(msg,ForgetPassword.this);

            if(msg.equals("Success")){

                finish();
            }
            Log.d("m", msg);
        }
            catch (JSONException e){


        }
    }

    @Override
    public void onError() {
        progress.setVisibility(View.GONE);
        Log.d("reslogin","error");

    }
});
    }

}
