package com.androidmax.max.hr.Employee.infoPkg;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidmax.max.hr.R;

import java.util.ArrayList;

public class info_docs_adapter  extends RecyclerView.Adapter<info_docs_adapter.viewHolder> {
ArrayList<info_docs_class>doc_list;
Activity activity;

    public info_docs_adapter(ArrayList<info_docs_class> doc_list, Activity activity) {
        this.doc_list = doc_list;
        this.activity = activity;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.info_doc_row,null);
        info_docs_adapter.viewHolder rc= new info_docs_adapter.viewHolder(v);
        return rc;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
 holder.doc_name.setText(doc_list.get(position).getDoc_Name());
 holder.emp_name.setText(doc_list.get(position).getEmpName());
 holder.emp_code.setText(doc_list.get(position).getEmpCode());
 info_personal.check(doc_list.get(position).getDocReleaseDate(),holder.doc_releaseDate);
 info_personal.check(doc_list.get(position).getDocExpireDate(),holder.doc_expireDate);
 String expired=doc_list.get(position).getDocExpireDate();
 int checkExpire=doc_list.get(position).getExp_after();

if(checkExpire!=0){
       if(checkExpire<0){
           holder.text.setText(activity.getString(R.string.expired));
           holder.icon.setImageResource(R.drawable.dis_like);

       }
        else if(checkExpire>0&&checkExpire<30){
           holder.text.setText(activity.getString(R.string.upon_expire));
           holder.icon.setImageResource(R.drawable.warning);
       }
       else {

           holder.text.setText(activity.getString(R.string.active));
           holder.icon.setImageResource(R.drawable.like);
       }
}
else{
    holder.text.setVisibility(View.INVISIBLE);
    holder.icon.setVisibility(View.INVISIBLE);
}

    }

    @Override
    public int getItemCount() {
        return doc_list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{

        TextView doc_name, doc_expireDate, doc_releaseDate, text, emp_name, emp_code;
        ImageView icon;
        LinearLayout emp_info;
        public viewHolder(View itemView) {
            super(itemView);
            icon=(ImageView) itemView.findViewById(R.id.icon);
            text=(TextView) itemView.findViewById(R.id.doc_type);
            doc_name=(TextView)itemView.findViewById(R.id.doc_name) ;
            doc_expireDate=(TextView)itemView.findViewById(R.id.end_date) ;
            doc_releaseDate=(TextView)itemView.findViewById(R.id.release_date) ;
            emp_code=(TextView)itemView.findViewById(R.id.emp_code) ;
            emp_name=(TextView)itemView.findViewById(R.id.emp_name) ;
            emp_info=(LinearLayout) itemView.findViewById(R.id.emp_info_layout) ;
            // to check if user account not open
            SharedPreferences shared = activity.getSharedPreferences("user",0);
            String emp_code= shared.getString("emp_code","");
            String admin_emp_code= shared.getString("admin_emp_code","");
            Boolean isAdmin= shared.getBoolean("admin",true);


            if(!isAdmin){
                emp_info.setVisibility(View.GONE);
            }
        }
    }

}
