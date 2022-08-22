package com.max.rm.hr.Employee.infoPkg;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.max.rm.hr.Employee.Api;
import com.max.rm.hr.Employee.RequestInterface;
import com.max.rm.hr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class info_docs extends Fragment {
    RecyclerView docs_rc;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.info_docs,null);
        docs_rc=(RecyclerView) v.findViewById(R.id.docs_list);
        getActivity().setTitle(getString(R.string.documents));
         Bundle b= getArguments();
         String funName= b.getString("fun_name");
         viewData(funName);
          return  v;
    }
    public void viewData(String fun_name){
        Api api= new Api(getActivity());
        SharedPreferences shared= getActivity().getSharedPreferences("user",0);
        String org_id=shared.getString("org_id","");
        final String emp_code=shared.getString("emp_code","");
        api.getEmpData(org_id,emp_code,fun_name, new RequestInterface() {
            @Override

            public void onResponse(String response) {
                Log.d("responsen", response);
                //  progress.setVisibility(View.GONE);
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                Log.d("responsegover", n);

                try {
                    JSONObject ob= new JSONObject(n);
                    ArrayList<info_docs_class> docs_list= new ArrayList<>();
                    JSONArray docs_arr= ob.getJSONArray("Emp_Documents");
                    for( int i =0; i <docs_arr.length();i++){
                        JSONObject docObj= docs_arr.getJSONObject(i);
                        String docName= docObj.getString("ITEM_NMAR");
                        String docExpireDate_G= docObj.getString("EXPIREDT_G");
                        String docReleaseDate_G= docObj.getString("RELEASEDT_G");
                        String docExpireDate_H= docObj.getString("EXPIREDT_H");
                        String docReleaseDate_H= docObj.getString("RELEASEDT_H");
                        String EXPIRE_AFTER_DAYS= docObj.getString("EXPIRE_AFTER_DAYS");
                        int empCode= docObj.getInt("EMP_CODE");
                        String empName= docObj.getString("NM_AR");
                        double exp_day=0;
                        if(!EXPIRE_AFTER_DAYS.equals("null")){
                             exp_day=Double.parseDouble(EXPIRE_AFTER_DAYS);
                        }

                        info_docs_class doc= new info_docs_class(docName,docExpireDate_G,docReleaseDate_G,(int)exp_day,String.valueOf(empCode),empName);
                        docs_list.add(doc);

                    }
                    GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 1);
                    docs_rc.setLayoutManager(lLayout);
                    docs_rc.setHasFixedSize(true);
                    info_docs_adapter adapter= new info_docs_adapter(docs_list, getActivity());
                    docs_rc.setAdapter(adapter);

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
}
