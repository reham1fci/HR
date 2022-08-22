package com.max.rm.hr.Employee.payrollPkg;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.max.rm.hr.Employee.infoPkg.rec_interface;
import com.max.rm.hr.R;

import java.util.ArrayList;

public class prevMonthAdapter  extends RecyclerView.Adapter<prevMonthAdapter.viewHolder> {
    ArrayList<payrollClass> list;
    Activity activity;
    rec_interface object;

    public prevMonthAdapter(ArrayList<payrollClass> list, Activity activity,rec_interface object) {
        this.list = list;
        this.activity = activity;
        this.object=object;
    }

    @Override
    public prevMonthAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.payroll_prev_month_row,null);
        prevMonthAdapter.viewHolder rc= new prevMonthAdapter.viewHolder(v);
        return rc;
    }

    @Override
    public void onBindViewHolder(viewHolder holder,  int position) {
        holder.raise.setText(list.get(position).getRaise());
        holder.deduction.setText(list.get(position).getDeduction());
        holder.total.setText(list.get(position).getTotal());
        holder.emp_name.setText(list.get(position).getEmp_name());
        holder.emp_code.setText(list.get(position).getEmp_code());
        holder.date.setText(list.get(position).getDate());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                object.onRecItemSelected(position, view);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{

        TextView total,emp_code, emp_name, raise, deduction, date;
        LinearLayout emp_info;
        CardView card;
        public viewHolder(View itemView) {
            super(itemView);

            total=(TextView)itemView.findViewById(R.id.total) ;
            emp_code=(TextView)itemView.findViewById(R.id.emp_code) ;
            emp_name=(TextView)itemView.findViewById(R.id.emp_name) ;
            raise=(TextView)itemView.findViewById(R.id.raise) ;
            deduction=(TextView)itemView.findViewById(R.id.deduction) ;
            date=(TextView)itemView.findViewById(R.id.date) ;
            card=(CardView) itemView.findViewById(R.id.card);
            emp_info=(LinearLayout) itemView.findViewById(R.id.emp_info_layout) ;
            if(!PayrollPrevMonth.all){
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

        }
    }
}
