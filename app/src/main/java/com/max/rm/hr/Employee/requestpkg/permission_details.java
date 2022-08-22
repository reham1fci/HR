package com.max.rm.hr.Employee.requestpkg;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.max.rm.hr.Employee.Api;
import com.max.rm.hr.Employee.RequestInterface;
import com.max.rm.hr.Employee.infoPkg.importatnt;
import com.max.rm.hr.R;
import com.max.rm.hr.keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class permission_details  extends Fragment {
    vacationClass vacationObj;
    Api api;
    SharedPreferences shared;
    String emp_code,org_id, periodsNames;
    int job_dgr;
    TextView status, type,Date,period,desc,rejectedNote,rejectedNoteView, Note;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.permission_details,null);
        status=(TextView) v.findViewById(R.id.status);
        type=(TextView) v.findViewById(R.id.type);
        Date=(TextView) v.findViewById(R.id.date);
        period=(TextView) v.findViewById(R.id.period);
        desc=(TextView) v.findViewById(R.id.desc);
        Note=(TextView) v.findViewById(R.id.note);
        rejectedNote=(TextView) v.findViewById(R.id.rejected_note);
        rejectedNoteView=(TextView) v.findViewById(R.id.rejectedNoteView);
        Bundle bundle= getArguments();
         vacationObj=(vacationClass)bundle.getSerializable(keys.PERMISSION);
        init();
        getPeriods("Get_Emp_Permission_DTL_RQ");
        return v;
    }

    public void init(){
        getActivity().setTitle(getString(R.string.permission_request));
        api = new Api(getActivity());
        shared = getActivity().getSharedPreferences("user", 0);
        org_id = shared.getString("org_id", "");
        emp_code = shared.getString("emp_code", "");
        job_dgr=shared.getInt("jobDegree",0);

    }
    public void getPeriods(String function_name){
     //   vList= new ArrayList<>();

        api.getTypes(org_id, function_name, String.valueOf(job_dgr), emp_code,String.valueOf(vacationObj.getRecordId()),"null","null", new RequestInterface() {
            @Override
            public void onResponse(String response) {
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                try {
                    JSONObject ob = new JSONObject(n);
                    JSONArray periodsArray= ob.getJSONArray("DataTable");
                    for( int i =0; i<periodsArray.length();i++){
                        JSONObject period_object= periodsArray.getJSONObject(i);
                        int period_id=period_object.getInt("W_ID");
                        String period_name=getPeriodById(period_id);
                        periodsNames=period_name+",";
                    }
                     setViewData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {

            }
        });}
        public String getPeriodById(int period_id){
            ArrayList<importatnt>periods=importatnt.workShift();
            for(int i=0; i<periods.size();i++){
                int period_id2=periods.get(i).getqTypeId();
                String period_name=periods.get(i).getqName();
                if (period_id == period_id2) {
                    return period_name;
                }
                }
                return  null;
            }
             public void setViewData(){
                 period.setText(periodsNames);
                 type.setText(vacationObj.getvAmount());
                 desc.setText(vacationObj.getvDescription());
                 importatnt.check(vacationObj.getNote(),Note);
                 Date.setText(vacationObj.getvStartDate());
                 // type.setText(vacationObj.getvSlice());
                 String statustxt= vacationObj.getvStatues();
                 //status.setText(statustxt);
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



