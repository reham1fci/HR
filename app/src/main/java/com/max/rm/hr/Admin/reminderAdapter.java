package com.androidmax.max.hr.Admin;
import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.androidmax.max.hr.Employee.infoPkg.rec_interface;
import com.androidmax.max.hr.R;
import java.util.ArrayList;

public class reminderAdapter extends RecyclerView.Adapter<reminderAdapter.viewHolder> {
    ArrayList<reminderClass> tasksList;
    Activity activity;
    com.androidmax.max.hr.Employee.infoPkg.rec_interface rec_interface;
    public reminderAdapter(ArrayList<reminderClass> tasksList, Activity activity,rec_interface rec_interface) {
        this.tasksList = tasksList;
        this.activity = activity;
        this.rec_interface=rec_interface;
    }

    @Override
    public reminderAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row,null);
        reminderAdapter.viewHolder rc= new reminderAdapter.viewHolder(v);
        return rc;
    }

    @Override
    public void onBindViewHolder(reminderAdapter.viewHolder holder, final int position) {
        holder.task_title.setText(tasksList.get(position).getTaskTitle());
        if(tasksList.get(position).getDate().equals("null")){
            holder.task_date.setText(tasksList.get(position).getTime());

        }
        else {
        holder.task_date.setText(tasksList.get(position).getDate());}
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rec_interface.onRecItemSelected(position,view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{

        TextView task_title, task_date;
        CardView cardView;
        public viewHolder(View itemView) {
            super(itemView);
            task_title=(TextView)itemView.findViewById(R.id.taskTitle) ;
            task_date=(TextView)itemView.findViewById(R.id.taskDate) ;
            cardView=(CardView)itemView.findViewById(R.id.card) ;
        }
    }}