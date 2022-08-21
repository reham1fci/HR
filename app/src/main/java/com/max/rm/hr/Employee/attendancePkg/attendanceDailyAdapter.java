package com.androidmax.max.hr.Employee.attendancePkg;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidmax.max.hr.R;

import java.util.ArrayList;

public class attendanceDailyAdapter extends RecyclerView.Adapter<attendanceDailyAdapter.viewHolder> {
        ArrayList<attendanceDaily_class> attend_list;
        Activity activity;

public attendanceDailyAdapter(ArrayList<attendanceDaily_class> attend_list, Activity activity) {
        this.attend_list = attend_list;
        this.activity = activity;
        }

@Override
public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_daily_row,null);
        attendanceDailyAdapter.viewHolder rc= new attendanceDailyAdapter.viewHolder(v);
        return rc;
        }

@Override
public void onBindViewHolder(viewHolder holder, int position) {
        holder.date.setText(attend_list.get(position).getDay()+"-"+attend_list.get(position).getDate());
        holder.period.setText(attend_list.get(position).getPeriod());
        holder.emp_name.setText(attend_list.get(position).getEmpName());
        holder.emp_code.setText(attend_list.get(position).getEmpCode());
    check(attend_list.get(position).getCheck_in(), holder.chek_in);
        check(attend_list.get(position).getCheck_out(), holder.chek_out);
    Log.d("msg",attend_list.get(position).getState());
        if(attend_list.get(position).getState().equals("حاضر")){
        holder.state.setText(activity.getString(R.string.present));
        holder.icon.setImageResource(R.drawable.true_icon);
        }
        else if(attend_list.get(position).getState().equals("غياب"))
            {
            holder.state.setText(activity.getString(R.string.absence));
            holder.icon.setImageResource(R.drawable.false_icon);

        }
        else {
            holder.state.setText(attend_list.get(position).getState());
            holder.icon.setImageResource(R.drawable.a2);

        }

        }

@Override
public int getItemCount() {
        return attend_list.size();
        }

class viewHolder extends RecyclerView.ViewHolder{

    TextView date, period, chek_in, chek_out,state,emp_code, emp_name;;
    ImageView icon;
    LinearLayout emp_info;
    public viewHolder(View itemView) {
        super(itemView);
        icon=(ImageView) itemView.findViewById(R.id.icon);
        date=(TextView) itemView.findViewById(R.id.date);
        period=(TextView)itemView.findViewById(R.id.period) ;
        chek_in=(TextView)itemView.findViewById(R.id.check_in) ;
        chek_out=(TextView)itemView.findViewById(R.id.check_out) ;
        state=(TextView)itemView.findViewById(R.id.state) ;
        emp_code=(TextView)itemView.findViewById(R.id.emp_code) ;
        emp_name=(TextView)itemView.findViewById(R.id.emp_name) ;
        emp_info=(LinearLayout) itemView.findViewById(R.id.emp_info_layout) ;
        SharedPreferences shared = activity.getSharedPreferences("user",0);
        String emp_code= shared.getString("emp_code","");
        String admin_emp_code= shared.getString("admin_emp_code","");
        Boolean isAdmin= shared.getBoolean("admin",true);
        if(!isAdmin){
            emp_info.setVisibility(View.GONE);
        }
    }
}
    public void check( String text, TextView v){
        if (text.equals("null")){
            v.setText("");

        }
        else {
            v.setText(text);

        }}
}