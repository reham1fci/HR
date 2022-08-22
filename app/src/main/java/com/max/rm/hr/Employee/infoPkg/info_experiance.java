package com.max.rm.hr.Employee.infoPkg;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.max.rm.hr.Employee.Api;
import com.max.rm.hr.Employee.RequestInterface;
import com.max.rm.hr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class info_experiance  extends Fragment{
    RecyclerView expert_list;
    FloatingActionButton addBtn;

     ArrayList<info_exp_class>exp_list;
    info_exp_adapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.info_experiance,null);
        getActivity().setTitle(getString(R.string.experience));
        expert_list=(RecyclerView)v.findViewById(R.id.experiance_list);
        addBtn=(FloatingActionButton)v.findViewById(R.id.addBtn);
         exp_list= new ArrayList<>();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment= new info_exp_add();
                Bundle b= new Bundle();
                b.putString("fun","AddNew_Expert");
                fragment.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();
            }
        });
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
        api.getEmpData(org_id,emp_code,"Get_Emp_Experts", new RequestInterface() {
            @Override

            public void onResponse(String response) {
                Log.d("responsen", response);
                //  progress.setVisibility(View.GONE);
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                Log.d("responsegover", n);

                try {
                    JSONObject ob= new JSONObject(n);
                    JSONArray exp_arr= ob.getJSONArray("JS_EMP_QUALIFICATION");
                    for( int i =0; i <exp_arr.length();i++){
                        JSONObject exp_object= exp_arr.getJSONObject(i);
                        String exp_name= exp_object.getString("EXP_TITLE");
                        String exp_details= exp_object.getString("EXP_DETAIL");
                        String exp_years= exp_object.getString("EXP_YEARS");
                        int qCode= exp_object.getInt("CODE");

                        info_exp_class exp= new info_exp_class(exp_name,exp_details,exp_years,String.valueOf(qCode));
                        exp_list.add(exp);

                    }
                    GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 1);
                    expert_list.setLayoutManager(lLayout);
                    expert_list.setHasFixedSize(true);
                     adapter= new info_exp_adapter(exp_list, getActivity(), new rec_interface() {
                        @Override
                        public void onRecItemSelected(final int position, View view) {
                            final PopupMenu menu= new PopupMenu(getActivity(),view);
                            menu.getMenuInflater().inflate(R.menu.edit_delete,menu.getMenu());
                            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                     int id= menuItem.getItemId();
                                     if(id==R.id.edit){
                                         Fragment fragment= new info_exp_add();
                                         Bundle b= new Bundle();
                                         b.putString("fun","Edit_Expert");
                                         b.putString("id",exp_list.get(position).getRecord_id());
                                         b.putString("years",exp_list.get(position).getJob_years());
                                         b.putString("job_title",exp_list.get(position).getJob_name());
                                         b.putString("job_details",exp_list.get(position).getJob_details());

                                         fragment.setArguments(b);
                                         getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();

                                     }
                                      else  if(id==R.id.delete){
deleteExpert(exp_list.get(position).getRecord_id(), position);
                                     }


                                    return false;
                                }
                            });
                            menu.show();
                        }
                    });
                    expert_list.setAdapter(adapter);

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
    public void deleteExpert(String id , final int position){
        final Api api= new Api(getActivity());
        SharedPreferences shared= getActivity().getSharedPreferences("user",0);

        String org_id=shared.getString("org_id","");
        String emp_code=shared.getString("emp_code","");
        api.Delete("EmpProcess",org_id, emp_code, "Delete_Expert", id, new RequestInterface() {
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
                    //    f.onDelete(position);
                         exp_list.remove(position);
                         adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();

                        // activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new info_qualification()).commit();
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
