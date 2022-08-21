package com.androidmax.max.hr.Employee.infoPkg;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidmax.max.hr.R;

import java.util.ArrayList;

public class info_financial_adapter extends RecyclerView.Adapter<info_financial_adapter.viewHolder>  {
    ArrayList<info_financial_class>list;
    Activity activity;

    public info_financial_adapter(ArrayList<info_financial_class> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.info_financial_row,null);
        info_financial_adapter.viewHolder rc= new viewHolder(v);
        return rc;    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
holder.name.setText(list.get(position).getName());
holder.price.setText(list.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
TextView name, price;
        public viewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name) ;
            price=(TextView)itemView.findViewById(R.id.price) ;

        }
    }
}
