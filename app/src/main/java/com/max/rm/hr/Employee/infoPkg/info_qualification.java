package com.androidmax.max.hr.Employee.infoPkg;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.androidmax.max.hr.Employee.Api;
import com.androidmax.max.hr.Employee.RequestInterface;
import com.androidmax.max.hr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class info_qualification  extends Fragment  {
    RecyclerView qualRc;

    info_qualification_adapter adapter;
    FloatingActionButton addQ;
    Api api;
    ArrayList<info_qualification_class> q_list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.info_qualification,null);
        qualRc=(RecyclerView) v.findViewById(R.id.qualification_list);
        addQ=(FloatingActionButton)v.findViewById(R.id.addBtn) ;
        getActivity().setTitle(getString(R.string.qualification));
         api= new Api(getActivity());
         q_list= new ArrayList<>();

        addQ.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        addQualification();
    }
});
        viewData();
        return  v;
    }
    public void addQualification(){

        Fragment fragment= new info_qualification_add();
        Bundle b= new Bundle();
        b.putString("fun","AddNew_Qulification");
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();
    }
    public void viewData(){
        SharedPreferences shared= getActivity().getSharedPreferences("user",0);
        String org_id=shared.getString("org_id","");
        String emp_code=shared.getString("emp_code","");
        //  String org_id="001";
        //  String emp_code="9101";
        api.getEmpData(org_id,emp_code,"Get_Emp_Qualifications", new RequestInterface() {
            @Override

            public void onResponse(String response) {
                Log.d("responsen", response);
                //  progress.setVisibility(View.GONE);
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");

                //  String jsonResponse="{\n"+"\"employee\":"+n+"}";
                Log.d("responsefa", n);

                try {
                    JSONObject ob= new JSONObject(n);
                    JSONArray q_arr=ob.getJSONArray("JS_EMP_QUALIFICATION");
                    for(int i=0;i<q_arr.length();i++){
                        JSONObject q_object=q_arr.getJSONObject(i);
                        int q_id=q_object.getInt("Q_TYPE");
                        HashMap<String,String>Qmap=importatnt.Get_QualificationTypes();
                        String q_name=Qmap.get(String.valueOf(q_id));
                        String q_date=q_object.getString("Q_DATE");
                        String q_note= q_object.getString("NOTE");
                        String q_major= q_object.getString("MAJOR");
                        int qCode= q_object.getInt("CODE");
                        info_qualification_class qObject= new info_qualification_class(String.valueOf(qCode),q_name,q_date,q_major,q_note);
                        q_list.add(qObject);

                    }

                   GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 1);
                    qualRc.setLayoutManager(lLayout);
                    qualRc.setHasFixedSize(true);
                  /*   adapter= new info_qualification_adapter(q_list, getActivity(), new refreshList() {
                         @Override
                         public void onDelete(int position) {
                             q_list.remove(position);
                             adapter.notifyDataSetChanged();
                         }

                         @Override
                         public void onEdit(int position, String id) {
                             Fragment fragment= new info_qualification_add();
                             Bundle b= new Bundle();
                             b.putString("id",id);
                             b.putString("fun","Edit_Qulification");
                             b.putString("q_name",q_list.get(position).getQualification_name());
                             b.putString("q_date",q_list.get(position).getQualification_date());
                             b.putString("q_major",q_list.get(position).getQualification_major());
                             b.putString("q_note",q_list.get(position).getQualification_note());

                             fragment.setArguments(b);
                             getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();

                         }
                     });*/
                    adapter= new info_qualification_adapter(q_list, getActivity(), new rec_interface() {
                        @Override
                        public void onRecItemSelected(final int position, View view) {
                            final PopupMenu menu= new PopupMenu(getActivity(),view);
                            menu.getMenuInflater().inflate(R.menu.edit_delete,menu.getMenu());
                            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    int id= menuItem.getItemId();
                                    if(id==R.id.edit){
                                        Fragment fragment= new info_qualification_add();
                                        Bundle b= new Bundle();
                                        b.putString("id",q_list.get(position).getQ_id());
                                        b.putString("fun","Edit_Qulification");
                                        b.putString("q_name",q_list.get(position).getQualification_name());
                                        b.putString("q_date",q_list.get(position).getQualification_date());
                                        b.putString("q_major",q_list.get(position).getQualification_major());
                                        b.putString("q_note",q_list.get(position).getQualification_note());

                                        fragment.setArguments(b);
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();

                                    }
                                    else  if(id==R.id.delete){
                                        deleteQualification(q_list.get(position).getQ_id(),position);
                                    }


                                    return false;
                                }
                            });
                            menu.show();
                        }
                    });


                    qualRc.setAdapter(adapter);



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
    public void deleteQualification(String id, final int position){
        final Api api= new Api(getActivity());
        SharedPreferences shared= getActivity().getSharedPreferences("user",0);

        String org_id=shared.getString("org_id","");
        String emp_code=shared.getString("emp_code","");
        api.Delete("EmpProcess",org_id, emp_code, "Delete_Qulification", id, new RequestInterface() {
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
                          q_list.remove(position);
                          adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();

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

   /* @Override
    public void onDelete(int pos) {
        q_list.remove(pos);
       adapter= new info_qualification_adapter(q_list, getActivity(), new info_qualification());
        adapter.notifyDataSetChanged();
    }*/
}
