package com.androidmax.max.hr.Employee.infoPkg;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidmax.max.hr.R;

import java.util.ArrayList;

public class inf_archive_adapter  extends RecyclerView.Adapter<inf_archive_adapter.view_holder>{
    ArrayList<info_archive_class> archive_list;
    Activity activity;
    rec_interface rec_interface;

    public inf_archive_adapter(ArrayList<info_archive_class> archive_list, Activity activity,rec_interface rec_interface) {
        this.archive_list = archive_list;
        this.activity = activity;
       this. rec_interface=rec_interface;
    }

    @Override
    public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.info_archive_row,null);
        inf_archive_adapter.view_holder rc= new inf_archive_adapter.view_holder(v);
        return rc;
    }

    @Override
    public void onBindViewHolder(view_holder holder, final int position) {
     holder.name.setText(archive_list.get(position).getName());
     holder.card.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             rec_interface.onRecItemSelected(position,view);


         }
     });
    }

    @Override
    public int getItemCount() {
        return archive_list.size();
    }

    class view_holder extends RecyclerView.ViewHolder{
    TextView name;
    CardView card;
         public view_holder(View itemView) {
             super(itemView);
             name=(TextView) itemView.findViewById(R.id.archiveName);
             card=(CardView)itemView.findViewById(R.id.card);

         }
     }

}
