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

public class vacation_details extends Fragment {
    Api api;
    SharedPreferences shared;
    String emp_code,org_id;
    int job_dgr;
    vacationClass vacationObj;
    TextView v_status, v_slice,v_startDate,v_endDate,v_dayNums,v_desc,v_rejectedNote,rejectedNoteView, v_Note;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.vaction_details,null);
        v_status=(TextView) v.findViewById(R.id.status);
        v_slice=(TextView) v.findViewById(R.id.v_slice);
        v_startDate=(TextView) v.findViewById(R.id.start_date);
        v_endDate=(TextView) v.findViewById(R.id.end_date);
        v_dayNums=(TextView) v.findViewById(R.id.nums);
        v_desc=(TextView) v.findViewById(R.id.v_desc);
        v_Note=(TextView) v.findViewById(R.id.v_Note);
        v_rejectedNote=(TextView) v.findViewById(R.id.rejected_note);
        rejectedNoteView=(TextView) v.findViewById(R.id.rejectedNoteView);
        Bundle bundle= getArguments();
         vacationObj=(vacationClass)bundle.getSerializable(keys.VACACTION);
        init();
        getVacationSliceList();
        return v;
    }
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
    public void setView(String slice_name){
        v_dayNums.setText(vacationObj.getvAmount());
        v_desc.setText(vacationObj.getvDescription());
        //  importatnt.check(vactionObj.get);
        //   v_note.setText(vactionObj.getvDescription());

        v_startDate.setText(vacationObj.getvStartDate());
        v_endDate.setText(vacationObj.getvEndDate());
        v_slice.setText(slice_name);
        String status= vacationObj.getvStatues();
        v_status.setText(status);
        String rejected= vacationObj.getvRejectedNote();

        if(rejected.equals("null")){
            rejectedNoteView.setVisibility(View.GONE);
            v_rejectedNote.setVisibility(View.GONE);

        }
        else{
            v_rejectedNote.setText(rejected);

        }
        getStatues(status,v_status);
    }
    public  void init(){
        getActivity().setTitle(getString(R.string.vacation_request));

        api = new Api(getActivity());
        shared = getActivity().getSharedPreferences("user", 0);
        org_id = shared.getString("org_id", "");
        emp_code=shared.getString("emp_code","");
        job_dgr=shared.getInt("jobDegree",0);


    }
    public void  getVacationSliceList(){

        final ArrayList<importatnt> sliceList= new ArrayList<>();
        api.getTypes(org_id, "Get_All_VacationTypes","null","null", "null","null","null",new RequestInterface() {
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
                        int loan_code=loanObject.getInt("CODE");
                        String loan_Name=loanObject.getString("NMAR");
                        sliceList.add(new importatnt(loan_code,loan_Name));

                    }
                    String slice_name=  getSliceById(Integer.parseInt(vacationObj.getvSlice()),sliceList);
                    Log.d("slice name", slice_name);
                       setView(slice_name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError() {
                Log.d("responsev","error");

            }
        });}
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
