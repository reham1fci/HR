package com.max.rm.hr.Employee.requestpkg;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.max.rm.hr.Employee.infoPkg.rec_interface;
import com.max.rm.hr.R;
import com.max.rm.hr.keys;

import java.util.ArrayList;

public class vacationAdapter extends RecyclerView.Adapter<vacationAdapter.viewHolder> {
    ArrayList<vacationClass> list;
    Activity activity;
    //refreshList f;
    rec_interface rec_interface;
     int layoutId;

    public vacationAdapter(ArrayList<vacationClass> list, Activity activity, rec_interface rec_interface, int layoutId) {
        this.list = list;
        this.activity = activity;
        this.rec_interface = rec_interface;
        this.layoutId = layoutId;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(layoutId,null);
        vacationAdapter.viewHolder rc= new vacationAdapter.viewHolder(v);
        return rc;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, final int position) {
        holder.emp_name.setText(list.get(position).getEmp_name());
        holder.emp_code.setText(list.get(position).getEmp_code());
        holder.amount.setText(list.get(position).getvAmount());
        getStatues(list.get(position).getvStatues(),holder.status);
        holder.startDate.setText(list.get(position).getvStartDate());
        holder.endDate.setText(list.get(position).getvEndDate());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rec_interface.onRecItemSelected(position,view);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView amount, startDate,endDate, status,amount_txt,startDate_txt,endDate_txt, emp_code, emp_name;
        // ImageView edit, delete;
        LinearLayout emp_info;
        CardView card;
        public viewHolder(View itemView) {
            super(itemView);
            card=(CardView)itemView.findViewById(R.id.card);
            amount=(TextView)itemView.findViewById(R.id.nums) ;
            amount_txt=(TextView)itemView.findViewById(R.id.amount_txt) ;
            startDate=(TextView)itemView.findViewById(R.id.start_date) ;
            startDate_txt=(TextView)itemView.findViewById(R.id.start_date_txt) ;
            endDate_txt=(TextView)itemView.findViewById(R.id.end_date_txt) ;
            endDate=(TextView)itemView.findViewById(R.id.end_date) ;
            status=(TextView)itemView.findViewById(R.id.status) ;
            emp_code=(TextView)itemView.findViewById(R.id.emp_code) ;
            emp_name=(TextView)itemView.findViewById(R.id.emp_name) ;
            emp_info=(LinearLayout) itemView.findViewById(R.id.emp_info_layout) ;

            SharedPreferences shared = activity.getSharedPreferences("user",0);
            String emp_code= shared.getString("emp_code","");
            String admin_emp_code= shared.getString("admin_emp_code","");
            Boolean isAdmin= shared.getBoolean("admin",true);


            if(!requestsList.all){
                emp_info.setVisibility(View.GONE);
            }
            if(requestsList.type.equals(keys.LOAN)||requestsList.type.equals(keys.CUSTODY)){
                endDate.setVisibility(View.GONE);
                endDate_txt.setVisibility(View.GONE);
                amount_txt.setText(activity.getString(R.string.amount));
                startDate_txt.setText(activity.getString(R.string.date));
            }
          else  if(requestsList.type.equals(keys.PERMISSION)){
                endDate.setVisibility(View.GONE);
                endDate_txt.setVisibility(View.GONE);
                amount_txt.setText(activity.getString(R.string.permission_type));
                startDate_txt.setText(activity.getString(R.string.date));
            } else  if(requestsList.type.equals(keys.OTHER)){
                endDate.setVisibility(View.GONE);
                endDate_txt.setVisibility(View.GONE);
                amount_txt.setText(activity.getString(R.string.title));
                startDate_txt.setText(activity.getString(R.string.date));
            }
        }
    }
    public void getStatues(String status, TextView statusView){
        if(status.equals(activity.getString(R.string.pending))){
            statusView.setText(activity.getString(R.string.pending));
            Drawable img = activity.getResources().getDrawable( R.drawable.warning );
            img.setBounds( 0, 0, 60, 60 );
            statusView.setCompoundDrawables( img, null, null, null );
            statusView.setBackgroundColor(activity.getResources().getColor(R.color.yellow));



        }
        else   if(status.equals(activity.getString(R.string.rejected))){
            statusView.setText(activity.getString(R.string.rejected));
            Drawable img = activity.getResources().getDrawable( R.drawable.false_icon );
            img.setBounds( 0, 0, 60, 60 );
            statusView.setCompoundDrawables( img, null, null, null );

            statusView.setBackgroundColor(activity.getResources().getColor(R.color.red));

        }
        else
        {
            statusView.setText(activity.getString(R.string.accept));
            Drawable img = activity.getResources().getDrawable( R.drawable.true_icon );
            img.setBounds( 0, 0, 60, 60 );
            statusView.setCompoundDrawables( img, null, null, null );
            // v_status.setCompoundDrawablesWithIntrinsicBounds( R.drawable.true_icon, 0, 0, 0);
            statusView.setBackgroundColor(activity.getResources().getColor(R.color.green));

        }
    }
}
