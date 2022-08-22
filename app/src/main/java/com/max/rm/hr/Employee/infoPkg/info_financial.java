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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.max.rm.hr.Employee.Api;
import com.max.rm.hr.Employee.RequestInterface;
import com.max.rm.hr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class info_financial extends Fragment {
    RecyclerView financialList;
    ProgressBar progress;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.info_financial, null);
        financialList=(RecyclerView) v.findViewById(R.id.financialList);
        getActivity().setTitle(getString(R.string.Financial_data));
       progress=(ProgressBar) v.findViewById(R.id.progress);
        viewData();
        return v;
    }
    public void viewData(){
        final ArrayList<info_financial_class> list= new ArrayList<>();
        Api api= new Api(getActivity());
        SharedPreferences shared= getActivity().getSharedPreferences("user",0);
        String org_id=shared.getString("org_id","");
        String emp_code=shared.getString("emp_code","");
       /* String org_id="001";
        String emp_code="9101";*/
        api.getEmpData(org_id,emp_code,"Get_FinancialData", new RequestInterface() {
            @Override
            public void onResponse(String response) {
                progress.setVisibility(View.GONE);
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
               // n = n.replaceAll("\\s+","");

              //  String jsonResponse="{\n"+"\"employee\":"+n+"}";
              //  Log.d("responsejob", n);

                try {
                    JSONObject fileObj= new JSONObject(n);
                    JSONArray arr=fileObj.getJSONArray("FinancialData");
                    for( int i=0 ; i<arr.length();i++){
                        JSONObject emp= arr.getJSONObject(i);
                        String name=emp.getString("NMAR");
                        String price=emp.getString("PRICE");
                        list.add(new info_financial_class(name,price))  ;
                    }
                    GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 1);
                    financialList.setLayoutManager(lLayout);
                    financialList.setHasFixedSize(true);
                    info_financial_adapter adapter= new info_financial_adapter(list, getActivity());
                    financialList.setAdapter(adapter);

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
