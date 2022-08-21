package com.androidmax.max.hr.Employee.requestpkg;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidmax.max.hr.Employee.Api;
import com.androidmax.max.hr.Employee.RequestInterface;
import com.androidmax.max.hr.Employee.infoPkg.importatnt;
import com.androidmax.max.hr.R;
import com.androidmax.max.hr.keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class loan_details extends Fragment {
    vacationClass vacationObj;
    SharedPreferences shared;
    String emp_code,org_id;
    int job_dgr;
    Api api;
    TextView status, type,Date,amount,desc,rejectedNote,rejectedNoteView, Note;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.loan_details,null);
        status=(TextView) v.findViewById(R.id.status);
        type=(TextView) v.findViewById(R.id.loan_type);
        Date=(TextView) v.findViewById(R.id.date);
        amount=(TextView) v.findViewById(R.id.amount);
        desc=(TextView) v.findViewById(R.id.desc);
        Note=(TextView) v.findViewById(R.id.note);
        rejectedNote=(TextView) v.findViewById(R.id.rejected_note);
        rejectedNoteView=(TextView) v.findViewById(R.id.rejectedNoteView);
        Bundle bundle= getArguments();
         vacationObj=(vacationClass)bundle.getSerializable(keys.LOAN);
        init();
        getLoanList();
        return v;
    }
    public  void init(){
        getActivity().setTitle(getString(R.string.loan_request));

        api = new Api(getActivity());
        shared = getActivity().getSharedPreferences("user", 0);
        org_id = shared.getString("org_id", "");
        emp_code=vacationObj.getEmp_code();
        job_dgr=shared.getInt("jobDegree",0);


    }
    public void  getLoanList(){

        final ArrayList<importatnt> loanTypeList= new ArrayList<>();
        api.getTypes(org_id, "Get_All_LoanTypes", "null","null", "null","null","null",new RequestInterface() {
            @Override
            public void onResponse(String response) {
                String n = response.substring(1, response.length() - 1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                Log.d("responsev",response);

                try {
                    JSONObject ob= new JSONObject(n);
                    JSONArray typesArray=ob.getJSONArray("DataTable");
                    for(int i=0; i<typesArray.length();i++){
                        JSONObject loanObject= typesArray.getJSONObject(i);
                        int loan_code=loanObject.getInt("LOAN_CODE");
                        String loan_Name=loanObject.getString("NMAR");
                        loanTypeList.add(new importatnt(loan_code,loan_Name));

                    }
                    String loan_name=  getSliceById(Integer.parseInt(vacationObj.getvSlice()),loanTypeList);
                    Log.d("slice name", loan_name);
                    setView(loan_name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError() {
                Log.d("responsev","error");

            }
        });}
    public String getSliceById(int id, ArrayList<importatnt>slice){
        for(int i=0; i<slice.size();i++){
            int slice_id=slice.get(i).getqTypeId();
            String slice_name=slice.get(i).getqName();
            if (id == slice_id) {
                return slice_name;
            }
        }
        return  null;
    }
    public void setView(String loanType_name){
        amount.setText(vacationObj.getvAmount());
        desc.setText(vacationObj.getvDescription());
        importatnt.check(vacationObj.getNote(),Note);
        Date.setText(vacationObj.getvStartDate());
        type.setText(loanType_name);
        String statustxt= vacationObj.getvStatues();
     //   status.setText(statustxt);
        String rejected= vacationObj.getvRejectedNote();

        if(rejected.equals("null")){
            rejectedNoteView.setVisibility(View.GONE);
            rejectedNote.setVisibility(View.GONE);

        }
        else{
            rejectedNote.setText(rejected);

        }
          getStatues(statustxt,status);

    }
    public void getStatues(String status, TextView statusView){
        if(status.equals(getString(R.string.pending))){
            statusView.setText(getString(R.string.pending));
            Drawable img = getResources().getDrawable( R.drawable.warning );
            img.setBounds( 0, 0, 60, 60 );
            statusView.setCompoundDrawables( img, null, null, null );
            statusView.setBackgroundColor(getResources().getColor(R.color.yellow));



        }
        else   if(status.equals(getString(R.string.rejected))){
            statusView.setText(getString(R.string.rejected));
            Drawable img =getResources().getDrawable( R.drawable.false_icon );
            img.setBounds( 0, 0, 60, 60 );
            statusView.setCompoundDrawables( img, null, null, null );

            statusView.setBackgroundColor(getResources().getColor(R.color.red));

        }
        else
        {
            statusView.setText(getString(R.string.accept));
            Drawable img =getResources().getDrawable( R.drawable.true_icon );
            img.setBounds( 0, 0, 60, 60 );
            statusView.setCompoundDrawables( img, null, null, null );
            // v_status.setCompoundDrawablesWithIntrinsicBounds( R.drawable.true_icon, 0, 0, 0);
            statusView.setBackgroundColor(getResources().getColor(R.color.green));

        }

    }
}
