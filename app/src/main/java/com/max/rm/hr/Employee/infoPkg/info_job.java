package com.max.rm.hr.Employee.infoPkg;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.max.rm.hr.Employee.Api;
import com.max.rm.hr.Employee.RequestInterface;
import com.max.rm.hr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class info_job extends Fragment {
    TextView mangement_place, job_degree, sponsor, emp_group,bank,bank_account,job_hour
            ,job_date,job_start_date,job_end_date,contract_type,emp_status, lastDirectJobDate;
    ProgressBar progress;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.info_job, null);
        getActivity().setTitle(getString(R.string.job_data));

        mangement_place=(TextView)v.findViewById(R.id.managementPlace) ;
        progress=(ProgressBar) v.findViewById(R.id.progress) ;
        job_degree=(TextView)v.findViewById(R.id.jobDegree) ;
        sponsor=(TextView)v.findViewById(R.id.sponsor) ;
        emp_group=(TextView)v.findViewById(R.id.empGroup) ;
        bank=(TextView)v.findViewById(R.id.bank) ;
        bank_account=(TextView)v.findViewById(R.id.bankAccount) ;
        job_hour=(TextView)v.findViewById(R.id.jobHour) ;
        job_date=(TextView)v.findViewById(R.id.jobDate) ;
        job_start_date=(TextView)v.findViewById(R.id.jobStartDate) ;
        job_end_date=(TextView)v.findViewById(R.id.jobEndDate) ;
        contract_type=(TextView)v.findViewById(R.id.contractType) ;
        emp_status=(TextView)v.findViewById(R.id.empStatus) ;
        lastDirectJobDate=(TextView)v.findViewById(R.id.jobDirectlyDate) ;
        viewData();
        return v;
    }
    public void viewData(){
        Api api= new Api(getActivity());
        SharedPreferences shared= getActivity().getSharedPreferences("user",0);
        String org_id=shared.getString("org_id","");
         String emp_code=shared.getString("emp_code","");
        //String org_id="001";
       // String emp_code="9101";
        api.getEmpData(org_id,emp_code,"Get_FunctionalData", new RequestInterface() {
            @Override
            public void onResponse(String response) {
                progress.setVisibility(View.GONE);
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
              //  n = n.replaceAll("\\s+","");

               // String jsonResponse="{\n"+"\"employee\":"+n+"}";
                Log.d("responsejob", n);

                try {
                    JSONObject fileObj= new JSONObject(n);
                   JSONArray arr=fileObj.getJSONArray("FunctionalData");
                    JSONObject emp= arr.getJSONObject(0);
                    int EMP_CODE=emp.getInt("EMP_CODE");
                    String managementPlace_txt=emp.getString("MGRLOCATION_TXT");
                    String jobDegree_txt=emp.getString("DGR_TXT");
                    String job_txt=emp.getString("JOB_TXT");
                    String sponsor_txt=emp.getString("SPONSER_TXT");
                    String empGroup_txt=emp.getString("GROUP_TXT");
                    String bank_txt=emp.getString("BANK_TXT");
                    String bankAccount_text=emp.getString("BANK_ACCOUNT");
                    String jobHireDateG_text=emp.getString("HIREDATE_G");
                    String jobHireDateH_text=emp.getString("HIREDATE_H");
                    String empState_txt=emp.getString("STATE_TXT");
                    String directJobDateG_txt=emp.getString("DIRCWORK_G");
                    String directJobDateH_txt=emp.getString("DIRCWORK_H");
                    String jobHour_txt=emp.getString("WORKHOURS");
                    String contractType_txt=emp.getString("AGREMENT_TYPE_TXT");

                    String jobStartG_txt=emp.getString("AGREMENT_START_G");
                    String jobStartH_txt=emp.getString("AGREMENT_START_H");
                    String jobEndG_txt=emp.getString("AGREMENT_END_G");
                    String jobEndH_txt=emp.getString("AGREMENT_END_H");



                    check(managementPlace_txt,mangement_place);
                    check(jobDegree_txt, job_degree);
                    check(sponsor_txt,sponsor);
                    check(empGroup_txt,emp_group);
                    check(bank_txt,bank);
                    check(bankAccount_text,bank_account);
                    check(jobHireDateG_text,job_date);
                    check(jobStartG_txt,job_start_date);
                    check(jobEndG_txt,job_end_date);
                    check(jobHour_txt,job_hour);
                    check(empState_txt,emp_status);
                    check(contractType_txt,contract_type);
                    check(directJobDateG_txt,lastDirectJobDate);
/*nameEn.setText(nameEn_txt);
Email.setText(email_txt);
phone.setText(phone_txt);
//nationality.setText(nationality_txt);
religion.setText(religion_txt);
birthDate_G.setText(birthdate_g);
birthDate_H.setText(birthdate_h);
B_city.setText(birthcity_txt);
B_country.setText(birthcountry_txt);
note.setText(note_txt);
Gender.setText(gender_txt);
socialState.setText(soicalstate_txt);*/
                   /* byte[] byte_arr= Base64.decode(image_txt,Base64.DEFAULT);
                    Bitmap b= BitmapFactory.decodeByteArray(byte_arr,0,byte_arr.length);
                    image.setImageBitmap(b);*/
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
                Log.d("response", "error");

            }
        });
    }
    public void check( String text, TextView v){
        if (text.equals("null")){
            v.setText("");

        }
        else {
            v.setText(text);

        }
    }
}
