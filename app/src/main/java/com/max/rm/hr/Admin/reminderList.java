package com.max.rm.hr.Admin;

import android.app.AlertDialog;
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
import com.max.rm.hr.Employee.infoPkg.rec_interface;
import com.max.rm.hr.R;
import com.max.rm.hr.dialog_interface;
import com.max.rm.hr.keys;
import com.max.rm.hr.normalWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class reminderList extends Fragment {
    RecyclerView tasksList;
    FloatingActionButton
            addBtn;
    ArrayList<reminderClass>tasks;
    Api api;
    boolean all,addButton;
    SharedPreferences shared;
    String emp_code,org_id,function_name,admin_emp_code;
    reminderAdapter adapter;
    TextView noData;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.info_experiance,null);
        getActivity().setTitle(getString(R.string.experience));
        tasksList=(RecyclerView)v.findViewById(R.id.experiance_list);
        addBtn=(FloatingActionButton)v.findViewById(R.id.addBtn);
        noData=(TextView)v.findViewById(R.id.noData);
        getActivity().setTitle(getString(R.string.tasks));
        Bundle bundle= getArguments();
        tasks= new ArrayList<>();
        all=bundle.getBoolean(keys.allEmployee);
        addButton= bundle.getBoolean(keys.addButton);
        function_name=bundle.getString(keys.CLASS_NAME);
        api = new Api(getActivity());
        shared = getActivity().getSharedPreferences("user", 0);
        org_id = shared.getString("org_id", "");
        admin_emp_code=shared.getString("admin_emp_code","");

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment= new addEditeReminder();
                Bundle b= new Bundle();
                b.putString(keys.CLASS_NAME,"AddNew_Reminder");
                fragment.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"addEditeReminder").addToBackStack("addEditeReminder").commit();
            }
        });
        if(!addButton){
            addBtn.setVisibility(View.GONE);
        }
        if(all){
            emp_code="";
        }
        else{

            emp_code = shared.getString("emp_code", "");
        }
        getAllTasks(function_name,emp_code);

        return  v;
    }
     public void getAllTasks(String function_name,String emp_code){
        Log.d("empcode", emp_code);
        Log.d("function_name", function_name);

        api.getTasks(function_name, org_id, emp_code, new RequestInterface() {
            @Override
            public void onResponse(String response) {
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                Log.d("responseTasks", n);
                Log.d("responsen", response);
                //  progress.setVisibility(View.GONE);

                try {
                    JSONObject ob= new JSONObject(n);
                    JSONArray exp_arr= ob.getJSONArray("DataTable");
                    if(exp_arr.length()<=0){
                           noData.setVisibility(View.VISIBLE);
                           noData.setText(getString(R.string.no_date)+" "+getString(R.string.tasks));

                    }
                    for( int i =0; i <exp_arr.length();i++){
                        JSONObject object= exp_arr.getJSONObject(i);
                        String date=object.getString("DT_G");
                        String time=object.getString("TIME");
                        String title=object.getString("NMAR");
                        String description=object.getString("NOTE");
                        String taskTime=object.getString("INTIME");
                        String taskDate=object.getString("INDATE");
                        int task_id=object.getInt("CODE");
                        int isRepeat=object.getInt("ISREPEAT");
                        reminderClass task= new reminderClass(String.valueOf(task_id),title,description,date,time,taskTime, taskDate,isRepeat);
                        tasks.add(task);


                    }
                     adapter= new reminderAdapter(tasks, getActivity(), new rec_interface() {
                        @Override
                        public void onRecItemSelected(int position, View view) {
                           showMenu(position, view);
                        }
                    });
                    GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 1);
                    tasksList.setLayoutManager(lLayout);
                    tasksList.setHasFixedSize(true);
                    tasksList.setAdapter(adapter);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }}

            @Override
            public void onError() {

            }
        });

     }
    public void showMenu(int pos,View view) {

        final PopupMenu menu= new PopupMenu(getActivity(),view);
        menu.getMenuInflater().inflate(R.menu.details_edit_delete, menu.getMenu());
        if(function_name.equals("Get_Avilable_Reminders")){
           menu.getMenu().getItem(3).setVisible(true);
           menu.getMenu().getItem(2).setVisible(false);
           menu.getMenu().getItem(1).setVisible(false);
        }
        onMenuItemSelected(menu,pos);
        menu.show();


    }
    public void onMenuItemSelected(PopupMenu menu , final int position){
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id=menuItem .getItemId();
                if(id==R.id.edit){
                    Fragment fragment= new addEditeReminder();
                    Bundle b= new Bundle();
                    b.putString(keys.CLASS_NAME,"Edit_Reminder");
                    b.putSerializable(keys.TASK,tasks.get(position));
                    fragment.setArguments(b);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"addEditeReminder").addToBackStack("addEditeReminder").commit();

                }
                else  if(id==R.id.delete2){
                    deleteWindow(position);

                }
                else  if(id==R.id.details){
                    Fragment fragment= new reminderDetails();
                    Bundle b= new Bundle();
                    b.putSerializable(keys.TASK,tasks.get(position));
                    fragment.setArguments(b);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"reminderDetails").addToBackStack("reminderDetails").commit();


                }
                else  if(id==R.id.hide){
                    // hide task
                    deleteTask(Integer.parseInt(tasks.get(position).getTask_id()),position,"SetAs_Hidden_Reminder",emp_code);

                }
                return false;
            }
        });
    }


    public void deleteTask(int record_id, final int item_position, String functionName, String user_id){
        Log.d("emp_code", emp_code);
        Log.d("org", org_id);

        api.Delete("Reminders", org_id, user_id, functionName,String.valueOf( record_id), new
                RequestInterface() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        String n= response.substring(1,response.length()-1);
                        n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                        try {
                            JSONObject object= new JSONObject(n);
                            String msg=object.getString("Msg");
                            if(msg.equals("Success")){
                                tasks.remove(item_position);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();

                            }
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
    public void deleteWindow(final int position){
        normalWindow.dialogOkCancel(getString(R.string.delete )+"?", new dialog_interface() {
            @Override
            public void onDialogOkClick(AlertDialog alertDialog) {
                deleteTask(Integer.parseInt(tasks.get(position).getTask_id()),position,"Delete_Reminder",admin_emp_code);
            }

            @Override
            public void onDialogCancelClick(AlertDialog alertDialog) {

            }
        }, getActivity());
    }
}
