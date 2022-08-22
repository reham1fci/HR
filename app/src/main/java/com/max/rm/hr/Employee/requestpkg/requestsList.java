package com.max.rm.hr.Employee.requestpkg;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.max.rm.hr.Employee.Api;
import com.max.rm.hr.Employee.RequestInterface;
import com.max.rm.hr.Employee.infoPkg.importatnt;
import com.max.rm.hr.Employee.infoPkg.rec_interface;
import com.max.rm.hr.R;
import com.max.rm.hr.dialog_interface;
import com.max.rm.hr.keys;
import com.max.rm.hr.normalWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class requestsList extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    Api api;
    SharedPreferences shared;
    String emp_code,org_id, isUsed,isRejected;
    int job_dgr;
    //info_qualification_adapter adapter;
    FloatingActionButton addRequest;
   public static String type;
    vacationAdapter adapter;
    ArrayList<vacationClass> vList;
    public static boolean all,addButton;
     ArrayList<importatnt>PermissionTypeList;
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.requests_list,null);
        recyclerView=(RecyclerView) v.findViewById(R.id.qualification_list);
        addRequest=(FloatingActionButton)v.findViewById(R.id.addBtn) ;
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        Bundle b= getArguments();
        PermissionTypeList= new ArrayList<>();
         type=b.getString(keys.CLASS_NAME);
         all=b.getBoolean(keys.allEmployee);
        isRejected= b.getString(keys.IS_REJECTED);
        isUsed=    b.getString(keys.IS_USED);
         addButton=b.getBoolean(keys.addButton);
        init();
        if(!addButton){
            addRequest.setVisibility(View.GONE);
        }
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        swipeRefreshLayout.setRefreshing(true);

                                        if(all){
                                            adminRequests();
                                        }
                                        else{
                                            employeeRequest();
                                        }                                    }
                                }
        );

        addRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equals(keys.VACACTION)){

                    addVacation();
                }
              else  if(type.equals(keys.LOAN)){

                    addLoan();
                }
                else  if(type.equals(keys.CUSTODY)){

                    addCustody();
                }
                else  if(type.equals(keys.PERMISSION)){

                    addPermission();
                }
                else  if(type.equals(keys.OTHER)){

                    addOther();
                }
            }
        });

        return  v;
    }
    public void addVacation(){
        Fragment fragment= new Req_Vacation();
        Bundle b= new Bundle();
        b.putString(keys.CLASS_NAME,"AddNew_RQ_Vacation");
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"Req_Vacation").addToBackStack("Req_Vacation").commit();
    }
    public void addPermission(){
        Fragment fragment= new req_permission();
        Bundle b= new Bundle();
        b.putString(keys.CLASS_NAME,"AddNew_RQ_Permission");
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"req_permission").addToBackStack("req_permission").commit();
    }
    public void addCustody(){
        Fragment fragment= new Req_Custody();
        Bundle b= new Bundle();
        b.putString(keys.CLASS_NAME,"AddNew_RQ_Custody");
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"Req_Custody").addToBackStack("Req_Custody").commit();
    }
    public void addLoan(){
        Fragment fragment= new Req_loan();
        Bundle b= new Bundle();
        b.putString(keys.CLASS_NAME,"AddNew_RQ_Loan");
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"Req_loan").addToBackStack("Req_loan").commit();
    }
    public void addOther(){
        Fragment fragment= new req_another();
        Bundle b= new Bundle();
        b.putString(keys.CLASS_NAME,"AddNew_RQ_Other");
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"req_another").addToBackStack("req_another").commit();
    }
    public void editVacation( int position){
        Fragment fragment= new Req_Vacation();
        Bundle b= new Bundle();
        b.putString(keys.CLASS_NAME,"Edit_RQ_Vacation");
        b.putSerializable(keys.VACACTION,vList.get(position));
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"Req_Vacation").addToBackStack("Req_Vacation").commit();
    }
    public void editLoan( int position){
        Fragment fragment= new Req_loan();
        Bundle b= new Bundle();
        b.putString(keys.CLASS_NAME,"Edit_RQ_Loan");
        b.putSerializable(keys.LOAN,vList.get(position));
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"Req_loan").addToBackStack("Req_loan").commit();
    }
    public void editCustody( int position){
        Fragment fragment= new Req_Custody();
        Bundle b= new Bundle();
        b.putString(keys.CLASS_NAME,"Edit_RQ_Custody");
        b.putSerializable(keys.CUSTODY,vList.get(position));
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"Req_Custody").addToBackStack("Req_Custody").commit();
    }
    public void editPermission( int position){
        Fragment fragment= new req_permission();
        Bundle b= new Bundle();
        b.putString(keys.CLASS_NAME,"Edit_RQ_Permission");
        b.putSerializable(keys.PERMISSION,vList.get(position));
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"req_permission").addToBackStack("req_permission").commit();
    }
    public void editOther( int position){
        Fragment fragment= new req_another();
        Bundle b= new Bundle();
        b.putString(keys.CLASS_NAME,"Edit_RQ_Other");
        b.putSerializable(keys.OTHER,vList.get(position));
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"req_another").addToBackStack("req_another").commit();
    }
    public void detailsVacation( int position) {
        Fragment fragment= new vacation_details();
        Bundle b= new Bundle();
        b.putSerializable(keys.VACACTION,vList.get(position));
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"vacation_details").addToBackStack("vacation_details").commit();

    }public void detailsLoan( int position) {
        Fragment fragment= new loan_details();
        Bundle b= new Bundle();
        b.putSerializable(keys.LOAN,vList.get(position));
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"loan_details").addToBackStack("loan_details").commit();

    }
public void detailsCustody( int position) {
        Fragment fragment= new custody_details();
        Bundle b= new Bundle();
        b.putSerializable(keys.CUSTODY,vList.get(position));
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"custody_details").addToBackStack("custody_details").commit();

    }public void detailsOther( int position) {
        Fragment fragment= new other_details();
        Bundle b= new Bundle();
        b.putSerializable(keys.OTHER,vList.get(position));
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"other_details").addToBackStack("other_details").commit();

    }
public void detailsPermission( int position) {
        Fragment fragment= new permission_details();
        Bundle b= new Bundle();
        b.putSerializable(keys.PERMISSION,vList.get(position));
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment,"permission_details").addToBackStack("permission_details").commit();

    }

    public void vacationJsonParse(String response){
        try {
            JSONObject ob= new JSONObject(response);
            JSONArray v_arr=ob.getJSONArray("DataTable");
            for(int i=0;i<v_arr.length();i++){
                JSONObject v_object=v_arr.getJSONObject(i);
                int emp_code=v_object.getInt("EMP_CODE");
                String vDate=v_object.getString("DATE_G");
                String vStartDate=v_object.getString("STARTDATE_G");
                String vEndDate= v_object.getString("ENDDATE_G");
                String vDesc= v_object.getString("V_DESC");
                String REJECT_NOTE= v_object.getString("REJECT_NOTE");
               // String vNote= v_object.getString("NOTE");
                int vSlice= v_object.getInt("VAC_SLICE_CODE");

                int isUsed= v_object.getInt("IS_USED");
                int record_id= v_object.getInt("CODE");
                int isRejected= v_object.getInt("IS_REJECT");
                int vAmount= v_object.getInt("C_AMOUNT");
                String emp_name="";
                if(all){
                    emp_name=v_object.getString("NM_AR");
                }
                String statues;
                if (isRejected==1){
                    statues=getString(R.string.rejected);
                }
                else if(isUsed==1){
                    statues=getString(R.string.accept);

                }
                else{
                    statues=getString(R.string.pending);

                }

                vacationClass vObj= new vacationClass(String.valueOf(emp_code),emp_name,statues,vStartDate,vEndDate,vDesc,REJECT_NOTE,String.valueOf(vAmount),record_id,String.valueOf(vSlice));
                //  info_qualification_class qObject= new info_qualification_class(String.valueOf(qCode),q_name,q_date,q_major,q_note);
                vList.add(vObj);
            }
            viewList(vList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void loanJsonParsing(String response){
Log.d("loan",response);
        try {
            JSONObject ob= new JSONObject(response);
            JSONArray v_arr=ob.getJSONArray("DataTable");
            for(int i=0;i<v_arr.length();i++){
                JSONObject v_object=v_arr.getJSONObject(i);
                int emp_code=v_object.getInt("EMP_CODE");
                String loan_date=v_object.getString("RECDATE_G");
                String loanDesc= v_object.getString("DESCRIPTION");
                String REJECT_NOTE= v_object.getString("REJECT_NOTE");
                 String Note= v_object.getString("NOTE");
                 int loan_code= v_object.getInt("LOAN_CODE");
                int isUsed= v_object.getInt("IS_USED");
                int record_id= v_object.getInt("CODE");
                int isRejected= v_object.getInt("IS_REJECT");
                int loanAmount= v_object.getInt("AMOUNT");
                String emp_name="";
                if(all){
                    emp_name=v_object.getString("NM_AR");
                }
                String statues;
                if (isRejected==1){
                    statues=getString(R.string.rejected);
                }
                else if(isUsed==1){
                    statues=getString(R.string.accept);

                }
                else{
                    statues=getString(R.string.pending);

                }

                vacationClass vObj= new vacationClass(String.valueOf(emp_code),emp_name,statues,loan_date,loanDesc,REJECT_NOTE,String.valueOf(loanAmount),record_id,Note,String.valueOf(loan_code));
                //  info_qualification_class qObject= new info_qualification_class(String.valueOf(qCode),q_name,q_date,q_major,q_note);
                vList.add(vObj);
            }
            viewList(vList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void custodyJsonParsing(String response){
        Log.d("loan",response);
        try {
            JSONObject ob= new JSONObject(response);
            JSONArray v_arr=ob.getJSONArray("DataTable");
            for(int i=0;i<v_arr.length();i++){
                JSONObject v_object=v_arr.getJSONObject(i);
                int emp_code=v_object.getInt("EMP_CODE");
                String date=v_object.getString("DT_G");
                String custodyDesc= v_object.getString("CUST_DESC");
                String REJECT_NOTE= v_object.getString("REJECT_NOTE");
                String Note= v_object.getString("NOTE");
                int type= v_object.getInt("CUSTOD_CODE");
                int isUsed= v_object.getInt("IS_USED");
                int record_id= v_object.getInt("CODE");
                int isRejected= v_object.getInt("IS_REJECT");
                int custodyAmount= v_object.getInt("AMOUNT");
                String emp_name="";
                if(all){
                    emp_name=v_object.getString("NM_AR");
                }
                String statues;
                if (isRejected==1){
                    statues=getString(R.string.rejected);
                }
                else if(isUsed==1){
                    statues=getString(R.string.accept);

                }
                else{
                    statues=getString(R.string.pending);

                }

                vacationClass vObj= new vacationClass(String.valueOf(emp_code),emp_name,statues,date,custodyDesc,REJECT_NOTE,String.valueOf(custodyAmount),record_id,Note,String.valueOf(type));
                //  info_qualification_class qObject= new info_qualification_class(String.valueOf(qCode),q_name,q_date,q_major,q_note);
                vList.add(vObj);
            }
            viewList(vList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void PermissionJsonParsing(String response){
        Log.d("loan",response);
        try {
            JSONObject ob= new JSONObject(response);
            JSONArray v_arr=ob.getJSONArray("DataTable");
            for(int i=0;i<v_arr.length();i++){
                JSONObject v_object=v_arr.getJSONObject(i);
                int emp_code=v_object.getInt("EMP_CODE");
                String date=v_object.getString("DT_G");
                String emp_name="";
                if(all){
                 emp_name=v_object.getString("NM_AR");
                }
                String Desc= v_object.getString("P_DESC");
                String REJECT_NOTE= v_object.getString("REJECT_NOTE");
                 String Note= v_object.getString("NOTE");
                int isUsed= v_object.getInt("IS_USED");
                int record_id= v_object.getInt("CODE");
                int isRejected= v_object.getInt("IS_REJECT");
                int per_type= v_object.getInt("PERTYPE");
                String perTypeTxt=getPermissionNameById(per_type);
                String statues;
                if (isRejected==1){
                    statues=getString(R.string.rejected);
                }
                else if(isUsed==1){
                    statues=getString(R.string.accept);

                }
                else{
                    statues=getString(R.string.pending);

                }

                vacationClass vObj= new vacationClass(String.valueOf(emp_code),emp_name,statues,date,Desc,REJECT_NOTE,perTypeTxt,record_id,Note,String.valueOf(per_type));
                //  info_qualification_class qObject= new info_qualification_class(String.valueOf(qCode),q_name,q_date,q_major,q_note);
                vList.add(vObj);
            }
            viewList(vList);
        } catch (JSONException e) {
            e.printStackTrace();
        }}
    public void otherJsonParsing(String response){
        Log.d("loan",response);
        try {
            JSONObject ob= new JSONObject(response);
            JSONArray v_arr=ob.getJSONArray("DataTable");
            for(int i=0;i<v_arr.length();i++){
                JSONObject v_object=v_arr.getJSONObject(i);
                int emp_code=v_object.getInt("EMP_CODE");
                String date=v_object.getString("RQ_DATETIME");
                String Desc= v_object.getString("RQ_BODY");
                String REJECT_NOTE= v_object.getString("REJECT_NOTE");
                String title_txt= v_object.getString("RQ_TITLE");
                String Note= v_object.getString("NOTE");
                int isUsed= v_object.getInt("IS_USED");
                int record_id= v_object.getInt("ID");
                int isRejected= v_object.getInt("IS_REJECT");
                String req_type= v_object.getString("RQ_TYPE");
              //String perTypeTxt=getPermissionNameById(per_type);
                String emp_name="";
                if(all){
                    emp_name=v_object.getString("NM_AR");
                }
                String statues;
                if (isRejected==1){
                    statues=getString(R.string.rejected);
                }
                else if(isUsed==1){
                    statues=getString(R.string.accept);

                }
                else{
                    statues=getString(R.string.pending);

                }

                vacationClass vObj= new vacationClass(String.valueOf(emp_code),emp_name,statues,date,Desc,REJECT_NOTE,title_txt,record_id,Note,req_type);
                //  info_qualification_class qObject= new info_qualification_class(String.valueOf(qCode),q_name,q_date,q_major,q_note);
                vList.add(vObj);
            }
            viewList(vList);
        } catch (JSONException e) {
            e.printStackTrace();
        }}
    public  void viewList(ArrayList<vacationClass>list){
        GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setHasFixedSize(true);
        adapter= new vacationAdapter(list, getActivity(), new rec_interface() {
            @Override
            public void onRecItemSelected(int position, View view) {
                PopupMenu menu=    showMenu(view);
                Log.d("pos", String.valueOf(position));
                if(!addButton){ // if admin  not see only details
                    menu.getMenu().getItem(2).setVisible(false);
                    menu.getMenu().getItem(1).setVisible(false);

                }

                if(!vList.get(position).vStatues.equals(getString(R.string.pending))){
                    menu.getMenu().getItem(2).setEnabled(false);
                }

                onVacationMenuItemSelected(menu,position);
            }
        },R.layout.v_row);
        recyclerView.setAdapter(adapter);
    }


public void getRequestList(String function_name){
      vList= new ArrayList<>();

        api.getTypes(org_id, function_name, "null", emp_code, "null",isUsed,isRejected,new RequestInterface() {
    @Override
    public void onResponse(String response) {
        String n= response.substring(1,response.length()-1);
        n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
        swipeRefreshLayout.setRefreshing(false);

        Log.i("MainActivity", n);
        if(type.equals(keys.VACACTION)) {

            vacationJsonParse(n);
        }
        else if(type.equals(keys.LOAN)) {

            loanJsonParsing(n);
        }
        else if( type.equals(keys.CUSTODY)){
              custodyJsonParsing(n);
        } else if( type.equals(keys.PERMISSION)){
            PermissionJsonParsing(n);
        }else if( type.equals(keys.OTHER)){
            otherJsonParsing(n);
        }


    }

    @Override
    public void onError() {

    }
});


    }
    public void init(){
        api = new Api(getActivity());

        shared = getActivity().getSharedPreferences("user", 0);
        org_id = shared.getString("org_id", "");
        emp_code = shared.getString("emp_code", "");
        job_dgr=shared.getInt("jobDegree",0);

    }
    public PopupMenu showMenu( View view) {

        final PopupMenu menu= new PopupMenu(getActivity(),view);
        menu.getMenuInflater().inflate(R.menu.details_edit_delete, menu.getMenu());
        menu.show();
        return menu;


    }
    public void onVacationMenuItemSelected(PopupMenu menu, final int position){
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {


                int id =menuItem .getItemId();
                if(id==R.id.details){
                    if(type.equals(keys.VACACTION)){
                    detailsVacation(position); }
                    else if(type.equals(keys.LOAN)){
                        detailsLoan(position);
                    }
                 else if(type.equals(keys.CUSTODY)){

                        detailsCustody(position);
                    } else if(type.equals(keys.OTHER)){

                        detailsOther(position);
                    }else if(type.equals(keys.PERMISSION)){

                        detailsPermission(position);
                    }

                }
                else if(id==R.id.delete2){
                  deleteWindow(position);


                }
                else  if(id==R.id.edit){
                    if(type.equals(keys.VACACTION)){
                    editVacation(position);
                    }
                   else if(type.equals(keys.LOAN)){
                        editLoan(position);
                    }
                    else if(type.equals(keys.CUSTODY)){
                        editCustody(position);
                    }  else if(type.equals(keys.PERMISSION)){
                        editPermission(position);
                    }else if(type.equals(keys.OTHER)){
                        editOther(position);
                    }

                }
                return  false;
            }
        });
    }

public void deleteVaction(int record_id, final int item_position, String functionName){
        api.Delete(getString(R.string.requests), org_id, emp_code, functionName,String.valueOf( record_id), new
                RequestInterface() {
                    @Override
                    public void onResponse(String response) {
                        String n= response.substring(1,response.length()-1);
                        n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                        try {
                            JSONObject object= new JSONObject(n);
                            String msg=object.getString("Msg");
                            if(msg.equals("Success")){
                                vList.remove(item_position);
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
 public void deleteWindow(final int position){
     normalWindow.dialogOkCancel(getString(R.string.delete )+"?", new dialog_interface() {
         @Override
         public void onDialogOkClick(AlertDialog alertDialog) {
             if(type.equals(keys.VACACTION)) {
                 deleteVaction(vList.get(position).getRecordId(), position, "Delete_RQ_Vacation");
             }
           else  if(type.equals(keys.LOAN)) {
                 deleteVaction(vList.get(position).getRecordId(), position, "Delete_RQ_Loan");
             }
             else  if(type.equals(keys.CUSTODY)) {
                 deleteVaction(vList.get(position).getRecordId(), position, "Delete_RQ_Custody");
             }
             else  if(type.equals(keys.PERMISSION)) {
                 deleteVaction(vList.get(position).getRecordId(), position, "Delete_RQ_Permission");
             } else  if(type.equals(keys.OTHER)) {
                 deleteVaction(vList.get(position).getRecordId(), position, "Delete_RQ_Other");
             }

         }

         @Override
         public void onDialogCancelClick(AlertDialog alertDialog) {

         }
     }, getActivity());
 }
    public void  getPermissionTypeList(final String function_name){
        api.getTypes(org_id, "Get_PermissionTypes", String.valueOf(job_dgr), "null","null","null","null",new RequestInterface() {
            @Override
            public void onResponse(String response) {
                String n = response.substring(1, response.length() - 1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                Log.d("perm",n);
                try {
                    JSONObject ob= new JSONObject(n);
                    JSONArray typesArray=ob.getJSONArray("DataTable");
                    for(int i=0; i<typesArray.length();i++){
                        JSONObject loanObject= typesArray.getJSONObject(i);
                        int permissionCode=loanObject.getInt("ITEM_CODE");
                        String permissionName=loanObject.getString("ITEM_NMAR");
                       PermissionTypeList .add(new importatnt(permissionCode,permissionName));
                       // adapter.add(permissionName);

                    }
                    getRequestList(function_name);
               //     permission_type.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError() {

            }
        });

}
    public String getPermissionNameById(int permission_code){
        for( int i =0; i<PermissionTypeList.size();i++){
            int permission_code2=PermissionTypeList.get(i).getqTypeId();
             if(permission_code==permission_code2){
             return    PermissionTypeList.get(i).getqName();
             }
        }
return  null;
    }
    public void adminRequests(){
        if(type.equals(keys.VACACTION)){
            getRequestList("Get_All_Vacation_RQ");
            getActivity().setTitle(getString(R.string.vacation_request));

        }
        else if(type.equals(keys.LOAN)){
            getRequestList("Get_All_Loan_RQ");
            getActivity().setTitle(getString(R.string.loan_request));

        }
        else if(type.equals(keys.CUSTODY)){
            getRequestList("Get_All_Custody_RQ");

            getActivity().setTitle(getString(R.string.custody_request));
        } else if(type.equals(keys.OTHER)){
            getRequestList("Get_All_Other_RQ");
            getActivity().setTitle(getString(R.string.another_request));
        }
        else if(type.equals(keys.PERMISSION)){
            getPermissionTypeList("Get_All_Permission_RQ");
            getActivity().setTitle(getString(R.string.permission_request));
        }
    }
    public void employeeRequest(){
        if(type.equals(keys.VACACTION)){
            getRequestList("Get_Emp_Vacation_RQ");
            getActivity().setTitle(getString(R.string.vacation_request));

        }
        else if(type.equals(keys.LOAN)){
            getRequestList("Get_Emp_Loan_RQ");

            getActivity().setTitle(getString(R.string.loan_request));

        }
        else if(type.equals(keys.CUSTODY)){
            getRequestList("Get_Emp_Custody_RQ");

            getActivity().setTitle(getString(R.string.custody_request));
        } else if(type.equals(keys.OTHER)){
            getRequestList("Get_Emp_Other_RQ");
            getActivity().setTitle(getString(R.string.another_request));
        }
        else if(type.equals(keys.PERMISSION)){
            getPermissionTypeList("Get_Emp_Permission_RQ");
            getActivity().setTitle(getString(R.string.permission_request));
        }
    }

    @Override
    public void onRefresh() {
        if(all){
            adminRequests();
        }
        else{
            employeeRequest();
        }
    }
}