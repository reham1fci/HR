package com.androidmax.max.hr.Employee.infoPkg;

import android.app.Dialog;
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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmax.max.hr.Employee.Api;
import com.androidmax.max.hr.Employee.RequestInterface;
import com.androidmax.max.hr.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class info_qualification_add extends Fragment {
    EditText qMajorEd,qNoteEd;

    TextView qDateEd;
    Spinner qTypeSp;
    Button saveBtn;
    String qTypeTx,qDateTx,fun_name ,recordId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.info_qualification_add,null);
        qMajorEd=(EditText)v.findViewById(R.id.q_major);
        qDateEd=(TextView)v.findViewById(R.id.q_date);
        qNoteEd=(EditText)v.findViewById(R.id.q_note);
        qTypeSp=(Spinner) v.findViewById(R.id.q_type);
        saveBtn=(Button) v.findViewById(R.id.save);
        Bundle b=getArguments();
         fun_name=b.getString("fun");
        if(fun_name.equals("Edit_Qulification")){
            recordId=    b.getString("id");
            b.getString("q_name");
          qDateEd.setText( b.getString("q_date"));
           qMajorEd.setText( b.getString("q_major"));
          qNoteEd.setText(  b.getString("q_note"));
        }
        else {
            recordId="null";
        }
        qDateEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d= new Dialog(getActivity());
                d.setContentView(R.layout.date);
                CalendarView calendarView= (CalendarView)d.findViewById(R.id.calendarView);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                        qDateTx=       String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                        //Log.d("date",date_str);
                        qDateEd.setText(qDateTx);
                    }

                });
                Button done= (Button)d.findViewById(R.id.done);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
                d.show();
            }


        });

        ArrayAdapter<String>adapter= new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item);
        importatnt im= new importatnt();
       final ArrayList<importatnt> q_types=im.qTypes();
       for(int i=0; i<q_types.size();i++){
           adapter.add(q_types.get(i).getqName());
       }
       qTypeSp.setAdapter(adapter);
       qTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               qTypeTx= String.valueOf(q_types.get(i).getqTypeId());
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             String qMajorTx= qMajorEd.getText().toString();
             String qNoteTx= qNoteEd.getText().toString();
             String dateTxt= qDateEd.getText().toString();

             if(qMajorTx.isEmpty()){
                 qMajorEd.setError(getString(R.string.enter)+getString(R.string.major));

             }
                else if(dateTxt.isEmpty()){
                    qMajorEd.setError(getString(R.string.enter)+getString(R.string.date));

                }
             else{
                 addQualification(recordId,qTypeTx,qMajorTx,dateTxt,qNoteTx,fun_name);
             }

            }
        });
        return v;

    }
    public void addQualification(String recordId,String q_type,String q_major,String q_date,String q_note,String funName){
        Api api = new Api(getActivity());


        SharedPreferences shared= getActivity().getSharedPreferences("user",0);

        String org_id=shared.getString("org_id","");
        String emp_code=shared.getString("emp_code","");
        api.addQualification(recordId,org_id, emp_code, funName, q_type, q_note, q_date, q_major, new RequestInterface() {
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
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,new info_qualification()).commit();


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
