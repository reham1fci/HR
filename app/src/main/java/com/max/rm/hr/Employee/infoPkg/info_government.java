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


public class info_government extends Fragment {
    ProgressBar progress;
    TextView id_num, copy_num,id_release_date,id_end_date, insurance_company,insurance_type,policy_num,insurance_date,
    passport_num,passport_place,pass_releaseDate, pass_endDate ,officeWork_num,insurance_num;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.info_government, null);
        getActivity().setTitle(getString(R.string.government_data));

        progress=(ProgressBar) v.findViewById(R.id.progress) ;

        id_num=(TextView)v.findViewById(R.id.id_data);
        copy_num=(TextView)v.findViewById(R.id.copy_num);
        id_release_date=(TextView)v.findViewById(R.id.release_date);
        id_end_date=(TextView)v.findViewById(R.id.end_date);
        insurance_company=(TextView)v.findViewById(R.id.insurance_company);
        insurance_type=(TextView)v.findViewById(R.id.insurance_category);
        policy_num=(TextView)v.findViewById(R.id.insurance_policy);
        insurance_date=(TextView)v.findViewById(R.id.due_date);
        passport_num=(TextView)v.findViewById(R.id.passport_num);
        passport_place=(TextView)v.findViewById(R.id.passport_place);
        pass_releaseDate=(TextView)v.findViewById(R.id.passportRelease_date);
        pass_endDate=(TextView)v.findViewById(R.id.passportEnd_date);
        officeWork_num=(TextView)v.findViewById(R.id.work_office_num);
        insurance_num=(TextView)v.findViewById(R.id.insurance_num);
        viewData();


        return v;
    }
    public void viewData(){
        Api api= new Api(getActivity());
        SharedPreferences shared= getActivity().getSharedPreferences("user",0);
     String org_id=shared.getString("org_id","");
        String emp_code=shared.getString("emp_code","");
      //  String org_id="001";
      //  String emp_code="9101";
        api.getEmpData(org_id,emp_code,"Get_GovernmentData", new RequestInterface() {
            @Override
            public void onResponse(String response) {
                Log.d("responsegover", response);
progress.setVisibility(View.GONE);
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
               // n = n.replaceAll("\\s+","");

              //  String jsonResponse="{\n"+"\"employee\":"+n+"}";
              //  Log.d("responsegover", jsonResponse);

              try {
                    JSONObject fileObj= new JSONObject(n);
                    JSONArray arr=fileObj.getJSONArray("GovernmentData");
                    JSONObject emp= arr.getJSONObject(0);
                  ///////////////////ID data//////////////////////////////

                  String idNo_txt=emp.getString("ID_NO");
                    String idCopyno_txt=emp.getString("ID_COPYNO");
                    String idReleasedtG_txt=emp.getString("ID_RELEASEDT_G");
                    String idReleasedtH_txt=emp.getString("ID_RELEASEDT_H");
                    String idExpiredtG_txt=emp.getString("ID_EXPIREDT_G");
                    String idExpiredtH_txt=emp.getString("ID_EXPIREDT_H");
                    ///////////////////passport data//////////////////////////////
                    String passportNo_txt=emp.getString("PASSPORT_NO");
                    String passportPlace_txt=emp.getString("PASSPORT_RELPLACE");
                    String passportReleasedtG_txt=emp.getString("PASSPORT_RELEASEDT_G");
                    String passportReleasedtH_txt=emp.getString("PASSPORT_RELEASEDT_H");
                    String passportExpiredtG_txt=emp.getString("PASSPORT_EXPIREDT_G");
                    String passportExpiredtH_txt=emp.getString("PASSPORT_EXPIREDT_H");
                    //////////////////////office date///////////////////////////
                    String workofficeNo_txt=emp.getString("WORKOFFICE_NO");
                    String workofficeInsureno_txt=emp.getString("WORKOFFICE_INSURENO");
                    ///////////////////////////insurance///////////////////////////////
                    String insureCardcompanyTxt=emp.getString("INSURE_CARDCOMPANY_TXT");
                    String insureCardtypeTxt=emp.getString("INSURE_CARDTYPE_TXT");
                    String insureCarddateG=emp.getString("INSURE_CARDDATE_G");
                    String insureCarddateH=emp.getString("INSURE_CARDDATE_H");
                    String insureBolesano=emp.getString("INSURE_BOLESANO");


                    check(idNo_txt,id_num);
                    check(idCopyno_txt,copy_num);
                    check(idReleasedtG_txt,id_release_date);
                    check(idExpiredtG_txt,id_end_date);
                    check(passportNo_txt,passport_num);
                    check(passportReleasedtG_txt,pass_releaseDate);
                    check(passportExpiredtG_txt,pass_endDate);
                    check(passportPlace_txt,passport_place);
                    check(workofficeNo_txt,officeWork_num);
                    check(workofficeInsureno_txt,insurance_num);
                    check(insureCardcompanyTxt,insurance_company);
                    check(insureCardtypeTxt,insurance_type);
                    check(insureCarddateG,insurance_date);
                    check(insureBolesano,policy_num);
                } catch (JSONException e) {
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
