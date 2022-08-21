package com.max.rm.hr.Employee.requestpkg;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.max.rm.hr.Employee.Api;
import com.max.rm.hr.Employee.RequestInterface;
import com.max.rm.hr.R;
import com.max.rm.hr.keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class requestsTypes extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
     TextView pending, reject,accept, pendingNum,rejectedNum,acceptNum;
    boolean all,addButton;
    RelativeLayout pendingLayout,acceptLayout,rejectedLayout;
    FragmentTransaction transaction;
    Api api;
    ProgressBar progress;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences shared;
    String emp_code,org_id;
     Button addBtn;
    FloatingActionButton addRequest;

    int job_dgr;
    int count=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.request_types,null);
        pending=(TextView) v.findViewById(R.id.pendingReq) ;
        reject=(TextView)v.findViewById(R.id.rejectedReq) ;
        accept=(TextView)v.findViewById(R.id.acceptReq) ;
        pendingNum=(TextView)v.findViewById(R.id.pendReqNum) ;
        addRequest=(FloatingActionButton)v.findViewById(R.id.addBtn) ;

        rejectedNum=(TextView)v.findViewById(R.id.rejectedReqNum) ;
        acceptNum=(TextView)v.findViewById(R.id.acceptReqNum) ;
        pendingLayout=(RelativeLayout) v.findViewById(R.id.pendingReqLayout) ;
        acceptLayout=(RelativeLayout) v.findViewById(R.id.acceptReqLayout) ;
        rejectedLayout=(RelativeLayout) v.findViewById(R.id.rejectedReqLayout) ;
        all= getArguments().getBoolean(keys.allEmployee);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        addButton= getArguments().getBoolean(keys.addButton);
        transaction=getActivity().getSupportFragmentManager().beginTransaction();
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
                                        swipeRefreshLayout.setRefreshing(true);

                                        getRequestCount(emp_code,pendingNum ,"0","0");
                                    }
                                }
        );
         addRequest.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Fragment f=new MyRequests();
                 Bundle b= new Bundle();
                 b.putBoolean(keys.allEmployee,all);
                 b.putBoolean(keys.addButton,addButton);
                 b.putBoolean("add_request",true);
                 b.putString(keys.IS_USED,"0");
                 b.putString(keys.IS_REJECTED,"0");
                 f.setArguments(b);
                 transaction.replace(R.id.frame,f,"MyRequests").addToBackStack("MyRequests").commit();

             }
         });

        pendingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new MyRequests();
                Bundle b= new Bundle();
                b.putBoolean(keys.allEmployee,all);
                b.putBoolean(keys.addButton,addButton);
                b.putString(keys.IS_USED,"0");
                b.putString(keys.IS_REJECTED,"0");
                f.setArguments(b);
                transaction.replace(R.id.frame,f,"MyRequests").addToBackStack("MyRequests").commit();

            }
        });
        acceptLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new MyRequests();
                Bundle b= new Bundle();
                b.putBoolean(keys.allEmployee,all);
                b.putBoolean(keys.addButton,addButton);
                b.putString(keys.IS_USED,"1");
                b.putString(keys.IS_REJECTED,"0");
                f.setArguments(b);
                transaction.replace(R.id.frame,f,"MyRequests").addToBackStack("MyRequests").commit();

            }
        });
        rejectedLayout .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new MyRequests();
                Bundle b= new Bundle();
                b.putBoolean(keys.allEmployee,all);
                b.putBoolean(keys.addButton,addButton);
                b.putString(keys.IS_USED,"0");
                b.putString(keys.IS_REJECTED,"1");
                f.setArguments(b);
                transaction.replace(R.id.frame,f,"MyRequests").addToBackStack("MyRequests").commit();

            }
        });
        return v;
    }
    public void getRequestCount( final String emp_code, final TextView tv ,String isUsed, String isRejected){
        Log.d("isused",isUsed);
        Log.d("isRejected",isRejected);

        api.getTypes(org_id, "Get_All_RQ_Count", "null", emp_code, "null",isUsed,isRejected,new RequestInterface() {
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
              swipeRefreshLayout.setRefreshing(false);

                Toast.makeText(getActivity(), "حدث خطا اعد المحاولة  ", Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void init(){
        getActivity().setTitle(getString(R.string.requests));

        show_icon(R.drawable.warning,pending);
       show_icon(R.drawable.false_icon,reject);
       show_icon(R.drawable.true_icon,accept);
        api = new Api(getActivity());
        shared = getActivity().getSharedPreferences("user", 0);
        org_id = shared.getString("org_id", "");

    }
    public void choose_function(int count , String emp_code ){
        switch(count){
            case 0:getRequestCount(emp_code,acceptNum,"1","0");break; // accept
            case 1: getRequestCount(emp_code,rejectedNum,"0","1");break;
            default: swipeRefreshLayout.setRefreshing(false);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        count=0;
    }
    public void show_icon(int id, TextView text){
        Drawable img = getActivity().getResources().getDrawable( id );
        img.setBounds( 0, 0, 60, 60 );
        text.setCompoundDrawables( img, null, null, null );


    }

    @Override
    public void onRefresh() {
        getRequestCount(emp_code,pendingNum ,"0","0");

    }
}

