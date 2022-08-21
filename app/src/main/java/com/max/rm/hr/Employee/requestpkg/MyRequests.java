package com.androidmax.max.hr.Employee.requestpkg;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmax.max.hr.Employee.Api;
import com.androidmax.max.hr.Employee.RequestInterface;
import com.androidmax.max.hr.R;
import com.androidmax.max.hr.keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyRequests  extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    TextView loanCount, vacationCount,custodyCount, otherCount,permissionCount;
    RelativeLayout loanReqLayout, vacationReqLayout,custodyReqLayout,otherReqLayout,permissionReqLayout;
    TextView loanReq,vacationReq,antherReq,custodyReq,permissionReq;
    FragmentTransaction transaction;
    String isUsed,isRejected;
    boolean all,addButton;
    SwipeRefreshLayout swipeRefreshLayout;
    Api api;
    SharedPreferences shared;
    String emp_code,org_id;
    int job_dgr;
     int count=0;
     boolean add_request= false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.my_requests,null);
        loanCount=(TextView) v.findViewById(R.id.loanReqNum) ;
        vacationCount=(TextView)v.findViewById(R.id.vacReqNum) ;
        custodyCount=(TextView)v.findViewById(R.id.custodyReqNum);
        otherCount=(TextView)v.findViewById(R.id.anotherRequestNum) ;
        permissionCount=(TextView)v.findViewById(R.id.permissionReqNum) ;
        loanReqLayout=(RelativeLayout) v.findViewById(R.id.loanReqLayout) ;
        vacationReqLayout=(RelativeLayout) v.findViewById(R.id.VacationReqLayout) ;
        custodyReqLayout=(RelativeLayout) v.findViewById(R.id.custdyReqLayout) ;
        otherReqLayout=(RelativeLayout) v.findViewById(R.id.anotherReqLayout) ;
        permissionReqLayout=(RelativeLayout) v.findViewById(R.id.permissionReqLayout) ;
        loanReq=(TextView)v.findViewById(R.id.loanReq) ;
        vacationReq=(TextView)v.findViewById(R.id.vacationReq) ;
        antherReq=(TextView)v.findViewById(R.id.anotherRequest) ;
        custodyReq=(TextView)v.findViewById(R.id.custodyReq) ;
        permissionReq=(TextView)v.findViewById(R.id.permissionReq) ;
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(this);

        all= getArguments().getBoolean(keys.allEmployee);
       addButton= getArguments().getBoolean(keys.addButton);
       isRejected= getArguments().getString(keys.IS_REJECTED);
       isUsed= getArguments().getString(keys.IS_USED);
       Bundle b= getArguments();
        if (b.containsKey("add_request")){
            add_request= true;
        }

        transaction=getActivity().getSupportFragmentManager().beginTransaction();
        getActivity().setTitle(getString(R.string.requests));
   init();
    if(all){
        emp_code="null";
           }
        else{
        emp_code = shared.getString("emp_code", "");

        }
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!add_request){
                                     swipeRefreshLayout.setRefreshing(true);
                                     getRequestCount("Get_Loan_RQ_Count",emp_code,loanCount);

                                            }
                                    }
                                }
        );
        loanReqLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (add_request){
            addLoan();
        }
        else {
            Fragment f = new requestsList();
            Bundle b = new Bundle();
            b.putBoolean(keys.allEmployee, all);
            b.putBoolean(keys.addButton, addButton);
            b.putString(keys.IS_USED, isUsed);
            b.putString(keys.IS_REJECTED, isRejected);
            b.putString(keys.CLASS_NAME, keys.LOAN);
            f.setArguments(b);
            transaction.replace(R.id.frame, f, "requestsList").addToBackStack("requestsList").commit();
        }
    }
});
vacationReqLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (add_request){
            addVacation();
        }
         else{
        Fragment f=new requestsList();
        Bundle b= new Bundle();
        b.putBoolean(keys.allEmployee,all);
        b.putBoolean(keys.addButton,addButton);
        b.putString(keys.CLASS_NAME,keys.VACACTION);
        b.putString(keys.IS_USED,isUsed);
        b.putString(keys.IS_REJECTED,isRejected);
        f.setArguments(b);
        transaction.replace(R.id.frame,f,"requestsList").addToBackStack("requestsList").commit();}
    }
});
otherReqLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (add_request){
            addOther();
        }
         else {
            Fragment f = new requestsList();
            Bundle b = new Bundle();
            b.putBoolean(keys.allEmployee, all);
            b.putBoolean(keys.addButton, addButton);
            b.putString(keys.IS_USED, isUsed);
            b.putString(keys.IS_REJECTED, isRejected);
            b.putString(keys.CLASS_NAME, keys.OTHER);
            f.setArguments(b);
            transaction.replace(R.id.frame, f, "requestsList").addToBackStack("requestsList").commit();
        }
    }
});
custodyReqLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (add_request){
            addCustody();
        }
        else{
        Fragment f=new requestsList();
        Bundle b= new Bundle();
        b.putBoolean(keys.allEmployee,all);
        b.putBoolean(keys.addButton,addButton);
        b.putString(keys.IS_USED,isUsed);
        b.putString(keys.IS_REJECTED,isRejected);
        b.putString(keys.CLASS_NAME,keys.CUSTODY);
        f.setArguments(b);
        transaction.replace(R.id.frame,f,"requestsList").addToBackStack("requestsList").commit();}

    }
});
permissionReqLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (add_request){
            addPermission();
        }
        else{
        Fragment f=new requestsList();
        Bundle b= new Bundle();
        b.putBoolean(keys.allEmployee,all);
        b.putBoolean(keys.addButton,addButton);
        b.putString(keys.IS_USED,isUsed);
        b.putString(keys.IS_REJECTED,isRejected);
        b.putString(keys.CLASS_NAME,keys.PERMISSION);
        f.setArguments(b);
        transaction.replace(R.id.frame,f,"requestsList").addToBackStack("requestsList").commit();}

    }
});
        return  v;
    }

    public void getRequestCount(String function_name, final String emp_code, final TextView tv){

        api.getTypes(org_id, function_name, "null", emp_code, "null",isUsed,isRejected,new RequestInterface() {
            @Override
            public void onResponse(String response) {
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                Log.d("result",n);
                JSONObject ob= null;
                try {
                    ob = new JSONObject(n);
                    JSONArray typesArray=ob.getJSONArray("DataTable");
                    JSONObject countObject=typesArray.getJSONObject(0);
                    int countRequest=countObject.getInt("COUNT");
                    if(countRequest>0){
                        tv.setVisibility(View.VISIBLE);
                        tv.setText(String.valueOf(countRequest));
                    }
                    choose_function(count,emp_code);
                    count=count+1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {
                Toast.makeText(getActivity(), "حدث خطا اعد المحاولة  ", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);


            }
        });


    }
    public void init(){
        api = new Api(getActivity());

        shared = getActivity().getSharedPreferences("user", 0);
        org_id = shared.getString("org_id", "");

    }
    public void choose_function(int count , String emp_code ){
        switch(count){
            case 0:getRequestCount("Get_Vacation_RQ_Count",emp_code,vacationCount);break;
            case 1: getRequestCount("Get_Custody_RQ_Count",emp_code,custodyCount);break;
            case 2:getRequestCount("Get_Other_RQ_Count",emp_code,otherCount); break;
            case 3: getRequestCount("Get_Permission_RQ_Count",emp_code,permissionCount); break;
            default: swipeRefreshLayout.setRefreshing(false);



        }
        }

    @Override
    public void onResume() {
        super.onResume();
        count=0;
    }

    @Override
    public void onRefresh() {
if (add_request){
        swipeRefreshLayout.setRefreshing(false);

}
   else {
        getRequestCount("Get_Loan_RQ_Count",emp_code,loanCount);}

    }
    public void addVacation(){
        Fragment fragment= new Req_Vacation();
        Bundle b= new Bundle();
        b.putString(keys.CLASS_NAME,"AddNew_RQ_Vacation");
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"Req_Vacation").addToBackStack("Req_Vacation").commit();
    }
    public void addPermission(){
        Fragment fragment= new req_permission();
        Bundle b= new Bundle();
        b.putString(keys.CLASS_NAME,"AddNew_RQ_Permission");
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"req_permission").addToBackStack("req_permission").commit();
    }
    public void addCustody(){
        Fragment fragment= new Req_Custody();
        Bundle b= new Bundle();
        b.putString(keys.CLASS_NAME,"AddNew_RQ_Custody");
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"Req_Custody").addToBackStack("Req_Custody").commit();
    }
    public void addLoan(){
        Fragment fragment= new Req_loan();
        Bundle b= new Bundle();
        b.putString(keys.CLASS_NAME,"AddNew_RQ_Loan");
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"Req_loan").addToBackStack("Req_loan").commit();
    }
    public void addOther(){
        Fragment fragment= new req_another();
        Bundle b= new Bundle();
        b.putString(keys.CLASS_NAME,"AddNew_RQ_Other");
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"req_another").addToBackStack("req_another").commit();
    }
}

