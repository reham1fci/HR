package com.max.rm.hr.Admin;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.max.rm.hr.ChatPkg.Employees;
import com.max.rm.hr.ChatPkg.allChats;
import com.max.rm.hr.ChatPkg.chat;
import com.max.rm.hr.Employee.Api;
import com.max.rm.hr.Employee.RequestInterface;
import com.max.rm.hr.Employee.attendancePkg.MyAttendance;
import com.max.rm.hr.Employee.employee_class;
import com.max.rm.hr.Employee.infoPkg.MyInformation;
import com.max.rm.hr.Employee.payrollPkg.MyPayroll;
import com.max.rm.hr.Employee.requestpkg.requestsTypes;
import com.max.rm.hr.R;
import com.max.rm.hr.dialog_interface;
import com.max.rm.hr.keys;
import com.max.rm.hr.normalWindow;
import com.max.rm.hr.rec_interface2;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class employeesList extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static String type = "";
    RecyclerView employeesRc;
    SwipeRefreshLayout swipeRefreshLayout;
    Api api;
    SharedPreferences shared;
    String emp_code, org_id;
    SearchView search_view;
    empListAdapter adapter;
    ArrayList<employee_class> empList = new ArrayList<>();
    FloatingActionButton done;
    Bundle bundle;
    TextView selectEmpView;
    boolean chatScreen = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.employees_list, null);
        employeesRc = (RecyclerView) v.findViewById(R.id.employeesList);
        search_view = (SearchView) v.findViewById(R.id.search);
        selectEmpView = (TextView) v.findViewById(R.id.selectEmp);
        done = (FloatingActionButton) v.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                group_info();
            }
        });
       // getCallerFragment();
     //  String name= getCallerFragment();
    //
        Log.d("flag", String.valueOf(allChats.flag));

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        search_view.setActivated(true);
        search_view.setQueryHint(getString(R.string.search));
        search_view.onActionViewExpanded();
        search_view.setIconified(false);
        search_view.clearFocus();
        bundle = getArguments();

        if (bundle != null && bundle.containsKey(keys.CLASS_NAME)) {
            chatScreen = true;
            type = bundle.getString(keys.CLASS_NAME);
            if (type.equals("group")) {
                done.setVisibility(View.VISIBLE);
            }

        }

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("text submit", query);
                adapter.getFilter().filter(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.d("text change", newText);
                adapter.getFilter().filter(newText);


                return true;
            }
        });
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        int flag= allChats.flag;

                                        if(flag==1){
                                            getActivity().onBackPressed();

                                        }
                                        else{
                                        intialize();
                                        swipeRefreshLayout.setRefreshing(true);

                                        getEmployeesList();}
                                    }
                                }
        );
        getActivity().setTitle(getString(R.string.employees));
        return v;
    }

    public void getEmployeesList() {
        final ArrayList<employee_class> emp_list = new ArrayList<>();
        api.getAllEmployeesData(org_id, "Get_All_EmployeesInfo", new RequestInterface() {
            @Override
            public void onResponse(String response) {
                String n = response.substring(1, response.length() - 1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                Log.d("employees", n);

                try {
                    JSONObject ob = new JSONObject(n);
                    JSONArray docs_arr = ob.getJSONArray("VW_EMPLOYEELIST");
                    for (int i = 0; i < docs_arr.length(); i++) {
                        JSONObject empObj = docs_arr.getJSONObject(i);
                        String empName = empObj.getString("NM_AR");
                        int empCode = empObj.getInt("EMP_CODE");
                        String empJob = empObj.getString("JOB_TXT");
                        int jobDegree = empObj.getInt("DGR_CODE");
                        emp_list.add(new employee_class(String.valueOf(empCode), empName, empJob, jobDegree));

                    }
                    emplyeesRcView(emp_list);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {

            }
        });

    }


    public void emplyeesRcView(final ArrayList<employee_class> emp_list) {
        adapter = new empListAdapter(emp_list, getActivity(), new rec_interface2() {
            @Override
            public void onRecItemSelected(final int position, View view, final ArrayList<employee_class> list) {
                int job_degree = list.get(position).getJob_degree();
                String emp_name = list.get(position).getNameAr();
                final String select_emp_code = list.get(position).getEMP_CODE();
                if (chatScreen) { // from chat code
                    type = bundle.getString(keys.CLASS_NAME);
                    if (type.equals("chat")) { // chat between two members
                        onChatMenuItemSelected(list.get(position));
                    } else if (type.equals("add_member")) { // add member to group chat
                        final String chat_key = bundle.getString("chat_key");
                        FirebaseDatabase firebase_instance = FirebaseDatabase.getInstance();
                        final DatabaseReference sRef = firebase_instance.getReference("chat");
                        ArrayList<Employees> members = (ArrayList<Employees>) bundle.getSerializable("members");
                        int member_index= checkMemberFound(members,select_emp_code);
                        if (member_index>=0) { // member found in group
                            if (!members.get(member_index).getEnd_at().equals("null")) {// member found but leave
                                backMemberToGroup(sRef,chat_key,select_emp_code);
                            } else {
                                normalWindow.window(getString(R.string.member_already_exist), getActivity());
                            }
                        }
                        else {
                          addNewMemberToGroupChat(select_emp_code,emp_name,sRef,chat_key);
                        }
                    }
                    else { // type group select group member
                        employee_class empObject = list.get(position);
                         selectGroupMembers(empObject);
                    }
                }
                else { // when admin click employees

                    showMenu(select_emp_code, emp_name, list.get(position), job_degree, view);
                }
            }


        });

        GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 1);
        employeesRc.setLayoutManager(lLayout);
        employeesRc.setHasFixedSize(true);
        employeesRc.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

    }
    public void showMenu(final String emp_code, String emp_name, employee_class emp, int jobDegree, View view) {

        final PopupMenu menu = new PopupMenu(getActivity(), view);
        menu.getMenuInflater().inflate(R.menu.admin_emplyee_select_menu, menu.getMenu());
        onMenuItemSelected(menu, emp_code, emp_name, emp, jobDegree);
        menu.show();
    }

    public void onChatMenuItemSelected( final employee_class emp) {
        Fragment f = new chat();
        Bundle bundle = new Bundle();
        SharedPreferences shared = getActivity().getSharedPreferences("user", 0);
        if (shared.getBoolean("admin", false)) {
            bundle.putString("sender_empCode", shared.getString("admin_emp_code", ""));
        } else {
            bundle.putString("sender_empCode", shared.getString("emp_code", ""));
        }
        empList.add(emp);
        bundle.putSerializable("receivers", empList);
        bundle.putString("sender_empName", shared.getString("name", ""));// function name
        bundle.putString("chat_key", "null");
        bundle.putString("chat_title",emp.getNameAr());
        f.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, f, "chat").addToBackStack("chat").commit();
    }
    public void onMenuItemSelected(PopupMenu menu, final String emp_code, final String emp_name, final employee_class emp, final int jobDegree) {
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("user", 0).edit();
                editor.putInt("jobDegree", jobDegree);
                editor.putString("emp_code", emp_code);
                editor.commit();
                int id = menuItem.getItemId();
                if (id == R.id.empInfo) {
                    Fragment f = new MyInformation();
                    transaction.replace(R.id.frame, f, "MyInformation").addToBackStack("MyInformation").commit();
                    return true;
                } else if (id == R.id.empAttendance) {
                    Fragment f = new MyAttendance();
                    Bundle bundle = new Bundle();
                    bundle.putString("fun_name", "Get_Emp_MonthlyAttend");
                    bundle.putString("fun_name_daily", "Get_Emp_DailyAttend");
                    f.setArguments(bundle);
                    transaction.replace(R.id.frame, f, "MyAttendance");
                    transaction.addToBackStack("MyAttendance");
                    transaction.commit();
                    return true;
                } else if (id == R.id.empPayroll) {
                    Fragment f = new MyPayroll();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(keys.allEmployee, false);
                    f.setArguments(bundle);
                    transaction.replace(R.id.frame, f, "MyPayroll");
                    transaction.addToBackStack("MyPayroll");
                    transaction.commit();
                    return true ;
                } else if (id == R.id.empRequest) {
                    Fragment f = new requestsTypes();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(keys.allEmployee, false);
                    bundle.putBoolean(keys.addButton, false);
                    f.setArguments(bundle);
                    transaction.replace(R.id.frame, f, "requestsTypes").addToBackStack("requestsTypes").commit();
                    return true;

                } else if (id == R.id.tasks) {
                    Fragment f = new reminderList();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(keys.allEmployee, false);
                    bundle.putString(keys.CLASS_NAME, "Get_Reminders");// function name
                    bundle.putBoolean(keys.addButton, true);
                    f.setArguments(bundle);
                    transaction.replace(R.id.frame, f, "reminderList").addToBackStack("reminderList").commit();
                    return true;

                } else if (id == R.id.chat) {
                    Fragment f = new chat();
                    Bundle bundle = new Bundle();
                    SharedPreferences shared = getActivity().getSharedPreferences("user", 0);
                    bundle.putString("sender_empCode", shared.getString("admin_emp_code", ""));// function name
                    empList.add(emp);
                    bundle.putSerializable("receivers", empList);
                    bundle.putString("sender_empName", shared.getString("name", ""));// function name
                    bundle.putString("chat_key", "null");
                    bundle.putString("chat_title",emp.getNameAr());// function name

                    f.setArguments(bundle);
                    transaction.replace(R.id.frame, f, "chat").addToBackStack("chat").commit();
                    return true;

                }
                return false;
            }
        });
    }


    @Override
    public void onRefresh() {
        getEmployeesList();
    }

    public void intialize() {
        api = new Api(getActivity());
        shared = getActivity().getSharedPreferences("user", 0);
        org_id = shared.getString("org_id", "");
        //org_id="001";
    }

    public void group_info() {
        if(empList.size()>0) {
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.group_info);
            final EditText gName = (EditText) dialog.findViewById(R.id.gName);
            final EditText gDescription = (EditText) dialog.findViewById(R.id.gDesc);
            FloatingActionButton done = (FloatingActionButton) dialog.findViewById(R.id.done);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String group_name = gName.getText().toString();
                    String group_desc = gDescription.getText().toString();
                    Fragment f = new chat();
                    Bundle bundle = new Bundle();
                    SharedPreferences shared = getActivity().getSharedPreferences("user", 0);
                    if (shared.getBoolean("admin", false)) {
                        bundle.putString("sender_empCode", shared.getString("admin_emp_code", ""));
                    } else {
                        bundle.putString("sender_empCode", shared.getString("emp_code", ""));
                    }

                    bundle.putSerializable("receivers", empList);
                    bundle.putString("sender_empName", shared.getString("name", ""));// function name
                    bundle.putString("chat_key", "group");
                    bundle.putString("gName", group_name);
                    bundle.putString("gDesc", group_desc);
                    f.setArguments(bundle);
                 //   getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, f, "chat").addToBackStack("chat").commit();
                  //

                    dialog.dismiss();

                }
            });
            dialog.show();
        }
        else{
            normalWindow.window("choose group member ",getActivity());
        }
    }
     public int checkMemberFound(ArrayList<Employees> members_list, String emp_code) {
         for (int i = 0; i < members_list.size(); i++) {
             String Emp_code = members_list.get(i).getEmp_code();
             if (Emp_code.equals(emp_code)) {
                 return i;
             }

         }
         return -1;
     }
     public void backMemberToGroup(final DatabaseReference  sRef, final String chat_key, final String emp_code){
         normalWindow.dialogOkCancel(getString(R.string.member_leave), new dialog_interface() {
             @Override
             public void onDialogOkClick(AlertDialog alertDialog) {
                 sRef.child(chat_key).child("member_child").child(emp_code).child("end_at").setValue("null");
                 getActivity().getSupportFragmentManager().popBackStack();
                 normalWindow.window(getString(R.string.done), getActivity());

             }

             @Override
             public void onDialogCancelClick(AlertDialog alertDialog) {
                 alertDialog.dismiss();
             }
         }, getActivity());
     }
      public void addNewMemberToGroupChat(String emp_code, String emp_name,final DatabaseReference  sRef, final String chat_key){
          // add member child for chat
        /*  String empcode = list.get(position).getEMP_CODE();
          String empname = list.get(position).getNameAr();*/
          Employees new_member = new Employees(emp_code, emp_name, "ay 7aga", "null", "null");
          sRef.child(chat_key).child("member_child").child(emp_code).setValue(new_member);
          getActivity().getSupportFragmentManager().popBackStack();
          normalWindow.window(getString(R.string.done), getActivity());
      }
       public void selectGroupMembers(employee_class empObject ){
           StringBuffer selectEmployee = new StringBuffer();
          // employee_class empObject = list.get(position);
           if (!empList.contains(empObject)) {
               empList.add(empObject);

           } else {
               empList.remove(empObject);
           }
           for (int i = 0; i < empList.size(); i++) {
               selectEmpView.setVisibility(View.VISIBLE);
               selectEmployee.append(empList.get(i).getEMP_CODE());
               selectEmployee.append(",");

           }

if(selectEmployee.length()>0){
           selectEmployee.deleteCharAt(selectEmployee.length() - 1);}
           selectEmpView.setText(selectEmployee.toString());
       }

    private void getCallerFragment(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        int count = getFragmentManager().getBackStackEntryCount();
        Log.d("count stack", String.valueOf(count));
        for (int i=0; i <count; i++){
            Log.d("f"+i,fm.getBackStackEntryAt(i).getName() );
        }

        //  Fragment f=
      /*    if(fm.getBackStackEntryAt(count - 2).getName().equals("employeesList")){
              Fragment fragment = new employeesList();
              fm.beginTransaction().remove(fragment).commit();

          }*/


    }
}
