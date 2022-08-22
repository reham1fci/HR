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

public class info_family extends Fragment {
     RecyclerView familyRc;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.info_family,null);
        familyRc=(RecyclerView) v.findViewById(R.id.family_list);
        getActivity().setTitle(getString(R.string.family));
        viewData();
        return  v;
    }
    public void viewData(){
        Api api= new Api(getActivity());
        SharedPreferences shared= getActivity().getSharedPreferences("user",0);
        String org_id=shared.getString("org_id","");
        String emp_code=shared.getString("emp_code","");
        //  String org_id="001";
        //  String emp_code="9101";
        api.getEmpData(org_id,emp_code,"Get_Emp_FamilyData", new RequestInterface() {
            @Override

            public void onResponse(String response) {
                Log.d("responsen", response);
                //  progress.setVisibility(View.GONE);
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");

                //  String jsonResponse="{\n"+"\"employee\":"+n+"}";
                Log.d("responsefa", n);

                try {
                    ArrayList<info_family_class> family_list= new ArrayList<>();

                    JSONObject ob= new JSONObject(n);
                    JSONArray familyArr=ob.getJSONArray("Emp_Documents");
                    for(int i=0;i<familyArr.length();i++){
                        JSONObject familyObject=familyArr.getJSONObject(i);
                        String familyRelation=familyObject.getString("ITEM_NMAR");
                      //  String family=familyObject.getString("");
                        String docExpireDate_G= familyObject.getString("EXPIREDT_G");
                        String docReleaseDate_G= familyObject.getString("RELEASEDT_G");
                        String docExpireDate_H= familyObject.getString("EXPIREDT_H");
                        String docReleaseDate_H= familyObject.getString("RELEASEDT_H");
                        info_family_class family= new info_family_class(familyRelation,docExpireDate_G,docReleaseDate_G);
                        family_list.add(family);

                    }

                    GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 1);
                    familyRc.setLayoutManager(lLayout);
                    familyRc.setHasFixedSize(true);
                    info_family_adapter adapter= new info_family_adapter(family_list, getActivity());
                    familyRc.setAdapter(adapter);



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
