package com.androidmax.max.hr.Employee.payrollPkg;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.androidmax.max.hr.R;

import java.util.ArrayList;

public class currentMonthAdapter  extends RecyclerView.Adapter<currentMonthAdapter.viewHolder> {
    ArrayList<payrollClass> list;
    Activity activity;
    String prev_Emp="null";
    int sumRaise=0;
    int sumDeduction=0;

    public currentMonthAdapter(ArrayList<payrollClass> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public currentMonthAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.payroll_current_month_row,null);
        currentMonthAdapter.viewHolder rc= new currentMonthAdapter.viewHolder(v);
        return rc;
    }

    @Override
    public void onBindViewHolder(currentMonthAdapter.viewHolder holder, int position) {
        holder.name.setText(list.get(position).getItemName());
        holder.amount.setText(list.get(position).getItemAmount());
        holder.emp_name.setText(list.get(position).getEmp_name());
        holder.emp_code.setText(list.get(position).getEmp_code());
        String currentEmpCode=list.get(position).getEmp_code();
        int financialType=list.get(position).getType();
        int amountValue=Integer.parseInt(list.get(position).getItemAmount());
         if(financialType==3||financialType==5||financialType==6||financialType==9){
             sumDeduction=sumDeduction+amountValue;
             holder.icon.setImageResource(R.drawable.down);
         }
         else {
             sumRaise=sumRaise+amountValue;
             holder.icon.setImageResource(R.drawable.up);


         }

               if(((position<list.size()-1)&&!currentEmpCode.equals(list.get(position+1).getEmp_code()))||position==list.size()-1){
             holder.totalLayout.setVisibility(View.VISIBLE);
             holder.deduction.setText(String.valueOf(sumDeduction));
             holder.raise.setText(String.valueOf(sumRaise));
            // holder.line.setVisibility(View.VISIBLE);
             sumDeduction=0;
             sumRaise=0;
         }

         if(!prev_Emp.equals(currentEmpCode)){
             holder.emp_info.setVisibility(View.VISIBLE);
             prev_Emp=currentEmpCode;
         }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{

        TextView  name,amount,emp_code, emp_name, raise, deduction;
        LinearLayout emp_info,totalLayout;
        ImageView icon ;
        View line;
        public viewHolder(View itemView) {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.name) ;
            icon=(ImageView) itemView.findViewById(R.id.icon) ;
            amount=(TextView)itemView.findViewById(R.id.amount) ;
            line=(View)itemView.findViewById(R.id.line) ;
            emp_code=(TextView)itemView.findViewById(R.id.emp_code) ;
            emp_name=(TextView)itemView.findViewById(R.id.emp_name) ;
            raise=(TextView)itemView.findViewById(R.id.raiseValue) ;
            deduction=(TextView)itemView.findViewById(R.id.deductionValue) ;
            emp_info=(LinearLayout) itemView.findViewById(R.id.emp_info_layout) ;
            totalLayout=(LinearLayout) itemView.findViewById(R.id.totalLayout) ;
            /*SharedPreferences shared = activity.getSharedPreferences("user",0);
            String emp_code= shared.getString("emp_code","");
            String admin_emp_code= shared.getString("admin_emp_code","");
            Boolean isAdmin= shared.getBoolean("admin",true);
           if(!isAdmin){
                emp_info.setVisibility(View.GONE);
            }*/
            if(!PayrollCurrentMonth.all){
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
