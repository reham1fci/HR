package com.androidmax.max.hr.Employee.infoPkg;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidmax.max.hr.R;

import java.util.ArrayList;

public class info_family_adapter extends RecyclerView.Adapter<info_family_adapter.viewHolder> {
        ArrayList<info_family_class> family_list;
        Activity activity;

public info_family_adapter(ArrayList<info_family_class> family_list, Activity activity) {
        this.family_list = family_list;
        this.activity = activity;
        }

@Override
public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.info_family_row,null);
        info_family_adapter.viewHolder rc= new info_family_adapter.viewHolder(v);
        return rc;
        }

@Override
public void onBindViewHolder(viewHolder holder, int position) {
        holder.doc_name.setText(family_list.get(position).getFamilyRelation());
        holder.doc_releaseDate.setText(family_list.get(position).getReleaseDate());
        holder.doc_expireDate.setText(family_list.get(position).getExpireDate());
        }

@Override
public int getItemCount() {
        return family_list.size();
        }

class viewHolder extends RecyclerView.ViewHolder{

    TextView doc_name, doc_expireDate, doc_releaseDate;
    public viewHolder(View itemView) {
        super(itemView);
        doc_name=(TextView)itemView.findViewById(R.id.doc_name) ;
        doc_expireDate=(TextView)itemView.findViewById(R.id.end_date) ;
        doc_releaseDate=(TextView)itemView.findViewById(R.id.release_date) ;
    }
}}