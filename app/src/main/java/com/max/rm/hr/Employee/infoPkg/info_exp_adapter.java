package com.max.rm.hr.Employee.infoPkg;

import android.app.Activity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.max.rm.hr.R;

import java.util.ArrayList;

public class info_exp_adapter  extends RecyclerView.Adapter<info_exp_adapter.viewHolder> {
    ArrayList<info_exp_class> exp_list;
    Activity activity;
rec_interface rec_interface;
    public info_exp_adapter(ArrayList<info_exp_class> exp_list, Activity activity,rec_interface rec_interface) {
        this.exp_list = exp_list;
        this.activity = activity;
        this.rec_interface=rec_interface;
    }

    @Override
    public info_exp_adapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.info_exp_row,null);
        info_exp_adapter.viewHolder rc= new info_exp_adapter.viewHolder(v);
        return rc;
    }

    @Override
    public void onBindViewHolder(info_exp_adapter.viewHolder holder, final int position) {
        holder.job_name.setText(exp_list.get(position).getJob_name());
        holder.job_details.setText(exp_list.get(position).getJob_details());
        holder.job_years.setText(exp_list.get(position).getJob_years());
         holder.cardView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 rec_interface.onRecItemSelected(position,view);
             }
         });
    }

    @Override
    public int getItemCount() {
        return exp_list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{

        TextView job_name, job_details, job_years;
        CardView cardView;
        public viewHolder(View itemView) {
            super(itemView);
            job_name=(TextView)itemView.findViewById(R.id.exp_name) ;
            job_details=(TextView)itemView.findViewById(R.id.exp_details) ;
            job_years=(TextView)itemView.findViewById(R.id.exp_years) ;
             cardView=(CardView)itemView.findViewById(R.id.exp_card) ;
        }
    }}