package com.androidmax.max.hr.Admin;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidmax.max.hr.Employee.employee_class;

import com.androidmax.max.hr.R;
import com.androidmax.max.hr.rec_interface2;

import java.util.ArrayList;

public class empListAdapter  extends RecyclerView.Adapter<empListAdapter.viewHolder> {
    ArrayList<employee_class> empList;
    ArrayList<employee_class> mStringFilterList;
    ValueFilter valueFilter;
    Activity activity;
    rec_interface2 rec_interface;
     int row_index=-1;
      ArrayList<Integer>selectedItem= new ArrayList<>();
    public empListAdapter(ArrayList<employee_class> empList, Activity activity,rec_interface2 rec_interface) {
        this.empList = empList;
        mStringFilterList = empList;

        this.activity = activity;
        this.rec_interface=rec_interface;
    }

    @Override
    public empListAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.emp_list_row,null);
        empListAdapter.viewHolder rc= new empListAdapter.viewHolder(v);
        return rc;
    }

    @Override
    public void onBindViewHolder(empListAdapter.viewHolder holder, final int position) {
        holder.job_name.setText(empList.get(position).getJob_name());
        holder.emp_name.setText(empList.get(position).getNameAr());
        holder.emp_code.setText(empList.get(position).getEMP_CODE());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(employeesList.type.equals("group")){

                    if (selectedItem.contains(position)){
                    selectedItem.remove(Integer.valueOf(position));
                }
                else {
                    selectedItem.add(position);
                }
                 row_index=position;

                  notifyDataSetChanged();
                }
                rec_interface.onRecItemSelected(position,view,empList);
            }
        });
        if(employeesList.type.equals("group")){
        if(selectedItem.contains(position)){
            holder.select.setBackgroundColor(Color.parseColor("#3878bc"));
        }
         else {
            holder.select.setBackgroundColor(Color.parseColor("#ffffff"));

        }
        }
    }

    @Override
    public int getItemCount() {
        return empList.size();
    }

     class viewHolder extends RecyclerView.ViewHolder{

        TextView job_name, emp_name, emp_code;
        CardView cardView;
         LinearLayout select;

        public viewHolder(View itemView) {
            super(itemView);
            job_name=(TextView)itemView.findViewById(R.id.emp_job) ;
            emp_name=(TextView)itemView.findViewById(R.id.emp_name) ;
            emp_code=(TextView)itemView.findViewById(R.id.emp_code) ;
            cardView=(CardView)itemView.findViewById(R.id.emp_card) ;
            select=(LinearLayout) itemView.findViewById(R.id.select) ;
        }

    }

    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<employee_class> filterList = new ArrayList<>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getNameAr().toUpperCase()).contains(constraint.toString().toUpperCase())||(mStringFilterList.get(i).getEMP_CODE().toUpperCase()).contains(constraint.toString().toUpperCase())||(mStringFilterList.get(i).getJob_name().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            empList = (ArrayList<employee_class>) filterResults.values;
            Log.d("list", String.valueOf(filterResults.count));
            notifyDataSetChanged();
        }
    }
}