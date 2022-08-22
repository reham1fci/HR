package com.max.rm.hr.ChatPkg;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.annotation.NonNull;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.max.rm.hr.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

class MessagesAdapter  extends ArrayAdapter<Message> {
    TextView messageView;
    String chat_key="";
    private AdapterCallback mAdaptercallback;

    MessagesAdapter(ArrayList<Message> messages,Activity activity){
        super(activity,0, messages);
    }
    MessagesAdapter(ArrayList<Message> messages,Activity activity, String chat_key){
        super(activity,0, messages);
        this.chat_key=chat_key;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView = super.getView(position, convertView, parent);
        Message message = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_item, parent, false);
        }
        String time = getDate(Long.parseLong(message.getTime()));
        String sender_name=message.getSenderName();
        if(sender_name!=null){
        String arr[] = sender_name.split(" ", 2);
        sender_name=arr[0];}

        String message_txt=message.getmTxt();
         messageView = (TextView)convertView.findViewById(R.id.message);
        messageView.setText(sender_name+"\n"+message_txt+"   "+time);

        String emp_code;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)messageView.getLayoutParams();
      SharedPreferences shared= getContext().getSharedPreferences("user",0);
      if(shared.getBoolean("admin",false)){
           emp_code=shared.getString("admin_emp_code","");
      }
      else {
 emp_code= shared.getString("emp_code","");
      }
        int sdk = Build.VERSION.SDK_INT;
        if (message.getmSender().equals(emp_code)){
            if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
                messageView.setBackgroundResource(R.drawable.imageee);
            } else{
                messageView.setBackgroundResource(R.drawable.imageee);
            }
            layoutParams.gravity = Gravity.RIGHT;
        }else{
            if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
                messageView.setBackgroundResource(R.drawable.bubble_left_gray);
            } else{
                messageView.setBackgroundResource(R.drawable.bubble_left_gray);
            }

            layoutParams.gravity = Gravity.LEFT;
        }

        messageView.setLayoutParams(layoutParams);


        return convertView;
    }
    private String getDate(long time) {
//
        Long tsLong = time*1000;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(tsLong);
        String date = DateFormat.format("hh:mm aa", cal).toString();

        return date;}
        public  void getSenderName(String empCode, final String message, final String time){

String chat_key =this.chat_key;

             FirebaseDatabase firebase_instance= FirebaseDatabase.getInstance();
             DatabaseReference sRef=firebase_instance.getReference("chat");
             sRef.child(chat_key).child("member_child").child(empCode).addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 Employees emp=    dataSnapshot.getValue(Employees.class);

         String sender_name=       emp.getEmp_name();

                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });

        }
    public void setAdapterCallback(AdapterCallback  adaptercallback){
        this.mAdaptercallback = adaptercallback;
    }
    public interface AdapterCallback {
        void MethodCallbackgo();
    }
}
