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

public class info_qualification_adapter extends RecyclerView.Adapter<info_qualification_adapter.viewHolder> {
    ArrayList<info_qualification_class>list;
    Activity activity;
    //refreshList f;
    rec_interface rec_interface;

    public info_qualification_adapter(ArrayList<info_qualification_class> list, Activity activity, com.androidmax.max.hr.Employee.infoPkg.rec_interface rec_interface) {
        this.list = list;
        this.activity = activity;
        this.rec_interface = rec_interface;
    }

  /*  public info_qualification_adapter(ArrayList<info_qualification_class> list, Activity activity, refreshList refreshList ) {
        this.list = list;
        this.activity = activity;
        this.f=refreshList;
    }*/

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.info_qualification_row ,null);
        info_qualification_adapter.viewHolder rc= new info_qualification_adapter.viewHolder(v);
        return rc;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, final int position) {
        holder.name.setText(list.get(position).getQualification_name());
        holder.date.setText(list.get(position).getQualification_date());
      check( list.get(position).getQualification_note(),holder.note);
        holder.major.setText(list.get(position).getQualification_major());
      /*  holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         deleteQualification(list.get(position).getQ_id(), position);
            }
        });*/
        /*holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              f.onEdit(position,list.get(position).getQ_id());
            }
        });*/
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
        TextView name, date,note, major;
       // ImageView edit, delete;
        CardView card;
        public viewHolder(View itemView) {
            super(itemView);
            card=(CardView)itemView.findViewById(R.id.card);
            name=(TextView)itemView.findViewById(R.id.qual_name) ;
            date=(TextView)itemView.findViewById(R.id.qual_date) ;
            note=(TextView)itemView.findViewById(R.id.note) ;
            major=(TextView)itemView.findViewById(R.id.qual_major) ;
         //   edit=(ImageView) itemView.findViewById(R.id.edit);
          //  delete=(ImageView) itemView.findViewById(R.id.delete);

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
    /*public void deleteQualification(String id, final int position){
        final Api api= new Api(activity);
        SharedPreferences shared= activity.getSharedPreferences("user",0);

        String org_id=shared.getString("org_id","");
        String emp_code=shared.getString("emp_code","");
        api.DeleteQualification(org_id, emp_code, "Delete_Qulification", id, new RequestInterface() {
            @Override
            public void onResponse(String response) {
                Log.d("responsen", response);
                //  progress.setVisibility(View.GONE);
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                try {
                    JSONObject object= new JSONObject(n);
                    String msg=object.getString("Msg");
                    if(msg.equals("Success")){
                        //f.onDelete(position);
                      //  list.remove(position);
                        Toast.makeText(activity, "done", Toast.LENGTH_SHORT).show();

                      // activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new info_qualification()).commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {

            }
        });

    }*/
}
