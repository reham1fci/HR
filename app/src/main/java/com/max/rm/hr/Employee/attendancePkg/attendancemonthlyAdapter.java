package com.max.rm.hr.Employee.attendancePkg;

import android.app.Activity;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.max.rm.hr.R;

import java.util.ArrayList;

public class attendancemonthlyAdapter extends RecyclerView.Adapter<attendancemonthlyAdapter.viewHolder> {
    ArrayList<attendanceMonthly_class> attend_list;
    Activity activity;

    public attendancemonthlyAdapter(ArrayList<attendanceMonthly_class> attend_list, Activity activity) {
        this.attend_list = attend_list;
        this.activity = activity;
    }

    @Override
    public attendancemonthlyAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_monthly_row,null);
        attendancemonthlyAdapter.viewHolder rc= new attendancemonthlyAdapter.viewHolder(v);
        return rc;
    }

    @Override
    public void onBindViewHolder(attendancemonthlyAdapter.viewHolder holder, int position) {
        holder.date.setText(attend_list.get(position).getMonth()+"/"+attend_list.get(position).getYear());
        holder.emp_name.setText(attend_list.get(position).getEmpName());
        holder.emp_code.setText(attend_list.get(position).getEmpCode());
        holder.workH_require.setText(attend_list.get(position).getRequiredWorkHour());
        holder.work_h.setText(attend_list.get(position).getWorkHour());
        holder.late.setText(attend_list.get(position).getLateHour());
        holder.early.setText(attend_list.get(position).getCheckoutEarlyHour());
        holder.permission.setText(attend_list.get(position).getPermissionHour());
        holder.overtime.setText(attend_list.get(position).getOvertimeHour());
        holder.absence.setText(attend_list.get(position).getAbsenceHour());

    }

    @Override
    public int getItemCount() {
        return attend_list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        LinearLayout emp_info;

        TextView date, work_h, workH_require, late,early, permission,overtime, absence, emp_code, emp_name;
        public viewHolder(View itemView) {
            super(itemView);
            date=(TextView) itemView.findViewById(R.id.date);
            work_h=(TextView)itemView.findViewById(R.id.w_h) ;
            workH_require=(TextView)itemView.findViewById(R.id.w_h_required) ;
            late=(TextView)itemView.findViewById(R.id.late) ;
            early=(TextView)itemView.findViewById(R.id.early) ;
            permission=(TextView)itemView.findViewById(R.id.permission) ;
            overtime=(TextView)itemView.findViewById(R.id.overtime) ;
            absence=(TextView)itemView.findViewById(R.id.absence) ;
            emp_code=(TextView)itemView.findViewById(R.id.emp_code) ;
            emp_name=(TextView)itemView.findViewById(R.id.emp_name) ;
            emp_info=(LinearLayout) itemView.findViewById(R.id.emp_info_layout) ;
           // Log.d("activity",activity.getClass().getName());

           SharedPreferences shared = activity.getSharedPreferences("user",0);
          String emp_code= shared.getString("emp_code","");
          String admin_emp_code= shared.getString("admin_emp_code","");
          Boolean isAdmin= shared.getBoolean("admin",true);


            if(!isAdmin){
                emp_info.setVisibility(View.GONE);
            }
        }
    }}