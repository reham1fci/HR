package com.androidmax.max.hr.Employee;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmax.max.hr.Admin.adMainPage;
import com.androidmax.max.hr.R;
import com.androidmax.max.hr.dialog_interface;
import com.androidmax.max.hr.normalWindow;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
 EditText userNameEd, passwordEd,orgCodeEd;
  Button logiBtn;
  TextView  forgetPassTv,signUpTv;
  public  static  final String forget_pass="ForgetPassword";
    public  static  final String sign_up="SetMyAccount";
    ProgressBar progress;
    SharedPreferences shared;
    Api api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameEd=(EditText)findViewById(R.id.user_name) ;
        passwordEd=(EditText)findViewById(R.id.password) ;
        orgCodeEd=(EditText)findViewById(R.id.org_code) ;
        forgetPassTv=(TextView) findViewById(R.id.forget_pass) ;
        signUpTv=(TextView) findViewById(R.id.create_account) ;
        logiBtn=(Button) findViewById(R.id.login) ;
        progress=(ProgressBar) findViewById(R.id.progress);
      //  layout=(LinearLayout) findViewById(R.id.layout);
        api= new Api(this);
         shared = getSharedPreferences("user",0);
       // Boolean isAdmin= shared.getBoolean("admin",true);
        if(shared.contains("emp_code")){
            Log.d("test" , "test shared") ;
            String user_name= shared.getString("emp_name","");
            String password= shared.getString("password","");
            String org_id= shared.getString("org_id","");
            if(isNetworkConnected()){
                 progress.setVisibility(View.VISIBLE);
                 userNameEd.setVisibility(View.GONE);
                 passwordEd.setVisibility(View.GONE);
                 orgCodeEd.setVisibility(View.GONE);
                 signUpTv.setVisibility(View.GONE);
                 forgetPassTv.setVisibility(View.GONE);
                 logiBtn.setVisibility(View.GONE);

           checkLoginData(user_name, password, org_id);
            }
            else {
                normalWindow.window("no Internet connection", this);
                userNameEd.setText(user_name);
                passwordEd.setText(password);
                orgCodeEd.setText(org_id);
            }
        }
        forgetPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, ForgetPassword.class);
                i.putExtra("type", forget_pass);
                startActivity(i);
            }
        });
        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, ForgetPassword.class);
                i.putExtra("type", sign_up);
                startActivity(i);

            }
        });
        logiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           login();
            }
        });
        getSupportActionBar().hide();


    }
    public  boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
     public void login() {
         if (!isNetworkConnected()){
             normalWindow.window("no Internet connection", this);
         }
         else {
         final String orgId_txt = orgCodeEd.getText().toString();
         final String userName_txt = userNameEd.getText().toString();
         final String password_txt = passwordEd.getText().toString();
         if (orgId_txt.isEmpty()) {
             orgCodeEd.setError(getString(R.string.enter) + getString(R.string.org_code));
         } else if (userName_txt.isEmpty()) {
             userNameEd.setError(getString(R.string.enter) + getString(R.string.user_name));

         } else if (password_txt.isEmpty()) {
             passwordEd.setError(getString(R.string.enter) + getString(R.string.password));
         } else {
             progress.setVisibility(View.VISIBLE);
             api.login(orgId_txt, userName_txt, password_txt, new RequestInterface() {
                 @Override
                 public void onResponse(String response) {

                     String n = response.substring(1, response.length() - 1);
                     n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                     //  n = n.replaceAll("\\s+","");

                     Log.d("reslogin", n);
                     try {
                         JSONObject object = new JSONObject(n);
                         String login = object.getString("Msg");
                         Log.d("m", login);

                         if (login.equals("Success")) {
                             progress.setVisibility(View.GONE);
                             final String empCode = object.getString("Emp_Code");
                             String name = object.getString("Nm_Ar");
                             int jobDegree = object.getInt("Dgr_Code");
                             final String jobDegree_txt = object.getString("Dgr_Txt");
                             int isManager = object.getInt("isManager");
                             final SharedPreferences.Editor shared = getSharedPreferences("user", 0).edit();
                             shared.putString("emp_code", empCode);
                             Log.d("emp code", empCode);
                             shared.putString("org_id", orgId_txt);
                             shared.putString("emp_name", userName_txt);
                             shared.putString("password", password_txt);
                             shared.putString("name", name);
                             shared.putInt("jobDegree", jobDegree);
                             if (isManager == 1) {
                                 shared.putBoolean("admin", true);
                                 shared.putString("admin_emp_code", empCode);
                                 shared.putInt("admin_job_degree", jobDegree);
                                 dialogOkCancel(getString(R.string.choose_account), new dialog_interface() {
                                     @Override
                                     public void onDialogOkClick(AlertDialog alertDialog) {
                                       Intent  i = new Intent(getApplicationContext(), adMainPage.class);
                                         i.putExtra("jobDegree", jobDegree_txt);
                                         shared.putString("account", "admin");
                                         shared.commit();
                                         startActivity(i);
                                         finish();
                                     }

                                     @Override
                                     public void onDialogCancelClick(AlertDialog alertDialog) {
                                         Intent i = new Intent(getApplicationContext(), MainPage.class);
                                         i.putExtra("jobDegree", jobDegree_txt);
                                         shared.putString("account", "emp");
                                         shared.commit();
                                         startActivity(i);
                                         finish();

                                     }
                                 });


                             } else {
                                  Intent i = new Intent(getApplicationContext(), MainPage.class);
                                 shared.putBoolean("admin", false);
                                 shared.putString("admin_emp_code", empCode);
                                 shared.putInt("admin_job_degree", jobDegree);
                                 shared.commit();
                                 i.putExtra("jobDegree", jobDegree_txt);
                                 startActivity(i);
                                 finish();

                             }


                         } else {
                             Toast.makeText(Login.this, login, Toast.LENGTH_SHORT).show();
                         }

                     } catch (JSONException e) {
                         e.printStackTrace();
                     }

                 }

                 @Override
                 public void onError() {
                     //login();
                 }
             });

         }}
     }
    public  void dialogOkCancel(String message, final dialog_interface click){
        final AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
        // alertDialog.setTitle("انتبه");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.Admin),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        click.onDialogOkClick(alertDialog);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.myAccount), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                click.onDialogCancelClick(alertDialog);

            }
        });

        alertDialog.show();
    }
     public void  checkLoginData(String user_name, String password, String org_id){
        api.login(org_id, user_name, password, new RequestInterface() {
            @Override
            public void onResponse(String response) {

                String n = response.substring(1, response.length() - 1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                //  n = n.replaceAll("\\s+","");

                Log.d("reslogin", n);
                try {
                    JSONObject object = new JSONObject(n);
                    String login = object.getString("Msg");
                    Log.d("m", login);

                    if (login.equals("Success")) {
                        Boolean isAdmin= shared.getBoolean("admin",true);

                        if(isAdmin){
                            SharedPreferences.Editor editor = shared.edit();
                            String admin_code= shared.getString("admin_emp_code","");
                            int admin_job_degree= shared.getInt("admin_job_degree",0);
                            editor.putString("emp_code",admin_code);
                            editor.putInt("jobDegree",admin_job_degree);
                            editor.commit();
                            String last_open_account=shared.getString("account","");
                            if(last_open_account.equals("admin")){
                                Intent i= new Intent(getApplicationContext(),adMainPage.class);
                                startActivity(i);
                                finish();
                            }
                            else {
                                Intent i= new Intent(getApplicationContext(),MainPage.class);
                                startActivity(i);
                                finish();
                            }
                        }
                        else{
                            Intent i= new Intent(getApplicationContext(),MainPage.class);
                            startActivity(i);
                            finish();
                        }

                    }
            }catch (JSONException e){
                }
            }

            @Override
            public void onError() {

            }
        });
     }
}
