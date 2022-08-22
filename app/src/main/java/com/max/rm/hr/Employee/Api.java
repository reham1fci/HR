package com.max.rm.hr.Employee;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.max.rm.hr.Employee.infoPkg.info_personal;

import java.util.HashMap;
import java.util.Map;

public class Api {
    public static final String BASE_URL="http://jazeerademo.webhop.net:8082/api/";
    Activity activity;
    RequestQueue queue;
    public Api(Activity activity) {
        this.activity = activity;
        this.queue= Volley.newRequestQueue(activity);
    }

    public void login(final String org_id, final String user_name ,final String password, final RequestInterface object){
        String url = BASE_URL+"Users";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
                 Log.d("login" , response  )      ;       }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                object.onError();
                Log.d("error", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                map.put("Org_id", org_id) ;
                map.put("user_name", user_name) ;
                map.put("password", password) ;
                return map;

            }
        };
       request.setRetryPolicy(new DefaultRetryPolicy( 50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);

    }
    public void forgetSetAccount(final String org_id, final String user_name , final String email, final String phone, final String emp_code, final String fingerprint, final String FuncationType, final RequestInterface object){
        String url = BASE_URL+"Users?Org_id="+org_id+"&name="+user_name+"&phone="+phone+"&email="+email+"&Emp_Code="+emp_code+"&Emp_Id="+fingerprint+"&FuncationType="+FuncationType;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                map.put("Content-Type", "application/json");
                map.put("Accept", "application/json");
                return map;
            }
        };
        queue.add(request);

    }
    public void getEmpData(final String org_id,final String Emp_Code,final String funName , final RequestInterface object){
        String url = BASE_URL+"Employee";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();
                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                map.put("Content-Type", "application/json");
                map.put("Accept", "application/json");
                map.put("FuncationType", funName)    ;
                map.put("Org_id", org_id) ;
                map.put("Emp_Code", Emp_Code) ;

                return map;

            }
        };
     //   request.setRetryPolicy(new DefaultRetryPolicy( 50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);

    }
    public void getAllEmployeesData(final String org_id,final String funName , final RequestInterface object){
        String url = BASE_URL+"Employee";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              Toast.makeText(activity, "internet connection problem try again ", Toast.LENGTH_SHORT).show();

                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                map.put("FuncationType", funName)    ;
                map.put("Org_id", org_id) ;
                return map;

            }
        };
        queue.add(request);

    }
    public void addQualification(final String record_id, final String org_id, final String Emp_Code, final String funName , final String q_type, final String q_note, final String q_date, final String q_major, final RequestInterface object){
        String url = BASE_URL+"EmpProcess";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                if(!record_id.equals("null")){
                    map.put("ID", record_id)    ;

                }
                map.put("FuncationType", funName)    ;
                map.put("Org_id", org_id) ;
                map.put("Emp_Code", Emp_Code) ;
                map.put("Q_Type", q_type) ;
                map.put("Q_Major", q_major) ;
                map.put("Q_Date", q_date) ;
                map.put("Q_Note", q_note) ;

                return map;

            }
        };
        queue.add(request);

    }
    public void Delete(final String module, final String org_id, final String Emp_Code, final String funName , final String q_id, final RequestInterface object){
        String url = BASE_URL+module;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                map.put("FuncationType", funName)    ;
                map.put("Org_id", org_id) ;
                   if(module.equals("Reminders")){
                       if(funName.equals("SetAs_Hidden_Reminder")) {  map.put("Emp_Code", Emp_Code) ;}
                       else{map.put("Usr_id", Emp_Code) ;}
                       map.put("Reminder_Code",q_id) ;
                           }
                               else{
                   map.put("Emp_Code", Emp_Code) ;
                     map.put("ID",q_id) ;
                                }

                return map;

            }
        };
        queue.add(request);

    }
    public void addExpert(final String record_id, final String org_id, final String Emp_Code, final String funName , final String job_years, final String job_title, final String job_details, final RequestInterface object){
        String url = BASE_URL+"EmpProcess";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                if(!record_id.equals("null")){
                    map.put("ID", record_id)    ;
                    Log.d("id",record_id);

                }
                map.put("FuncationType", funName)    ;
                map.put("Org_id", org_id) ;
                map.put("Emp_Code", Emp_Code) ;
                map.put("Exp_years", job_years) ;
                Log.d("years",job_years);

                map.put("Exp_Title", job_title) ;
                Log.d("title",job_title);

                map.put("Exp_Detail", job_details) ;
                Log.d("details",job_details);


                return map;

            }
        };
        queue.add(request);

    }
    public void editLoginData(final String Emp_Code, final String org_id, final String user_name , final String password, final RequestInterface object){
        String url = BASE_URL+"EmpProcess";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                map.put("Org_id", org_id) ;
                map.put("FuncationType", "Edit_UserName&Pass")    ;
                map.put("Emp_Code", Emp_Code) ;
                map.put("New_UserName", user_name) ;
                map.put("New_Password", password) ;
                return map;

            }
        };
        queue.add(request);

    }
    public void getDailyAttendance(final String startDate, final String endDate, final String workPeriod, final String Emp_code, final String Org_id, final String functionName, final RequestInterface object){
        String url = BASE_URL+"Attendance";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
          }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                map.put("Content-Type", "application/json");
                map.put("Accept", "application/json");

                if(!workPeriod.equals("null")){
                      map.put("WorkPeriods", workPeriod) ;
                    Log.d("work",workPeriod);


                }
                map.put("Org_id", Org_id) ;
                map.put("FuncationType", functionName)    ;
                map.put("Emp_Code", Emp_code) ;
                map.put("StartDate", startDate) ;
                map.put("EndDate", endDate) ;
                return map;

            }
        };
        queue.add(request);
    }
    public void getMonthlyAttendance(final String Emp_code, final String Org_id, final String fun_name, final RequestInterface object){
        String url = BASE_URL+"Attendance";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                map.put("Org_id", Org_id) ;
                map.put("FuncationType", fun_name)    ;
                map.put("Emp_Code", Emp_code) ;
                return map;

            }
        };
        queue.add(request);
    }
    public void getWorkPeriod(final String Org_id, final String function_name, final String emp_code,final RequestInterface object){
        String url = BASE_URL+"Attendance";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
                Log.d("work",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                if(!emp_code.equals("null")){
                    map.put("Emp_Code", emp_code);

                }
                map.put("Org_id", Org_id) ;
                map.put("FuncationType", function_name);
                return map;

            }
        };
        queue.add(request);
    }
    public void getCities(final String Org_id, final String function_name, final RequestInterface object){
        String url = BASE_URL+"Employee";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                map.put("Org_id", Org_id) ;
                map.put("FuncationType", function_name);
                return map;

            }
        };
        queue.add(request);
    }
    public void editProfile(final String org_id, final String Emp_code,
                            final String FIRST_NM_AR, final String MIDDLE_NM_AR , final String LAST_NM_AR,
                            final String FIRST_NM_EN , final String MIDDLE_NM_EN, final String LAST_NM_EN,
                             final int SOICALSTATE
            , final int BIRTHCOUNTRY, final int BIRTHCITY , final String BIRTHDATE_G , final String PHONE,
                            final String EMAIL , final String IMG, final RequestInterface object  ){
        String url = BASE_URL+"EmpProcess";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                object.onError();
                error.printStackTrace();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                map.put("Content-Type", "application/json");
                map.put("Accept", "application/json");

                map.put("Org_id", org_id) ;
                map.put("FuncationType", "Edit_PersonalData")    ;
                map.put("Emp_Code", Emp_code) ;
                map.put("FIRST_NM_AR", FIRST_NM_AR) ;
                Log.d("FIRST_NM_AR", FIRST_NM_AR);
                map.put("MIDDLE_NM_AR", MIDDLE_NM_AR) ;
                Log.d("MIDDLE_NM_AR", MIDDLE_NM_AR);

                map.put("LAST_NM_AR", LAST_NM_AR) ;
                Log.d("LAST_NM_AR", LAST_NM_AR);

                map.put("FIRST_NM_EN", FIRST_NM_EN) ;
                Log.d("FIRST_NM_EN", FIRST_NM_EN);

                map.put("MIDDLE_NM_EN", MIDDLE_NM_EN) ;
                Log.d("MIDDLE_NM_EN", MIDDLE_NM_EN);

                map.put("LAST_NM_EN", LAST_NM_EN) ;
                Log.d("LAST_NM_EN", LAST_NM_EN);

                map.put("SOICALSTATE", String.valueOf(SOICALSTATE)) ;
                Log.d("SOICALSTATE", String.valueOf(SOICALSTATE));

                map.put("BIRTHCOUNTRY", String.valueOf(BIRTHCOUNTRY)) ;
                Log.d("BIRTHCOUNTRY", String.valueOf(BIRTHCOUNTRY)) ;

                map.put("BIRTHCITY", String.valueOf(BIRTHCITY)) ;
                Log.d("BIRTHCITY", String.valueOf(BIRTHCITY)) ;
                map.put("BIRTHDATE_G", BIRTHDATE_G) ;
                Log.d("BIRTHDATE_G", BIRTHDATE_G) ;
                map.put("PHONE", PHONE) ;
                Log.d("PHONE", PHONE) ;
                map.put("EMAIL", EMAIL) ;
                Log.d("EMAIL", EMAIL) ;
                map.put("IMG", IMG) ;

                Log.d("IMG", IMG) ;
                info_personal.longInfo(IMG);


                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy( 50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(request);
    }
     public  void getArchiveImage(final String org_id, final String emp_code, final int archive_id, final RequestInterface object){
         String url = BASE_URL+"Employee";
         StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                object.onResponse(response);
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                 object.onError();

             }
         }){
             @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 Map<String, String> map= new HashMap<String, String>();
                 map.put("Org_id", org_id) ;
                 map.put("FuncationType", "Get_Emp_Achive_ByID");
                 map.put("Archive_ID", String.valueOf(archive_id));
                 map.put("Emp_Code", emp_code) ;

                 return map;

             }
         };
         queue.add(request);


     }
    public void addCustodyRequest(final String record_id, final String org_id, final String Emp_Code, final String funName , final String custody_type, final String note, final String date, final String description, final String amount, final String mobile_model, final RequestInterface object){
        String url = BASE_URL+"Requests";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                if(!record_id.equals("null")){
                    map.put("ID", record_id)    ;

                }
                map.put("FuncationType", funName)    ;
                map.put("Org_id", org_id) ;
                map.put("Usr_id", Emp_Code) ;
                map.put("Amount", amount) ;
                map.put("DT_G", date) ;
                map.put("Note", note) ;
                map.put("Description",description) ;
                map.put("Emp_Code", Emp_Code) ;
                map.put("Custod_Code", custody_type) ;
                map.put("RQ_Source",mobile_model ) ;

                return map;

            }
        };
        queue.add(request);

    }
    public void addOtherRequest(final String record_id, final String org_id, final String Emp_Code, final String funName , final String other_type, final String note, final String date, final String body, final String title, String refrence , final String mobile_model, final String macAddress, final RequestInterface object){
        String url = BASE_URL+"Requests";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                if(!record_id.equals("null")){
                    map.put("ID", record_id)    ;

                }
                map.put("FuncationType", funName)    ;
                map.put("Org_id", org_id) ;
                map.put("Usr_id", Emp_Code) ;
                map.put("RQ_Title", title) ;
                map.put("DT_G", date) ;
                map.put("Note", note) ;
                map.put("RQ_Body",body) ;
                map.put("Emp_Code", Emp_Code) ;
                map.put("RQ_Type", other_type) ;
                map.put("RQ_Source",mobile_model ) ;
                map.put("MacAddress",macAddress ) ;

                return map;

            }
        };
        queue.add(request);

    }

    public void addPermissionRequest(final String record_id, final String org_id, final String Emp_Code, final String funName , final  String PerType,final String periods, final String note, final String date, final String description, final String mobile_model, final RequestInterface object){
        String url = BASE_URL+"Requests";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                if(!record_id.equals("null")){
                    map.put("ID", record_id)    ;

                }
                map.put("FuncationType", funName)    ;
                map.put("Org_id", org_id) ;
                map.put("Usr_id", Emp_Code) ;
             //   map.put("Amount", amount) ;
                map.put("DT_G", date) ;
                map.put("Note", note) ;
                map.put("Description",description) ;
                map.put("Emp_Code", Emp_Code) ;
                map.put("w_id", periods);
                map.put("RQ_Source",mobile_model ) ;
                map.put("PerType",PerType ) ;
                return map;

            }
        };
        queue.add(request);

    }
    public void addLoanRequest(final String record_id, final String org_id, final String Emp_Code, final String funName , final String loan_type, final String loan_note, final String loan_date, final String loan_description, final String loan_amount, final RequestInterface object){
        String url = BASE_URL+"Requests";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                if(!record_id.equals("null")){
                    map.put("ID", record_id)    ;

                }
                map.put("FuncationType", funName)    ;
                map.put("Org_id", org_id) ;
                map.put("Usr_id", Emp_Code) ;
                map.put("Amount", loan_amount) ;
                map.put("DT_G", loan_date) ;
                map.put("Note", loan_note) ;
                map.put("Description", loan_description) ;
                map.put("Emp_Code", Emp_Code) ;
                map.put("Loan_Code", loan_type) ;

                return map;

            }
        };
        queue.add(request);

    }
    public void addVacationRequest(final String record_id, final String org_id, final String Emp_Code, final String funName , final String vacation_slice, final String num_days, final String current_date, final String start_date, final String end_date, final String description,final String note,final RequestInterface object){
        String url = BASE_URL+"Requests";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                if(!record_id.equals("null")){
                    map.put("ID", record_id)    ;

                }
                map.put("FuncationType", funName)    ;
                map.put("Org_id", org_id) ;
                map.put("Usr_id", Emp_Code) ;
                map.put("Amount", num_days) ;
                map.put("DT_G", current_date) ;
                map.put("StartDate_G", start_date) ;
                map.put("EndDate_G", end_date) ;
                map.put("Description", description) ;
                map.put("Emp_Code", Emp_Code) ;
                map.put("Vac_Slice_Code", vacation_slice) ;
                map.put("Note", note) ;


                return map;

            }
        };
        queue.add(request);

    }
     public void PayRoll(String function_name, String org_id, String emp_code, String month, String year , final RequestInterface object){
        String url = BASE_URL+"PayRoll?FuncationType="+function_name+"&Org_id="+org_id+"&Mnth="+month+"&Year="+year+"&Emp_Code="+emp_code;
         StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 object.onResponse(response);
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 object.onError();
                 Log.d("responsev",error.toString());

             }
         });
         queue.add(request);

     }
    public void getTasks(String function_name, String org_id, String emp_code, final RequestInterface object){
        String url;
        if(function_name.equals("Get_Reminders_Details")){
               url = BASE_URL+"Reminders?FuncationType="+function_name+"&Org_id="+org_id+"&Reminder_Code="+emp_code;}
               else {
         url = BASE_URL+"Reminders?FuncationType="+function_name+"&Org_id="+org_id+"&Emp_Code="+emp_code;}
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                object.onError();
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                Log.d("responsev",error.toString());

            }
        });
        queue.add(request);

    }


    public void getTypes( String Org_id,String function_name,String Dgr_code , String Emp_code,String Mst_Code,String isUsed ,String isRejected,final RequestInterface object){
        String url;

        if(!Mst_Code.equals("null")){
            url = BASE_URL+"Requests?FuncationType="+function_name+"&Org_id="+Org_id+"&Dgr_Code="+Dgr_code+"&Mst_Code="+Mst_Code;

        }
       else if(!Dgr_code.equals("null")&&!Emp_code.equals("null")){
            url = BASE_URL+"Requests?FuncationType="+function_name+"&Org_id="+Org_id+"&Dgr_Code="+Dgr_code+"&Emp_Code="+Emp_code;

        }
        else if(!Dgr_code.equals("null")){
        url = BASE_URL+"Requests?FuncationType="+function_name+"&Org_id="+Org_id+"&Dgr_Code="+Dgr_code;
        }
        else if(!Emp_code.equals("null")&&!isUsed.equals("null")&&!isRejected.equals("null")){

            url = BASE_URL+"Requests?FuncationType="+function_name+"&Org_id="+Org_id+"&Emp_Code="+Emp_code+"&isUsed="+isUsed+"&isReject="+isRejected;

        }
        else if(!isUsed.equals("null")&&!isRejected.equals("null")){

            url = BASE_URL+"Requests?FuncationType="+function_name+"&Org_id="+Org_id+"&isUsed="+isUsed+"&isReject="+isRejected ;

        }
        else if(!Emp_code.equals("null")){
            url = BASE_URL+"Requests?FuncationType="+function_name+"&Org_id="+Org_id+"&Emp_Code="+Emp_code;
        }
        else {
             url = BASE_URL + "Requests?FuncationType=" + function_name + "&Org_id=" + Org_id;
        }
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                object.onError();
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                Log.d("responsev",error.toString());

            }
        });
        queue.add(request);
    }


    public void addEditTask(final String function_name, final String org_id, final String record_id,
                            final String title, final String desc, final String date,final String time, final String timeIn, final String dateIn,
                            final int isRepeat, final String employees_ids, final String admin_id,
                            final RequestInterface object ){

        String url = BASE_URL+"Reminders";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                object.onError();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                if(!record_id.equals("null")){
                    map.put("Reminder_Code", record_id) ;
                }
                map.put("Org_id", org_id) ;

                Log.d("Org_id",org_id);
                map.put("FuncationType", function_name);
                Log.d("Org_id",org_id);

                map.put("isRepeate", String.valueOf(isRepeat));
                map.put("Employees", employees_ids) ;
                map.put("Title", title) ;
                map.put("R_Desc", desc) ;
                map.put("InDate", dateIn) ;
                map.put("InTime", timeIn) ;
                map.put("Time", time) ;
                map.put("Usr_id", admin_id) ;
                map.put("DT_G", date) ;
                return map;

            }
        };
        queue.add(request);



    }
    public void getTaskDetails(final String org_id, final String funName , final String record_id , final RequestInterface object){
        String url = BASE_URL+"Reminders";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                map.put("FuncationType", funName)    ;
                map.put("Org_id", org_id) ;
                map.put("Reminder_Code", record_id) ;
                return map;

            }
        };
        queue.add(request);

    }
    public void sendNotification (final String chat_key, final String chat_type, final String chat_title, final String sender_emp_code, final String sender_emp_name, final String message_txt, final String Register_key,final RequestInterface object){
        String url= "http://jazeerademo.webhop.net:8080/notification.php";
        StringRequest request= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             object.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                object.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map= new HashMap<String, String>();
                map.put("chat_key", chat_key);
                map.put("chat_title", chat_title);
                map.put("chat_type", chat_type);
                map.put("sender_code", sender_emp_code);
                map.put("sender_name", sender_emp_name);
                map.put("message", message_txt);
                map.put("register_key", Register_key);
                return map;
            }
        };

   queue.add(request);
    }


}

